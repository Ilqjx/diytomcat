package cn.ilqjx.diytomcat.util;

import cn.hutool.system.SystemUtil;

import java.io.File;

/**
 * @author upfly
 * @create 2020-09-12 20:39
 */
public class Constant {
    public static final int CODE_200 = 200; // http 返回代码常量
    public static final int CODE_302 = 302;
    public static final int CODE_404 = 404;
    public static final int CODE_500 = 500;

    public static final String RESPONSE_HEAD_200 =
            "HTTP/1.1 200 OK\r\n" + "Content-Type: {}{}\r\n\r\n"; // 200 头信息
    public static final String RESPONSE_HEAD_404 =
            "HTTP/1.1 404 Not Found\r\n" + "Content-Type: text/html\r\n\r\n"; // 404 头信息
    public static final String RESPONSE_HEAD_500 =
            "HTTP/1.1 500 Internal Server Error\r\n" + "Content-type: text/html\r\n\r\n"; // 500 头信息

    // SystemUtil.get("user.dir") 获取程序当前路径
    public static final File WEBAPPS_FOLDER =
            new File(SystemUtil.get("user.dir"), "webapps");
    public static final File ROOT_FOLDER = new File(WEBAPPS_FOLDER, "ROOT");

    public static final File CONF_FOLDER = new File(SystemUtil.get("user.dir"), "conf");
    public static final File SERVER_XML_FILE = new File(CONF_FOLDER, "server.xml");

    public static final File WEB_XML_FILE = new File(CONF_FOLDER, "web.xml");
    public static final File CONTEXT_XML_FILE = new File(CONF_FOLDER, "context.xml");

    // 404 文本响应
    public static final String TEXT_FORMAT_404 =
            "<html><head><title>DIY Tomcat/1.0.1 - Error report</title><style>" +
            "<!--H1 {font-family:Tahoma,Arial,sans-serif;color:white;background-color:#525D76;font-size:22px;} " +
            "H2 {font-family:Tahoma,Arial,sans-serif;color:white;background-color:#525D76;font-size:16px;} " +
            "H3 {font-family:Tahoma,Arial,sans-serif;color:white;background-color:#525D76;font-size:14px;} " +
            "BODY {font-family:Tahoma,Arial,sans-serif;color:black;background-color:white;} " +
            "B {font-family:Tahoma,Arial,sans-serif;color:white;background-color:#525D76;} " +
            "P {font-family:Tahoma,Arial,sans-serif;background:white;color:black;font-size:12px;}" +
            "A {color : black;}A.name {color : black;}HR {color : #525D76;}--></style> " +
            "</head><body><h1>HTTP Status 404 - {}</h1>" +
            "<HR size='1' noshade='noshade'><p><b>type</b> Status report</p><p><b>message</b> <u>{}</u></p><p><b>description</b> " +
            "<u>The requested resource is not available.</u></p><HR size='1' noshade='noshade'><h3>DiyTocmat 1.0.1</h3>" +
            "</body></html>";

    // 500 文本响应
    public static final String TEXT_FORMAT_500 = "<html><head><title>DIY Tomcat/1.0.1 - Error report</title><style>"
            + "<!--H1 {font-family:Tahoma,Arial,sans-serif;color:white;background-color:#525D76;font-size:22px;} "
            + "H2 {font-family:Tahoma,Arial,sans-serif;color:white;background-color:#525D76;font-size:16px;} "
            + "H3 {font-family:Tahoma,Arial,sans-serif;color:white;background-color:#525D76;font-size:14px;} "
            + "BODY {font-family:Tahoma,Arial,sans-serif;color:black;background-color:white;} "
            + "B {font-family:Tahoma,Arial,sans-serif;color:white;background-color:#525D76;} "
            + "P {font-family:Tahoma,Arial,sans-serif;background:white;color:black;font-size:12px;}"
            + "A {color : black;}A.name {color : black;}HR {color : #525D76;}--></style> "
            + "</head><body><h1>HTTP Status 500 - An exception occurred processing {}</h1>"
            + "<HR size='1' noshade='noshade'><p><b>type</b> Exception report</p><p><b>message</b> <u>An exception occurred processing {}</u></p><p><b>description</b> "
            + "<u>The server encountered an internal error that prevented it from fulfilling this request.</u></p>"
            + "<p>Stacktrace:</p>" + "<pre>{}</pre>" + "<HR size='1' noshade='noshade'><h3>DiyTocmat 1.0.1</h3>"
            + "</body></html>";
}
