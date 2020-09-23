package cn.ilqjx.diytomcat.util;

import cn.hutool.core.io.FileUtil;
import cn.ilqjx.diytomcat.catalina.Context;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;

/**
 * 用来获取某个 Context 下的欢迎文件名称
 *
 * @author upfly
 * @create 2020-09-23 20:14
 */
public class WebXMLUtil {

    /**
     * 获取欢迎文件名
     *
     * @param context
     * @return
     */
    public static String getWelcomeFile(Context context) {
        String xml = FileUtil.readUtf8String(Constant.WEB_XML_FILE);
        Document document = Jsoup.parse(xml);
        Elements elements = document.select("welcome-file");
        for (Element element : elements) {
            String welcomeFileName = element.text();
            File file = new File(context.getDocBase(), welcomeFileName);
            if (file.exists()) {
                return file.getName();
            }
        }
        // 如果都找不到，默认返回 index.html
        return "index.html";
    }
}
