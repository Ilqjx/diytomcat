package cn.ilqjx.diytomcat;

import cn.hutool.core.util.NetUtil;
import cn.ilqjx.diytomcat.http.Request;
import cn.ilqjx.diytomcat.util.MiniBrowser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

                // 打开输出流，准备给客户端输出信息
                OutputStream os = s.getOutputStream();
                // 响应头，web服务器和浏览器之间通信需要遵循http协议，所以需要加一个头信息
                String response_head = "HTTP/1.1 200 OK\r\n" + "Content-Type: text/html\r\n\r\n";
                String responseString = "Hello DIY Tomcat from how2j.cn";
                responseString = response_head + responseString;
                os.write(responseString.getBytes());
                // 强制把缓存中的数据写出
                os.flush();
                s.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
