package cn.ilqjx.diytomcat.classloader;

import cn.hutool.core.io.FileUtil;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

/**
 * 把 WEB-INF/lib/xxx.jar 和 WEB-INF/classes 加载到类加载器中
 *
 * @author upfly
 * @create 2020-11-06 19:31
 */
public class WebappClassLoader extends URLClassLoader {

    public WebappClassLoader(String docBase, ClassLoader commonClassLoader) {
        super(new URL[] {}, commonClassLoader);

        try {
            File webInfFolder = new File(docBase, "WEB-INF");
            File classesFolder = new File(webInfFolder, "classes");
            File libFolder = new File(webInfFolder, "lib");

            URL url;
            // 不加 "/" 的话，URLClassLoader 不能识别为目录
            url = new URL("file:" + classesFolder.getAbsolutePath() + "/");
            this.addURL(url);
            List<File> jarFiles = FileUtil.loopFiles(libFolder);
            for (File jarFile : jarFiles) {
                url = new URL("file:" + jarFile.getAbsolutePath());
                this.addURL(url);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭这个 WebappClassLoader
     */
    public void stop() {
        try {
            close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
