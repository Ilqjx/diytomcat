package cn.ilqjx.diytomcat.catalina;

import cn.ilqjx.diytomcat.util.ServerXMLUtil;

/**
 * @author upfly
 * @create 2020-09-22 19:17
 */
public class Service {
    private String name;
    private Engine engine;
    private Server server;

    public Service(Server server) {
        this.name = ServerXMLUtil.getServiceName();
        this.engine = new Engine(this);
        this.server = server;
    }

    public Engine getEngine() {
        return engine;
    }

    public Server getServer() {
        return server;
    }
}
