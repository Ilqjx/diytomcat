package cn.ilqjx.diytomcat.internalservlet;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.ilqjx.diytomcat.catalina.Context;
import cn.ilqjx.diytomcat.http.Request;
import cn.ilqjx.diytomcat.http.Response;
import cn.ilqjx.diytomcat.util.Constant;
import cn.ilqjx.diytomcat.util.WebXMLUtil;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;

/**
 * @author upfly
 * @create 2020-10-16 19:07
 */
public class DefaultServlet extends HttpServlet {
    private static DefaultServlet instance = new DefaultServlet();

    private DefaultServlet() {
    }

    public static DefaultServlet getInstance() {
        return instance;
    }

    @Override
    public void service(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        Request request = (Request) httpServletRequest;
        Response response = (Response) httpServletResponse;

        Context context = request.getContext();
        String uri = request.getUri();

        if ("/500.html".equals(uri)) {
            throw new RuntimeException("this is a deliberately created exception");
        }

        if ("/".equals(uri)) {
            // 获取欢迎文件名
            uri = WebXMLUtil.getWelcomeFile(context);
        }

        String fileName = StrUtil.removePrefix(uri, "/");
        // request.getRealPath(fileName) 根据文件名获取绝对路径
        File file = new File(request.getRealPath(fileName));

        if (file.exists()) {
            // 获取文件后缀名，设置 content-type
            String extName = FileUtil.extName(file);
            String mimeType = WebXMLUtil.getMimeType(extName);
            response.setContentType(mimeType);

            byte[] body = FileUtil.readBytes(file);
            response.setBody(body);

            // FileUtil.readUtf8String(file) 直接把文件的内容读出来赋给 fileContent
            // String fileContent = FileUtil.readUtf8String(file);
            // response.getWriter().println(fileContent);

            // if ("timeConsume.html".equals(fileName)) {
            //     ThreadUtil.sleep(1000);
            // }

            response.setStatus(Constant.CODE_200);
        } else {
            response.setStatus(Constant.CODE_404);
        }
    }
}
