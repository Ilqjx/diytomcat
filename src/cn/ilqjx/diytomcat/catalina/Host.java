package cn.ilqjx.diytomcat.catalina;

import cn.ilqjx.diytomcat.util.Constant;
import cn.ilqjx.diytomcat.util.ServerXMLUtil;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author upfly
 * @create 2020-09-21 19:34
 */
public class Host {
    private String name;
    private Map<String, Context> contextMap; // 存放路径对 Context 的映射
    private Engine engine;

    public Host(String name, Engine engine) {
        this.contextMap = new HashMap<>();
        this.name = name;
        this.engine = engine;

        scanContextsOnWebAppsFolder();
        scanContextsInServerXML();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 扫描 webapps 文件夹下的目录，对这些目录调用 loadContext(File folder) 进行加载
     */
    private void scanContextsOnWebAppsFolder() {
        File[] folders = Constant.WEBAPPS_FOLDER.listFiles();
        for (File folder : folders) {
            if (!folder.isDirectory()) {
                continue;
            }
            loadContext(folder);
        }
    }

    /**
     * 加载这个目录成为 Context 对象
     * @param folder
     */
    private void loadContext(File folder) {
        String path = folder.getName();
        if ("ROOT".equals(path)) {
            path = "/";
        } else {
            path = "/" + path;
        }

        String docBase = folder.getAbsolutePath();
        Context context = new Context(path, docBase);

        contextMap.put(context.getPath(), context);
    }

    /**
     * 扫描 server.xml文件里的　Context
     */
    private void scanContextsInServerXML() {
        List<Context> contexts = ServerXMLUtil.getContexts();
        for (Context context : contexts) {
            contextMap.put(context.getPath(), context);
        }
    }

    /**
     * 获取 Context 对象
     *
     * @param path
     * @return
     */
    public Context getContext(String path) {
        return contextMap.get(path);
    }
}
