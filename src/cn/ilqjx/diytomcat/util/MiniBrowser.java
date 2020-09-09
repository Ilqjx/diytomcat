package cn.ilqjx.diytomcat.util;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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

    /**
     * 返回二进制的http响应
     *
     * @param url
     * @param gzip 可以获取压缩后的数据
     * @return
     */
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

            Map<String, String> requestHeaders = new HashMap<>();
            requestHeaders.put("Host", u.getHost() + ":" + port);
            requestHeaders.put("Accept", "text/html");
            requestHeaders.put("Connection", "close");
            requestHeaders.put("User-Agent", "how2j mini browser / java1.8");

            if (gzip) {
                requestHeaders.put("Accept-Encoding", "gzip");
            }

            // 获取此url的路径部分
            String path = u.getPath();
            if (path.length() == 0) {
                path = "/";
            }

            String firstLine = "GET" + path + "HTTP/1.1\r\n";

            StringBuffer httpRequestString = new StringBuffer();
            httpRequestString.append(firstLine);
            Set<String> headers = requestHeaders.keySet();
            for (String header : headers) {
                String headerLine = header + ":" + requestHeaders.get(header) + "\r\n";
                httpRequestString.append(headerLine);
            }

            // autoFlush: true --> 刷新输出缓存区
            PrintWriter printWriter = new PrintWriter(client.getOutputStream(), true);
            // 输出到服务器？？？
            printWriter.println(httpRequestString);
            InputStream is = client.getInputStream();

            int buffer_size = 1024;

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[buffer_size];
            while (true) {
                int length = is.read(buffer);
                if (length == -1) {
                    break;
                }
                baos.write(buffer, 0, length);
                if (length != buffer_size) {
                    break;
                }
            }

            result = baos.toByteArray();
            client.close();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                result = e.toString().getBytes("utf-8");
            } catch (UnsupportedEncodingException ex) {
                ex.printStackTrace();
            }
        }
    }
}
