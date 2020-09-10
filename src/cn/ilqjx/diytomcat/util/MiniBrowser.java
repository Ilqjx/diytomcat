package cn.ilqjx.diytomcat.util;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author upfly
 * @create 2020-09-08 20:39
 */
public class MiniBrowser {

    public static String getContentString(String url) {
        return getContentString(url, false);
    }

    /**
     * 返回字符串的 http 响应内容
     *
     * @param url
     * @param gzip
     * @return
     */
    public static String getContentString(String url, boolean gzip) {
        byte[] result = getContentBytes(url, gzip);
        if (result == null) {
            return null;
        }
        try {
            return new String(result, "utf-8").trim();
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static byte[] getContentBytes(String url) {
        return getContentBytes(url, false);
    }

    /**
     * 返回二进制的 http 响应内容
     *
     * @param url
     * @param gzip
     * @return
     */
    public static byte[] getContentBytes(String url, boolean gzip) {
        byte[] response = getHttpBytes(url, gzip);
        byte[] doubleReturn = "\r\n\r\n".getBytes();

        int pos = -1;
        for (int i = 0; i < response.length - doubleReturn.length; i++) {
            byte[] temp = Arrays.copyOfRange(response, i, i + doubleReturn.length);
            // 头信息与响应内容之间有两个换行
            if (Arrays.equals(temp, doubleReturn)) {
                // 头信息最后一个字节的位置
                pos = i;
                break;
            }
        }
        // 没有响应内容
        if (pos == -1) {
            return null;
        }
        // 响应内容开始的位置
        pos += doubleReturn.length;

        byte[] result = Arrays.copyOfRange(response, pos, response.length);
        return result;
    }

    public static String getHttpString(String url) {
        return getHttpString(url, false);
    }

    /**
     * 返回字符串的 http 响应
     *
     * @param url
     * @return
     */
    public static String getHttpString(String url, boolean gzip) {
        byte[] httpBytes = getHttpBytes(url, gzip);
        return new String(httpBytes).trim();
    }

    /**
     * 返回二进制的 http 响应
     *
     * @param url
     * @param gzip 是否获取压缩后的数据
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

            // 浏览器发送给服务器的信息
            Map<String, String> requestHeaders = new HashMap<>();
            requestHeaders.put("Host", u.getHost() + ":" + port);
            requestHeaders.put("Accept", "text/html");
            requestHeaders.put("Connection", "close");
            requestHeaders.put("User-Agent", "how2j mini browser / java1.8");

            // 获取的是否是压缩后的数据
            if (gzip) {
                requestHeaders.put("Accept-Encoding", "gzip");
            }

            // 获取此 url 的路径部分
            String path = u.getPath();
            if (path.length() == 0) {
                path = "/";
            }

            String firstLine = "GET " + path + " HTTP/1.1\r\n";

            StringBuffer httpRequestString = new StringBuffer();
            httpRequestString.append(firstLine);
            Set<String> headers = requestHeaders.keySet();
            for (String header : headers) {
                String headerLine = header + ":" + requestHeaders.get(header) + "\r\n";
                httpRequestString.append(headerLine);
            }

            // autoFlush: true --> 刷新输出缓存区
            PrintWriter printWriter = new PrintWriter(client.getOutputStream(), true);
            // 发送信息到服务器
            printWriter.println(httpRequestString);
            // 接受服务器发送的信息
            InputStream is = client.getInputStream();

            // 将 is 接收到的信息转换为字节数组
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
        return result;
    }
}
