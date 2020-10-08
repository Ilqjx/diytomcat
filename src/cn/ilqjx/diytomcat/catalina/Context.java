package cn.ilqjx.diytomcat.catalina;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.log.LogFactory;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.util.Map;
import java.util.Set;

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

    private Map<String, String> url_servletClassName; // 地址对应 Servlet 的类名
    private Map<String, String> url_servletName; // 地址对应 Servlet 的名称
    private Map<String, String> servletName_className; // Servlet 名称对应类名
    private Map<String, String> className_servletName; // Servlet 类名对应名称

    public Context(String path, String docBase) {
        TimeInterval timeInterval = DateUtil.timer();
        this.path = path;
        this.docBase = docBase;
        LogFactory.get().info("Deploying web application directory {}", this.docBase);
        LogFactory.get().info("Deployment of web application directory {} has finished in {} ms",
                this.docBase, timeInterval.intervalMs());

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
}
