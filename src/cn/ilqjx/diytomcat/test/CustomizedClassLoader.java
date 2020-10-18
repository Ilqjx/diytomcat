package cn.ilqjx.diytomcat.test;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import org.junit.Test;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 类加载器：
 *     将一个全限定类名转换为这个类所对应的字节码文件，然后将字节码文件转换为字节数组，
 *     然后再调用 ClassLoader 的 defineClass()，就可以获得这个类的类对象了，这个类对象
 *     的类加载器就是做这件事情的类（在这里是当前类）。
 *
 * @author upfly
 * @create 2020-10-18 18:59
 */
public class CustomizedClassLoader extends ClassLoader {
    // user.dir: 获取工作目录（xxx/diytomcat）
    private File classesFolder = new File(System.getProperty("user.dir"), "classes_4_test");

    @Override
    protected Class<?> findClass(String fullQualifiedName) throws ClassNotFoundException {
        byte[] data = loadClassData(fullQualifiedName);
        return defineClass(fullQualifiedName, data, 0, data.length);
    }

    /**
     * @param fullQualifiedName 全限定类名
     * @return 全限定类名文件所对应的字节数组
     */
    private byte[] loadClassData(String fullQualifiedName) throws ClassNotFoundException {
        String fileName = StrUtil.replace(fullQualifiedName, ".", "/") + ".class";
        File classFile = new File(classesFolder, fileName);
        if (!classFile.exists()) {
            throw new ClassNotFoundException(fullQualifiedName);
        }
        return FileUtil.readBytes(classFile);
    }

    @Test
    public void test() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<?> clazz = findClass("cn.how2j.diytomcat.test.HOW2J");
        Object obj = clazz.newInstance();
        Method hello = clazz.getDeclaredMethod("hello");
        hello.invoke(obj);

        // ClassLoader classLoader = clazz.getClassLoader();
        // System.out.println(classLoader); // cn.ilqjx.diytomcat.test.CustomizedClassLoader@29104a
    }
}
