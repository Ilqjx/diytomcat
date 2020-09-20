package cn.ilqjx.diytomcat.util;

import cn.hutool.core.io.FileUtil;
import cn.ilqjx.diytomcat.catalina.Context;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * @author upfly
 * @create 2020-09-19 19:54
 */
public class ServerXMLUtil {

    public static List<Context> getContexts() {
        List<Context> result = new ArrayList<>();
        String xml = FileUtil.readUtf8String(Constant.SERVER_XML_FILE);
        Document document = Jsoup.parse(xml);

        Elements elements = document.select("Context");
        for (Element element : elements) {
            String path = element.attr("path");
            String docBase = element.attr("docBase");
            Context context = new Context(path, docBase);
            result.add(context);
        }

        return result;
    }
}
