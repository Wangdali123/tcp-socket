package com.wdl.tcp.bio;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * <p>Title: TcpBioClient</p>
 * <p>Description: TODO</p>
 * <p>Copyright: Copyright (c) 2019</p>
 * <p>Company: sodo</p>
 *
 * @author wangdali
 * @version 1.0
 * @date 2019/4/15 23:02
 */
public class TcpBioClient {

    public static void createSocket(String address, int port) throws IOException, InterruptedException {
        Socket socket = new Socket(address, port);
        PrintWriter printWriter =  new PrintWriter(socket.getOutputStream());
        while(true){
            printWriter.println("NEBS," + address + ":" + port);
            printWriter.flush();
            Thread.sleep(5000); //每隔5S发送心跳
        }
    }

}
