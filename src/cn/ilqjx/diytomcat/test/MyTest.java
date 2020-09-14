package cn.ilqjx.diytomcat.test;

import cn.ilqjx.diytomcat.util.Constant;
import org.junit.Test;

import java.io.File;

/**
 * @author upfly
 * @create 2020-09-14 19:21
 */
public class MyTest {

    @Test
    public void test1() {
        File webappsFolder = Constant.WEBAPPS_FOLDER;
        System.out.println(webappsFolder);

        File rootFolder = Constant.ROOT_FOLDER;
        System.out.println(rootFolder);
    }
}
