package cn.ilqjx.diytomcat.catalina;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.LogFactory;
import cn.hutool.system.SystemUtil;
import cn.ilqjx.diytomcat.http.Request;
import cn.ilqjx.diytomcat.http.Response;
import cn.ilqjx.diytomcat.util.Constant;
import cn.ilqjx.diytomcat.util.ThreadPoolUtil;
import cn.ilqjx.diytomcat.util.WebXMLUtil;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author upfly
 * @create 2020-09-22 19:30
 */
public class Server {
    private Service service;

    public Server() {
        this.service = new Service(this);
    }

    public void start() {
        logJVM();
        init();
    }

    public void init() {
        try {
            int port = 18080;

            // 注释掉了，为了和 tomcat 保持一致
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
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            // 表示收到一个浏览器客户端的请求
                            Request request = new Request(s, service);
                            Response response = new Response();

                            String uri = request.getUri();
                            if (uri == null) {
                                return;
                            }

                            if ("/500.html".equals(uri)) {
                                throw new RuntimeException("this is a deliberately created exception");
                            }

                            // System.out.println("浏览器的输入信息：\r\n" + request.getRequestString());
                            // System.out.println("uri：" + request.getUri());

                            Context context = request.getContext();

                            if ("/".equals(uri)) {
                                // 获取欢迎文件名
                                uri = WebXMLUtil.getWelcomeFile(context);
                            }

                            String fileName = StrUtil.removePrefix(uri, "/");
                            File file = new File(context.getDocBase(), fileName);
                            if (file.exists()) {
                                // FileUtil.readUtf8String(file) 直接把文件的内容读出来赋给 fileContent
                                String fileContent = FileUtil.readUtf8String(file);
                                response.getWriter().println(fileContent);

                                if ("timeConsume.html".equals(fileName)) {
                                    ThreadUtil.sleep(1000);
                                }
                            } else {
                                handle404(s, uri);
                                return;
                            }

                            handle200(s, response);
                        } catch (Exception e) {
                            LogFactory.get().error(e);
                            // 当服务器内部抛出异常返回 500 响应
                            handle500(s, e);
                        } finally {
                            try {
                                if (!s.isClosed()) {
                                    s.close();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

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
                };

                ThreadPoolUtil.run(runnable);
            }
        } catch (IOException e) {
            LogFactory.get().error(e);
            e.printStackTrace();
        }
    }

    /**
     * 打印 jvm 信息并输出到日志中
     */
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

    /**
     * 返回 200 响应
     *
     * @param s
     * @param response
     * @throws IOException
     */
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
    }

    /**
     * 返回 404 响应
     *
     * @param socket
     * @param uri
     * @throws IOException
     */
    protected void handle404(Socket socket, String uri) throws IOException {
        OutputStream os = socket.getOutputStream();
        String responseText = StrUtil.format(Constant.TEXT_FORMAT_404, uri, uri);
        responseText = Constant.RESPONSE_HEAD_404 + responseText;
        byte[] responseBytes = responseText.getBytes("utf-8");
        os.write(responseBytes);
    }

    /**
     * 返回 500 响应
     *
     * @param socket
     * @param e
     */
    protected void handle500(Socket socket, Exception e) {
        try {
            OutputStream os = socket.getOutputStream();

            // e.getStackTrace(): 获取 Exception 的异常堆栈
            StackTraceElement[] stackTrace = e.getStackTrace();
            StringBuffer buffer = new StringBuffer();
            buffer.append(e.toString());
            buffer.append("\r\n");
            for (StackTraceElement element : stackTrace) {
                buffer.append("\t");
                buffer.append(element.toString());
                buffer.append("\r\n");
            }

            // 获取异常信息
            String message = e.getMessage();
            // if (message != null && message.length() > 20) {
            //     // 截取部分信息
            //     message = message.substring(0, 19);
            // }

            String responseText = StrUtil.format(Constant.TEXT_FORMAT_500, message, e.toString(), buffer.toString());
            responseText = Constant.RESPONSE_HEAD_500 + responseText;
            byte[] responseBytes = responseText.getBytes("utf-8");

            os.write(responseBytes);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
