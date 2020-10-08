package cn.ilqjx.diytomcat.util;

import cn.hutool.core.io.FileUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * @author upfly
 * @create 2020-10-08 19:16
 */
public class ContextXMLUtil {

    public static String getWatchedResource() {
        try {
            String xml = FileUtil.readUtf8String(Constant.CONTEXT_XML_FILE);
            Document document = Jsoup.parse(xml);
            Element element = document.select("WatchedResource").first();
            return element.text();
        } catch (Exception e) {
            e.printStackTrace();
            return "WEB-INF/web.xml";
        }
    }
}
