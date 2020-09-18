package cn.ilqjx.diytomcat.test;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.util.NetUtil;
import cn.hutool.core.util.StrUtil;
import cn.ilqjx.diytomcat.util.MiniBrowser;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author upfly
 * @create 2020-09-11 19:30
 */
public class TestTomcat {
    private static int port = 18080;
    private static String ip = "127.0.0.1";

    @BeforeClass
    public static void beforeClass() {
        // 测试开始前检查 diytomcat 是否已经启动
        if (NetUtil.isUsableLocalPort(port)) { // 检查端口是否可用，也会访问服务器
            // 标准错误输出流
            System.err.println("请先启动位于端口：" + port + " 的diytomcat，否则不会进行单元测试.");
            // 标准输出流
            // System.out.println("请先启动位于端口：" + port + " 的diytomcat，否则不会进行单元测试.");
            // 非0表示非正常退出程序，0表示正常退出程序
            System.exit(1);
        } else {
            System.out.println("检测到 diytomcat 已经启动，下面进行单元测试.");
        }
    }

    @Test
    public void testHelloTomcat() {
        String content = getContentString("/");
        Assert.assertEquals(content, "Hello DIY Tomcat from how2j.cn");
    }

    @Test
    public void testAHtml() {
        String html = getContentString("/a.html");
        Assert.assertEquals(html, "Hello DIY Tomcat from a.html");
    }

    @Test
    public void testTimeConsumeHtml() throws InterruptedException {
        // corePoolSize: 核心线程数(核心线程在 idle 的时候会被 keep alive，不会被回收)
        // maximumPoolSize: 最大线程数

        // keepAliveTime: 线程最大空闲时间
        // 第 3、4 个参数：表示新增加出来的线程(核心线程之外的)如果空闲时间超过 60 秒，
        // 那么就会被回收，最后保留 20 个核心线程

        // new LinkedBlockingQueue<Runnable>(10): 表示当短时间有很多请求过来，
        // 使得 20 个核心线程都满了之后，并不会马上分配新的线程处理更多的请求，
        // 而是把这些请求放在这个 LinkedBlockingQueue 里，当核心线程忙过来了，
        // 就会来处理这个队列里的请求。只有当处理不过来的请求数目超过了 10 个之后，
        // 才会增加更多的线程来处理。
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(20,
                20, 60, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(10));

        TimeInterval timeInterval = DateUtil.timer();

        for (int i = 0; i < 3; i++) {
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    getContentString("/timeConsume.html");
                }
            });
        }

        // 关闭线程池

        // 并不会立即关闭，停止接收外部 submit 的任务，内部正在跑的任务和队列里等待的任务
        // 会执行完，之后才会停止
        threadPool.shutdown();
        // 当前线程阻塞，直到所有已提交的任务执行完，或者等超时时间到，或者线程被中断
        // 抛出 InterruptedException，然后返回 true(shutdown 请求后所有任务执行完毕)
        // 或 false(已超时)
        threadPool.awaitTermination(1, TimeUnit.HOURS);

        long duration = timeInterval.intervalMs();
        System.out.println("duration: " + duration);
        Assert.assertTrue(duration < 3000);
    }

    private String getContentString(String uri) {
        String url = StrUtil.format("http://{}:{}{}", ip, port, uri);
        String content = MiniBrowser.getContentString(url);
        return content;
    }
}
