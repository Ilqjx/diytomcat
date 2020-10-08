package cn.ilqjx.diytomcat.http;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

/**
 * @author upfly
 * @create 2020-09-12 20:44
 */
public class Response extends BaseResponse {
    // StringWriter: 把数据写入到 String 中去，内部提供 StringBuffer 保存数据
    private StringWriter stringWriter; // 用于存放返回的 html 文本
    private PrintWriter writer; // 打印流
    private String contentType; // 内容类型
    private byte[] body; // 存放二进制文件

    public Response() {
        this.stringWriter = new StringWriter();
        // writer 写进来的数据最后都写到 stringWriter 中了
        // 表面上是 PrintWriter 实际上是 StringWriter，把数据写到字符串中
        this.writer = new PrintWriter(stringWriter);
        this.contentType = "text/html";
    }

    public PrintWriter getWriter() {
        return writer;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentType() {
        return contentType;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public byte[] getBody() throws UnsupportedEncodingException {
        if (body == null) {
            String content = stringWriter.toString();
            body = content.getBytes("utf-8");
        }
        return body;
    }
}
