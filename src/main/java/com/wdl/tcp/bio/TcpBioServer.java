package com.wdl.tcp.bio;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>Title: TcpBioServer</p>
 * <p>Description: 长连接Tcp服务</p>
 * <p>Copyright: Copyright (c) 2019</p>
 * <p>Company: 王大厉</p>
 *
 * @author wangdali
 * @version 1.0
 * @date 2019/4/15 22:33
 */
public class TcpBioServer {

    private static volatile TcpBioServer tcpBioServer;

    private TcpBioServer(){}

    /**
     * 保存Socket对应关系
     */
    public ConcurrentHashMap<String, Socket> socketMap = new ConcurrentHashMap<>();

    /**
     * 双重检测获取单例(懒加载)
     * volatile解决指令乱序问题
     * @param port
     * @return
     */
    public static TcpBioServer getSingleInstance(int port) throws IOException {
        if(tcpBioServer == null){
            synchronized (TcpBioServer.class){
                if(tcpBioServer == null){
                    tcpBioServer = new TcpBioServer();
                    tcpBioServer.init(port);
                }
            }
        }
        return tcpBioServer;
    }

    private void init(int port) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        new Thread(()->{
            while (true){
                Socket socket = null;
                try {
                    socket = serverSocket.accept();
                    String key = socket.getInetAddress().getHostAddress() + socket.getPort();
                    socketMap.put(key, socket);
                    new Thread(new ReceiveRunnable(socket)).start(); //BIO 每个Socket需要单独开辟线程
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    class ReceiveRunnable implements Runnable{

        private Socket socket;

        public ReceiveRunnable(Socket socket){
            this.socket = socket;
        }

        @Override
        public void run() {
            while (true){
                if(!socket.isConnected() || socket.isClosed()){ //线程已关闭
                    return;
                }
                try {
                    InputStream inputStream = socket.getInputStream();
                    if(inputStream.available() > 0){ //接收区长度为空
                        byte[] bytes = new byte[inputStream.available()];
                        inputStream.read(bytes);
                        System.out.println("心跳：" + new String(bytes));
                        //业务处理(心跳、指令返回等)
                        PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
                        printWriter.println(new String(bytes)); //自动添加/r/n结束符
                        printWriter.flush(); //强制清空输出区(发送)
                    }
                    Thread.sleep(1000 * 3); //3S一次查询
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
