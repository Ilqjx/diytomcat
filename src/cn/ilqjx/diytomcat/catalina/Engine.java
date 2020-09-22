package cn.ilqjx.diytomcat.catalina;

import cn.ilqjx.diytomcat.util.ServerXMLUtil;

import java.util.List;

/**
 * @author upfly
 * @create 2020-09-21 20:12
 */
public class Engine {
    private String defaultHost; // 默认的 host 名称
    private List<Host> hosts;
    private Service service;

    public Engine(Service service) {
        this.service = service;
        this.defaultHost = ServerXMLUtil.getEngineDefaultHost();
        this.hosts = ServerXMLUtil.getHosts(this);
        checkDefault();
    }

    public Host getDefaultHost() {
        for (Host host : hosts) {
            if (defaultHost.equals(host.getName())) {
                return host;
            }
        }
        return null;
    }

    private void checkDefault() {
        if (getDefaultHost() == null) {
            throw new RuntimeException("the defaultHost " + defaultHost + " does not exist!");
        }
    }
}
