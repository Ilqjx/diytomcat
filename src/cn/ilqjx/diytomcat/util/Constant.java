package cn.ilqjx.diytomcat.util;

import cn.hutool.system.SystemUtil;

import java.io.File;

/**
 * @author upfly
 * @create 2020-09-12 20:39
 */
public class Constant {
    public static final String RESPONSE_HEAD_200 =
            "HTTP/1.1 200 OK\r\n" + "Content-Type: {}\r\n\r\n";

    // SystemUtil.get("user.dir") 获取程序当前路径
    public static final File WEBAPPS_FOLDER =
            new File(SystemUtil.get("user.dir"), "webapps");
    public static final File ROOT_FOLDER = new File(WEBAPPS_FOLDER, "ROOT");

    public static final File CONF_FOLDER = new File(SystemUtil.get("user.dir"), "conf");
    public static final File SERVER_XML_FILE = new File(CONF_FOLDER, "server.xml");
}
