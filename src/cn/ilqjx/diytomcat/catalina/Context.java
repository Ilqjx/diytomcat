package cn.ilqjx.diytomcat.catalina;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.LogFactory;
import cn.ilqjx.diytomcat.exception.WebConfigDuplicatedException;
import cn.ilqjx.diytomcat.util.ContextXMLUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.util.*;

/**
 * 存放 Servlet 的映射信息
 *
 * @author upfly
 * @create 2020-09-18 19:19
 */
public class Context {
    private String path; // 访问的路径
    private String docBase; // 对应在文件系统中的位置
    private File contextWebXmlFile; // 对应 xxx/WEB-INF/web.xml

    // 不同的 Context 对应不同的 web 应用，每个 Context 下面都有自己的映射关系
    private Map<String, String> url_servletClassName; // 地址对应 Servlet 的类名
    private Map<String, String> url_servletName; // 地址对应 Servlet 的名称
    private Map<String, String> servletName_className; // Servlet 名称对应类名
    private Map<String, String> className_servletName; // Servlet 类名对应名称

    public Context(String path, String docBase) {
        this.path = path;
        this.docBase = docBase;
        this.contextWebXmlFile = new File(docBase, ContextXMLUtil.getWatchedResource());
        this.url_servletClassName = new HashMap<>();
        this.url_servletName = new HashMap<>();
        this.servletName_className = new HashMap<>();
        this.className_servletName = new HashMap<>();

        deploy();
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDocBase() {
        return docBase;
    }

    public void setDocBase(String docBase) {
        this.docBase = docBase;
    }

    public String getServletClassName(String uri) {
        System.out.println("url_servletClassName: " + url_servletClassName);
        return url_servletClassName.get(uri);
    }

    /**
     * 解析 web.xml 中的数据
     *
     * @param document
     */
    private void parseServletMapping(Document document) {
        // 为 url_servletName 填充数据
        Elements urlPatternElements = document.select("servlet-mapping url-pattern");
        for (Element element : urlPatternElements) {
            String urlPattern = element.text();
            String servletName = element.parent().select("servlet-name").first().text();
            url_servletName.put(urlPattern, servletName);
        }

        // 为 className_servletName、servletName_className 填充数据
        Elements servletClassElements = document.select("servlet servlet-class");
        for (Element element : servletClassElements) {
            String servletClass = element.text();
            String servletName = element.parent().select("servlet-name").first().text();
            className_servletName.put(servletClass, servletName);
            servletName_className.put(servletName, servletClass);
        }

        // 为 url_servletClassName 填充数据
        Set<String> urlPatterns = url_servletName.keySet();
        for (String urlPattern : urlPatterns) {
            String servletName = url_servletName.get(urlPattern);
            String servletClassName = servletName_className.get(servletName);
            url_servletClassName.put(urlPattern, servletClassName);
        }
    }

    /**
     * 检查 xml 中相同标签的内容是否有重复的
     *
     * @param document
     * @param mapping xml 标签
     * @param desc 异常信息
     * @throws WebConfigDuplicatedException
     */
    private void checkDuplicated(Document document, String mapping, String desc) throws WebConfigDuplicatedException {
        // 判断逻辑：放在一个集合中，进行排序之后比较相邻元素是否相同
        Elements elements = document.select(mapping);
        List<String> contents = new ArrayList<>();
        for (Element element : elements) {
            contents.add(element.text());
        }
        Collections.sort(contents);
        for (int i = 0; i < contents.size() - 1; i++) {
            String contentPre = contents.get(i);
            String contentNext = contents.get(i + 1);
            if (contentPre.equals(contentNext)) {
                throw new WebConfigDuplicatedException(StrUtil.format(desc, contentPre));
            }
        }
    }

    /**
     * 检查 web.xml 文件中是否有重复的 servlet-name、servlet-class、url-pattern
     *
     * @throws WebConfigDuplicatedException
     */
    private void checkDuplicated() throws WebConfigDuplicatedException {
        String xml = FileUtil.readUtf8String(contextWebXmlFile);
        Document document = Jsoup.parse(xml);
        checkDuplicated(document, "servlet-mapping url-pattern", "servlet url 重复,请保持其唯一性:{}");
        checkDuplicated(document, "servlet servlet-name", "servlet 名称重复,请保持其唯一性:{}");
        checkDuplicated(document, "servlet servlet-class", "servlet 类名重复,请保持其唯一性:{}");
    }

    /**
     * 初始化 url_servletName、className_servletName、servletName_className、
     * url_servletClassName 这几个 HashMap
     */
    private void init() {
        // 判断是否有 web.xml 文件，没有则返回
        if (!contextWebXmlFile.exists()) {
            return;
        }

        try {
            checkDuplicated();
        } catch (WebConfigDuplicatedException e) {
            e.printStackTrace();
            return;
        }

        String xml = FileUtil.readUtf8String(contextWebXmlFile);
        Document document = Jsoup.parse(xml);
        parseServletMapping(document);
    }

    /**
     * 打印日志
     */
    private void deploy() {
        TimeInterval timeInterval = DateUtil.timer();
        LogFactory.get().info("Deploying web application directory {}", this.docBase);
        init();
        LogFactory.get().info("Deployment of web application directory {} has finished in {} ms", this.docBase, timeInterval.intervalMs());
    }
}
