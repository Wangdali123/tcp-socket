package com.wdl.tcp.bio;

import java.util.concurrent.*;

/**
 * <p>Title: BioArrayThreadPool</p>
 * <p>Description: Socket定长线程池<br/>
 *  同时最多15个客户端,5个等待（服务端不计入）<br/>
 *  空闲5分钟后需要重新连接</p>
 * <p>Copyright: Copyright (c) 2019</p>
 * <p>Company: WDL</p>
 *
 * @author wangdali
 * @version 1.0
 * @date 2019/6/12 16:45
 */
public enum BioArrayThreadPoolExecutor {
    ;

    /**
     * 5分钟空闲，需要重新连接
     * 同时最多15个客户端,5个等待（服务端不计入）
     */
    private static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(16, 21, 5, TimeUnit.MINUTES, new ArrayBlockingQueue<>(5));

    BioArrayThreadPoolExecutor() {
    }

    public static void executor(Runnable runnable){
        threadPoolExecutor.execute(runnable);
    }

    public static <V> Future<V> sublimt(Callable<V> callable){
        return threadPoolExecutor.submit(callable);
    }

}
