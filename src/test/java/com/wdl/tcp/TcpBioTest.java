package com.wdl.tcp;

import com.wdl.tcp.bio.TcpBioClient;
import com.wdl.tcp.bio.TcpBioServer;
import org.junit.Test;

import java.io.IOException;

/**
 * <p>Title: TcpBioTest</p>
 * <p>Description: TODO</p>
 * <p>Copyright: Copyright (c) 2019</p>
 * <p>Company: sodo</p>
 *
 * @author wangdali
 * @version 1.0
 * @date 2019/4/15 23:15
 */
public class TcpBioTest {

    @Test
    public void testBio() throws IOException, InterruptedException {
        TcpBioServer tcpBioServer = TcpBioServer.getSingleInstance(12002);
        Thread.sleep(5000); //等待服务器启动
        TcpBioClient.createSocket("127.0.0.1", 12002);
    }

}
