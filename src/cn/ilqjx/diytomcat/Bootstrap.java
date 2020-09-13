package cn.ilqjx.diytomcat;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.NetUtil;
import cn.hutool.core.util.StrUtil;
import cn.ilqjx.diytomcat.http.Request;
import cn.ilqjx.diytomcat.http.Response;
import cn.ilqjx.diytomcat.util.Constant;
import cn.ilqjx.diytomcat.util.MiniBrowser;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author upfly
 * @create 2020-09-06 20:42
 */
public class Bootstrap {

    public static void main(String[] args) {
        try {
            int port = 18080;

            // 判断端口是否被占用，没被占用返回 true
            if (!NetUtil.isUsableLocalPort(port)) {
                System.out.println(port + "端口已被占用");
                return;
            }
            // 服务端打开 port 端口
            ServerSocket ss = new ServerSocket(port);

            while (true) {
                // 监听 port 端口，看是否有连接请求过来
                Socket s =  ss.accept();
                // 表示收到一个浏览器客户端的请求
                Request request = new Request(s);
                System.out.println("浏览器的输入信息：\r\n" + request.getRequestString());
                System.out.println("uri：" + request.getUri());

                Response response = new Response();
                String html = "Hello DIY Tomcat from how2j.cn";
                response.getWriter().println(html);
                handle200(s, response);

                // 打开输出流，准备给客户端输出信息
                // OutputStream os = s.getOutputStream();
                // 响应头，web服务器和浏览器之间通信需要遵循http协议，所以需要加一个头信息
                // String response_head = "HTTP/1.1 200 OK\r\n" + "Content-Type: text/html\r\n\r\n";
                // String responseString = "Hello DIY Tomcat from how2j.cn";
                // responseString = response_head + responseString;
                // os.write(responseString.getBytes());
                // 强制把缓存中的数据写出
                // os.flush();
                // s.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handle200(Socket s, Response response) throws IOException {
        String contentType = response.getContentType();
        String headText = StrUtil.format(Constant.RESPONSE_HEAD_200, contentType);

        byte[] head = headText.getBytes();
        byte[] body = response.getBody();
        byte[] responseBytes = new byte[head.length + body.length];

        ArrayUtil.copy(head, 0, responseBytes, 0, head.length);
        ArrayUtil.copy(body, 0, responseBytes, head.length, body.length);

        OutputStream os = s.getOutputStream();
        os.write(responseBytes);
        s.close();
    }
}
