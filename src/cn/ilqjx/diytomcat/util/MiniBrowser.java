package cn.ilqjx.diytomcat.util;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;

/**
 * @author upfly
 * @create 2020-09-08 20:39
 */
public class MiniBrowser {

    public static void main(String[] args) {
        String url = "http://static.how2j.cn/diytomcat.html";
        String contentString = getContentString(url, false);
    }

    public static String getContentString(String url, boolean gzip) {
        byte[] result = getContentBytes(url, gzip);
    }

    public static byte[] getContentBytes(String url, boolean gzip) {
        byte[] response = getHttpBytes(url, gzip);
    }

    public static byte[] getHttpBytes(String url, boolean gzip) {
        byte[] result = null;
        try {
            URL u = new URL(url);
            Socket client = new Socket();
            int port = u.getPort();
            if (port == -1) {
                port = 80;
            }
            // 实现ip套接字地址(ip + 端口号)
            InetSocketAddress inetSocketAddress = new InetSocketAddress(u.getHost(), port);
            // 将此套接字连接到服务器
            client.connect(inetSocketAddress, 1000);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
