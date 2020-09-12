package cn.ilqjx.diytomcat.http;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author upfly
 * @create 2020-09-12 20:44
 */
public class Response {
    // StringWriter: 把数据写入到 String 中去，内部提供 StringBuffer 保存数据
    private StringWriter stringWriter; // 用于存放返回的 html 文本
    // 输出数据
    private PrintWriter writer;
    private String contentType; // 内容类型

    public Response() {
        this.stringWriter = new StringWriter();
        this.writer = new PrintWriter(stringWriter);
        this.contentType = "text/html";
    }

    public byte[] getBody() {
        String content = stringWriter.toString();

        return null;
    }

    public PrintWriter getWriter() {
        return writer;
    }

    public String getContentType() {
        return contentType;
    }
}
