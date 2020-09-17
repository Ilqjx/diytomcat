package cn.ilqjx.diytomcat.util;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author upfly
 * @create 2020-09-17 20:01
 */
public class ThreadPoolUtil {
    private static ThreadPoolExecutor threadPool = new ThreadPoolExecutor(20,
            100, 60, TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>(10));

    public static void run(Runnable runnable) {
        threadPool.execute(runnable);
    }
}
