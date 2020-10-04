package cn.ilqjx.diytomcat.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.ilqjx.diytomcat.catalina.Context;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 用来获取某个 Context 下的欢迎文件名称
 *
 * @author upfly
 * @create 2020-09-23 20:14
 */
public class WebXMLUtil {
    private static Map<String, String> mimeTypeMapping = new HashMap<>(); // 文件后缀名到 mime-type 的一个映射

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

    /**
     * 初始化 mimeTypeMapping
     */
    private static void initMimeType() {
        String xml = FileUtil.readUtf8String(Constant.WEB_XML_FILE);
        Document document = Jsoup.parse(xml);
        Elements elements = document.select("mime-mapping");
        for (Element element : elements) {
            Element extension = element.select("extension").first();
            Element mimeType = element.select("mime-type").first();
            mimeTypeMapping.put(extension.text(), mimeType.text());
        }
    }

    /**
     * 获取后缀名对应的 mime-type
     *
     * @param extName 后缀名
     * @return 后缀名对应的 mime-type
     */
    public static synchronized String getMimeType(String extName) {
        if (mimeTypeMapping.isEmpty()) {
            initMimeType();
        }
        String mimeType = mimeTypeMapping.get(extName);
        if (mimeType == null) {
            // 如果找不到后缀名对应的 mime-type，则返回 text/html
            return "text/html";
        }
        return mimeType;
    }
}
