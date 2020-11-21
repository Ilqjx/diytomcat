package cn.ilqjx.diytomcat.http;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;

import javax.servlet.http.Cookie;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
    private int status; // 响应的状态码
    private List<Cookie> cookies;

    public Response() {
        this.stringWriter = new StringWriter();
        // writer 写进来的数据最后都写到 stringWriter 中了
        // 表面上是 PrintWriter 实际上是 StringWriter，把数据写到字符串中
        this.writer = new PrintWriter(stringWriter);
        this.contentType = "text/html";
        this.cookies = new ArrayList<>();
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

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public void setStatus(int status) {
        this.status = status;
    }

    public List<Cookie> getCookies() {
        return cookies;
    }

    @Override
    public void addCookie(Cookie cookie) {
        cookies.add(cookie);
    }

    /**
     * 设置 cookie 头信息
     *
     * @return
     */
    public String getCookiesHeader() {
        if (cookies == null) {
            return "";
        }

        // EEE: 代表星期（比如：Sun） MMM: 代表月（比如：Sep）
        String pattern = "EEE, d MMM yyyy HH:mm:ss 'GMT'";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.ENGLISH);

        StringBuffer buffer = new StringBuffer();
        for (Cookie cookie : cookies) {
            // 添加完一条头信息换行
            buffer.append("\r\n");
            buffer.append("Set-Cookie: ");
            buffer.append(cookie.getName() + "=" + cookie.getValue() + ";");

            // 添加 Expires 头信息，表示有效时间
            if (cookie.getMaxAge() != -1) {
                buffer.append("Expires=");
                Date now = new Date();
                Date expire = DateUtil.offset(now, DateField.SECOND, cookie.getMaxAge());
                buffer.append(sdf.format(expire));
                buffer.append(";");
            }

            if (cookie.getPath() != null) {
                buffer.append("Path=" + cookie.getPath());
            }
        }

        return buffer.toString();
    }
}
