package cn.ilqjx.diytomcat;

import cn.ilqjx.diytomcat.catalina.Server;

/**
 * @author upfly
 * @create 2020-09-06 20:42
 */
public class Bootstrap {

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }
}
