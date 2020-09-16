package cn.ilqjx.diytomcat;

import cn.hutool.bloomfilter.filter.SDBMFilter;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.NetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.LogFactory;
import cn.hutool.system.SystemUtil;
import cn.ilqjx.diytomcat.http.Request;
import cn.ilqjx.diytomcat.http.Response;
import cn.ilqjx.diytomcat.util.Constant;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author upfly
 * @create 2020-09-06 20:42
 */
public class Bootstrap {

    public static void main(String[] args) {
        try {
            logJVM();

            int port = 18080;

            // 判断端口是否被占用，没被占用返回 true
            // if (!NetUtil.isUsableLocalPort(port)) {
            //     System.out.println(port + "端口已被占用");
            //     return;
            // }
            // 服务端打开 port 端口
            ServerSocket ss = new ServerSocket(port);

            while (true) {
                // 监听 port 端口，看是否有连接请求过来
                Socket s =  ss.accept();
                // 表示收到一个浏览器客户端的请求
                Request request = new Request(s);
                System.out.println("浏览器的输入信息：\r\n" + request.getRequestString());
                System.out.println("uri：" + request.getUri());

                String uri = request.getUri();
                if (uri == null) {
                    continue;
                }
                System.out.println(uri);

                Response response = new Response();
                if ("/".equals(uri)) {
                    String html = "Hello DIY Tomcat from how2j.cn";
                    response.getWriter().println(html);
                } else {
                    String fileName = StrUtil.removePrefix(uri, "/");
                    File file = FileUtil.file(Constant.ROOT_FOLDER, fileName);
                    if (file.exists()) {
                        String fileContent = FileUtil.readUtf8String(file);
                        response.getWriter().println(fileContent);
                    } else {
                        response.getWriter().println("File Not Found");
                    }
                }

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
            LogFactory.get().error(e);
            e.printStackTrace();
        }
    }

    private static void logJVM() {
        Map<String, String> infos = new LinkedHashMap<>();
        infos.put("Server version", "How2j DiyTomcat/1.0.1");
        infos.put("Server built", "2020-09-16 19:35:28");
        infos.put("Server number", "1.0.1");
        infos.put("OS Name\t", SystemUtil.get("os.name"));
        infos.put("OS Version", SystemUtil.get("os.version"));
        // os.arch: 操作系统的架构
        infos.put("Architecture", SystemUtil.get("os.arch"));
        // java.home: Java 安装目录
        infos.put("Java Home", SystemUtil.get("java.home"));
        infos.put("JVM Version", SystemUtil.get("java.runtime.version"));
        // java.vm.specification.vendor: Java 虚拟机规范供应商
        infos.put("JVM Vendor", SystemUtil.get("java.vm.specification.vendor"));

        Set<String> keys = infos.keySet();
        for (String key : keys) {
            LogFactory.get().info(key + ":\t\t" + infos.get(key));
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
