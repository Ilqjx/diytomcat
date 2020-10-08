package cn.ilqjx.diytomcat.catalina;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.LogFactory;
import cn.ilqjx.diytomcat.http.Request;
import cn.ilqjx.diytomcat.http.Response;
import cn.ilqjx.diytomcat.util.Constant;
import cn.ilqjx.diytomcat.webappservlet.HelloServlet;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @author upfly
 * @create 2020-10-08 10:18
 */
public class HttpProcessor {

    public void execute(Socket socket, Request request, Response response) {
        try {
            String uri = request.getUri();
            if (uri == null) {
                return;
            }

            Context context = request.getContext();

            if ("/500.html".equals(uri)) {
                throw new RuntimeException("this is a deliberately created exception");
            }

            if ("/hello".equals(uri)) {
                HelloServlet helloServlet = new HelloServlet();
                helloServlet.doGet(request, response);
            }

            // System.out.println("浏览器的输入信息：\r\n" + request.getRequestString());
            // System.out.println("uri：" + request.getUri());

            /*
            if ("/".equals(uri)) {
                // 获取欢迎文件名
                uri = WebXMLUtil.getWelcomeFile(context);
            }

            String fileName = StrUtil.removePrefix(uri, "/");
            File file = new File(context.getDocBase(), fileName);

            if (file.exists()) {
                // 获取文件后缀名，设置 content-type
                String extName = FileUtil.extName(file);
                String mimeType = WebXMLUtil.getMimeType(extName);
                response.setContentType(mimeType);

                byte[] body = FileUtil.readBytes(file);
                response.setBody(body);

                // FileUtil.readUtf8String(file) 直接把文件的内容读出来赋给 fileContent
                // String fileContent = FileUtil.readUtf8String(file);
                // response.getWriter().println(fileContent);

                if ("timeConsume.html".equals(fileName)) {
                    ThreadUtil.sleep(1000);
                }
            } else {
                handle404(socket, uri);
                return;
            }
            */
            handle200(socket, response);
        } catch (Exception e) {
            LogFactory.get().error(e);
            // 当服务器内部抛出异常返回 500 响应
            handle500(socket, e);
        } finally {
            try {
                if (!socket.isClosed()) {
                    socket.close();
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
