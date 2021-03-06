package cn.ilqjx.diytomcat.util;

import cn.hutool.http.HttpUtil;

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

    /**
     * 返回字符串的 http 响应内容
     *
     * @param url
     * @return
     */
    public static String getContentString(String url) {
        return getContentString(url, false, null, true);
    }

    /**
     * 返回字符串的 http 响应内容
     *
     * @param url
     * @param gzip
     * @return
     */
    public static String getContentString(String url, boolean gzip) {
        return getContentString(url, gzip, null, true);
    }

    /**
     * 返回字符串的 http 响应内容
     *
     * @param url
     * @param params
     * @param isGet
     * @return
     */
    public static String getContentString(String url, Map<String, Object> params, boolean isGet) {
        return getContentString(url, false, params, isGet);
    }

    /**
     * 返回字符串的 http 响应内容
     *
     * @param url
     * @param gzip
     * @return
     */
    public static String getContentString(String url, boolean gzip, Map<String, Object> params, boolean isGet) {
        byte[] result = getContentBytes(url, gzip, params, isGet);
        if (result == null) {
            return null;
        }
        try {
            return new String(result, "utf-8").trim();
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     * 返回二进制的 http 响应内容
     *
     * @param url
     * @return
     */
    public static byte[] getContentBytes(String url) {
        return getContentBytes(url, false, null, true);
    }

    /**
     * 返回二进制的 http 响应内容
     *
     * @param url
     * @param gzip
     * @return
     */
    public static byte[] getContentBytes(String url, boolean gzip) {
        return getContentBytes(url, gzip, null, true);
    }

    /**
     * 返回二进制的 http 响应内容
     *
     * @param url
     * @param params
     * @param isGet
     * @return
     */
    public static byte[] getContentBytes(String url, Map<String,Object> params, boolean isGet) {
        return getContentBytes(url, false, params, isGet);
    }

    /**
     * 返回二进制的 http 响应内容
     *
     * @param url
     * @param gzip
     * @return
     */
    public static byte[] getContentBytes(String url, boolean gzip, Map<String, Object> params, boolean isGet) {
        byte[] response = getHttpBytes(url, gzip, params, isGet);
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

    /**
     * 返回字符串的 http 响应
     *
     * @param url
     * @return
     */
    public static String getHttpString(String url) {
        return getHttpString(url, false, null, true);
    }

    /**
     * 返回字符串的 http 响应
     *
     * @param url
     * @param gzip
     * @return
     */
    public static String getHttpString(String url, boolean gzip) {
        return getHttpString(url, gzip, null, true);
    }

    /**
     * 返回字符串的 http 响应
     *
     * @param url
     * @param params
     * @param isGet
     * @return
     */
    public static String getHttpString(String url, Map<String,Object> params, boolean isGet) {
        return getHttpString(url, false, params, isGet);
    }

    /**
     * 返回字符串的 http 响应
     *
     * @param url
     * @param gzip
     * @param params
     * @param isGet
     * @return
     */
    public static String getHttpString(String url, boolean gzip, Map<String,Object> params, boolean isGet) {
        byte[] bytes = getHttpBytes(url, gzip, params, isGet);
        return new String(bytes).trim();
    }

    /**
     * 返回二进制的 http 响应
     *
     * @param url 请求的资源路径
     * @param gzip 是否获取压缩后的数据
     * @param params 请求参数
     * @param isGet 是否是 GET 请求
     * @return
     */
    public static byte[] getHttpBytes(String url, boolean gzip, Map<String, Object> params, boolean isGet) {
        String method = isGet ? "GET" : "POST";
        byte[] result = null;
        try {
            URL u = new URL(url);
            Socket client = new Socket();
            int port = u.getPort();
            // -1 是什么鬼？？？
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

            String paramsString = "";
            if (params != null) {
                paramsString = HttpUtil.toParams(params);
            }

            if (isGet) {
                path = path + "?" + paramsString;
            }

            String firstLine = method + " " + path + " HTTP/1.1\r\n";

            StringBuffer httpRequestString = new StringBuffer();
            httpRequestString.append(firstLine);
            Set<String> headers = requestHeaders.keySet();
            for (String header : headers) {
                String headerLine = header + ":" + requestHeaders.get(header) + "\r\n";
                httpRequestString.append(headerLine);
            }

            // POST 请求的参数放在请求体中
            if (!isGet) {
                httpRequestString.append("\r\n");
                httpRequestString.append(paramsString);
            }

            // autoFlush: true --> 刷新输出缓存区
            PrintWriter printWriter = new PrintWriter(client.getOutputStream(), true);
            // 发送信息到服务器
            printWriter.println(httpRequestString);
            // 接收服务器发送的信息
            InputStream is = client.getInputStream();
            // 将 is 接收到的信息转换为字节数组
            result = readBytes(is, true);
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

    /**
     * 将输入流中的数据读取到字节数组中
     *
     * @param is
     * @param fully 表示是否完全读取
     * @return
     * @throws IOException
     */
    public static byte[] readBytes(InputStream is, boolean fully) throws IOException {
        int buffer_size = 1024;
        byte[] buffer = new byte[buffer_size];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        while (true) {
            int length = is.read(buffer);
            if (length == -1) {
                break;
            }
            // 写出到 buffer 数组中
            baos.write(buffer, 0, length);
            // 在传输过程中，可能就不会一次传输 1024 个字节，有时候会小于这个字节数
            // 有时候发送方不会发送完全，而是一点一点的发
            if (!fully && length != buffer_size) {
                break;
            }
        }
        byte[] result = baos.toByteArray();
        return result;
    }
}
