package cn.ilqjx.diytomcat.catalina;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.LogFactory;
import cn.ilqjx.diytomcat.http.Request;
import cn.ilqjx.diytomcat.http.Response;
import cn.ilqjx.diytomcat.util.Constant;
import cn.ilqjx.diytomcat.util.ThreadPoolUtil;
import cn.ilqjx.diytomcat.util.WebXMLUtil;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author upfly
 * @create 2020-10-07 20:42
 */
public class Connector implements Runnable {
    int port; // 端口号
    private Service service;

    public Connector(Service service) {
        this.service = service;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Service getService() {
        return service;
    }

    public void init() {
        LogFactory.get().info("Initializing ProtocolHandler [http-bio-{}]", port);
    }

    public void start() {
        LogFactory.get().info("Starting ProtocolHandler [http-bio-{}]", port);
        new Thread(this).start();
    }

    @Override
    public void run() {
        try {
            // 注释掉了，为了和 tomcat 保持一致
            // 判断端口是否被占用，没被占用返回 true
            // if (!NetUtil.isUsableLocalPort(port)) {
            //     System.out.println(port + "端口已被占用");
            //     return;
            // }
            // 服务端打开 port 端口
            ServerSocket ss = new ServerSocket(port);

            while (true) {
                // 监听 port 端口，看是否有连接请求过来
                Socket socket = ss.accept();

                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Request request = new Request(socket, service);
                            Response response = new Response();
                            HttpProcessor httpProcessor = new HttpProcessor();
                            httpProcessor.execute(socket, request, response);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                };
                ThreadPoolUtil.run(runnable);
            }
        } catch (IOException e) {
            LogFactory.get().error(e);
            e.printStackTrace();
        }
    }
}
