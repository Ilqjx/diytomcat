package cn.ilqjx.diytomcat.test;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.ilqjx.diytomcat.http.Request;
import cn.ilqjx.diytomcat.util.Constant;
import org.junit.Test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author upfly
 * @create 2020-09-14 19:21
 */
public class MyTest {

    @Test
    public void test9() {
        String str = "";

        String[] strings = str.split("&");
        for (int i = 0; i < strings.length; i++) {
            System.out.println(strings[i]);
        }

    //     String s = StrUtil.subBefore(str, "abc", false);
    //     System.out.println(s);
    }

    @Test
    public void test8() {
        Map<String, String> map = new HashMap<>();
        map.put(null, null);
        map.put(" ", "");
        System.out.println(map.get(null));
        System.out.println(map.get(" "));
    }

    @Test
    public void test7() {
        Class<Request> clazz = Request.class;
        System.out.println(clazz);

        ClassLoader loader = Object.class.getClassLoader();
        System.out.println(loader);

        ClassLoader loader1 = Request.class.getClassLoader();
        System.out.println(loader1);
    }

    @Test
    public void test6() {
        Object obj = Object.class;
        System.out.println(obj);

        Object obj2 = new Object();
        System.out.println(obj2);
        Class<?> clazz = obj2.getClass();

        System.out.println(clazz.getName());
        Object obj3 = ReflectUtil.newInstance(clazz.getName());
        Object hashCode = ReflectUtil.invoke(obj3, "hashCode");
        System.out.println(hashCode);

        if (obj == clazz) {
            System.out.println("equals");
        }
    }

    @Test
    public void test5() {
        RuntimeException exception = new RuntimeException("测试");
        String message = exception.getMessage();
        // System.out.println(message);
        System.out.println(exception.toString());
    }

    @Test
    public void test4() {
        String uri = "/a/index.html/";
        String path = StrUtil.subBetween(uri, "/", "/");
        System.out.println(path);
    }

    @Test
    public void test3() {
        String str = null;
        if (str == null) {
            System.out.println("null");
        }
    }

    @Test
    public void test2() {
        // File folder = new File("D:\\download\\log4j");
        File folder = new File("D:\\download\\log4j\\bin\\log4j\\TestLog4j.class");
        String name = folder.getName();
        System.out.println(name);
    }

    @Test
    public void test1() {
        File webappsFolder = Constant.WEBAPPS_FOLDER;
        System.out.println(webappsFolder);

        File rootFolder = Constant.ROOT_FOLDER;
        System.out.println(rootFolder);
    }
}
