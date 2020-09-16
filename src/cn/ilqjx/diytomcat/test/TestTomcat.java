package cn.ilqjx.diytomcat.test;

import cn.hutool.core.util.NetUtil;
import cn.hutool.core.util.StrUtil;
import cn.ilqjx.diytomcat.util.MiniBrowser;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.concurrent.Executors;
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
            // 错误输出
            System.err.println("请先启动位于端口：" + port + " 的diytomcat，否则不会进行单元测试.");
            // 标准输出
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
    public void testTimeConsumeHtml() {
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(20,
                20, 60, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(10));


    }

    private String getContentString(String uri) {
        String url = StrUtil.format("http://{}:{}{}", ip, port, uri);
        String content = MiniBrowser.getContentString(url);
        return content;
    }
}
