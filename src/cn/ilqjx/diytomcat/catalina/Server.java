package cn.ilqjx.diytomcat.catalina;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.log.LogFactory;
import cn.hutool.system.SystemUtil;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author upfly
 * @create 2020-09-22 19:30
 */
public class Server {
    private Service service;

    public Server() {
        this.service = new Service(this);
    }

    public void start() {
        TimeInterval timeInterval = DateUtil.timer();
        logJVM();
        init();
        LogFactory.get().info("Server startup in {} ms", timeInterval.intervalMs());
    }

    public void init() {
        service.start();
    }

    /**
     * 打印 jvm 信息并输出到日志中
     */
    private static void logJVM() {
        Map<String, String> infos = new LinkedHashMap<>();
        infos.put("Server version", "How2j DiyTomcat/1.0.1");
        infos.put("Server built", "2020-09-16 19:35:28");
        infos.put("Server number", "1.0.1");
        infos.put("OS Name\t", SystemUtil.get("os.name"));
        infos.put("OS Version", SystemUtil.get("os.version"));
        // os.arch: 操作系统的架构
        infos.put("Architecture", SystemUtil.get("os.arch"));
        // java.home: Java 安装目录
        infos.put("Java Home", SystemUtil.get("java.home"));
        infos.put("JVM Version", SystemUtil.get("java.runtime.version"));
        // java.vm.specification.vendor: Java 虚拟机规范供应商
        infos.put("JVM Vendor", SystemUtil.get("java.vm.specification.vendor"));

        Set<String> keys = infos.keySet();
        for (String key : keys) {
            LogFactory.get().info(key + ":\t\t" + infos.get(key));
        }
    }
}
