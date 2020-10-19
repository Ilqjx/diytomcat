package cn.ilqjx.diytomcat.classloader;

import org.junit.Test;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * 用于扫描 lib 目录下的 jar，然后调用 addUrl() 将 lib 中的这些 jar 加载到当前类加载器中，
 * 当调用 loadClass() 的时候，就会到这些 jar 里面去找了。
 *
 * @author upfly
 * @create 2020-10-19 19:49
 */
public class CommonClassLoader extends URLClassLoader {

    public CommonClassLoader() {
        super(new URL[] {});

        try {
            File workingFolder = new File(System.getProperty("user.dir"));
            File libFolder = new File(workingFolder, "lib");
            File[] jarFiles = libFolder.listFiles();
            for (File file : jarFiles) {
                if (file.getName().endsWith(".jar")) {
                    URL url = new URL("file:" + file.getAbsolutePath());
                    this.addURL(url);
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test() throws ClassNotFoundException {
        // Class<?> clazz = Class.forName("cn.hutool.aop.ProxyUtil");
        // ClassLoader classLoader = clazz.getClassLoader();
        // System.out.println(classLoader);

        CommonClassLoader loader = new CommonClassLoader();
        Class<?> clazz = loader.loadClass("cn.hutool.aop.ProxyUtil");
        System.out.println(clazz.getClassLoader());
    }
}
