package cn.ilqjx.diytomcat.util;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.FileUtil;
import cn.ilqjx.diytomcat.catalina.*;
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

    public static String getHostName() {
        String xml = FileUtil.readUtf8String(Constant.SERVER_XML_FILE);
        Document document = Jsoup.parse(xml);
        Element element = document.select("Host").first();
        String hostName = element.attr("name");
        return hostName;
    }

    public static String getEngineDefaultHost() {
        String xml = FileUtil.readUtf8String(Constant.SERVER_XML_FILE);
        Document document = Jsoup.parse(xml);
        Element element = document.select("Engine").first();
        String defaultHost = element.attr("defaultHost");
        return defaultHost;
    }

    public static List<Host> getHosts(Engine engine) {
        List<Host> result = new ArrayList<>();
        String xml = FileUtil.readUtf8String(Constant.SERVER_XML_FILE);
        Document document = Jsoup.parse(xml);
        Elements elements = document.select("Host");

        for (Element element : elements) {
            String name = element.attr("name");
            Host host = new Host(name, engine);
            result.add(host);
        }

        return result;
    }

    public static String getServiceName() {
        String xml = FileUtil.readUtf8String(Constant.SERVER_XML_FILE);
        Document document = Jsoup.parse(xml);
        Element element = document.select("Service").first();
        String serviceName = element.attr("name");
        return serviceName;
    }

    public static List<Connector> getConnectors(Service service) {
        List<Connector> connectors = new ArrayList<>();
        String xml = FileUtil.readUtf8String(Constant.SERVER_XML_FILE);
        Document document = Jsoup.parse(xml);
        Elements elements = document.select("Connector");
        for (Element element : elements) {
            int port = Convert.toInt(element.attr("port"));
            Connector connector = new Connector(service);
            connector.setPort(port);
            connectors.add(connector);
        }
        return connectors;
    }
}
