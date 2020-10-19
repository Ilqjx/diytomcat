package cn.ilqjx.diytomcat.test;

import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * @author upfly
 * @create 2020-10-19 17:53
 */
public class CustomizedURLClassLoader extends URLClassLoader {

    // 表示这个类加载器会到 urls 对应的这些文件里去找类文件
    public CustomizedURLClassLoader(URL[] urls) {
        super(urls);
    }

    public static void main(String[] args) throws MalformedURLException, ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        HOW2J how2J = new HOW2J();

        URL url = new URL("file:d:/project/idea/diytomcat/jar_4_test/test.jar");
        URL[] urls = new URL[] {url};

        CustomizedURLClassLoader loader = new CustomizedURLClassLoader(urls);
        Class<?> clazz = loader.loadClass("cn.ilqjx.diytomcat.test.HOW2J");
        System.out.println(clazz.getClassLoader());
    }
}
