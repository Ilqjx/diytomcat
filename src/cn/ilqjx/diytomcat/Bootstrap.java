package cn.ilqjx.diytomcat;

import cn.ilqjx.diytomcat.classloader.CommonClassLoader;

import java.lang.reflect.Method;

/**
 * @author upfly
 * @create 2020-09-06 20:42
 */
public class Bootstrap {

    public static void main(String[] args) throws Exception {
        CommonClassLoader commonClassLoader = new CommonClassLoader();
        // getContextClassLoader(): 获取线程的上下文类加载器
        // setContextClassLoader(): 设置线程的上下文类加载器
        // 当前线程加载任何类都会使用 CommonClassLoader 这个类加载器
        Thread.currentThread().setContextClassLoader(commonClassLoader);
        String serverClassName = "cn.ilqjx.diytomcat.catalina.Server";
        Class<?> serverClazz = commonClassLoader.loadClass(serverClassName);
        System.out.println(serverClazz.getClassLoader());
        Object serverObject = serverClazz.newInstance();
        Method startMethod = serverClazz.getMethod("start");
        startMethod.invoke(serverObject);
    }
}
