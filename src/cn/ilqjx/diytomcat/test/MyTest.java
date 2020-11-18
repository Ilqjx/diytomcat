package cn.ilqjx.diytomcat.test;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.ilqjx.diytomcat.http.Request;
import cn.ilqjx.diytomcat.util.Constant;
import org.junit.Test;

import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author upfly
 * @create 2020-09-14 19:21
 */
public class MyTest {

    @Test
    public void test12() {
        String line = "name";
        String name = StrUtil.subBefore(line, ":", false).trim().toLowerCase();
        String value = StrUtil.subAfter(line, ":", false).trim();
        System.out.println(name); // "name"
        System.out.println(value); // ""
    }

    @Test
    public void test11() {
        String requestString = "HTTP/1.1\r\nname: guozhenwei\r\nage: 18\r\n\r\nname=guozhenwei&age=18";
        parseHeaders(requestString);
    }

    private void parseHeaders(String requestString) {
        StringReader sr = new StringReader(requestString);
        List<String> lines = new ArrayList<>();
        IoUtil.readLines(sr, lines);
        // 第一行为请求行，跳过
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);

            // System.out.println("line: " + line);

            // 请求头和请求体之间的空行
            if (line.length() == 0) {
                break;
            }

            // String[] strings = line.split(":");
            // String name = strings[0];
            // String value = strings[1];

            String name = StrUtil.subBefore(line, ":", false).trim();
            String value = StrUtil.subAfter(line, ":", false).trim();

            System.out.println("name: " + name + ", value: " + value);

            // headerMap.put(name, value);
        }
    }

    @Test
    public void test10() {
        String name = "2000";
        Integer i = Convert.toInt(name, 0);
        System.out.println(i);
    }

    @Test
    public void test9() {
        String str = "";

        String[] strings = str.split("&");
        for (int i = 0; i < strings.length; i++) {
            System.out.println(strings[i]);
        }
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
