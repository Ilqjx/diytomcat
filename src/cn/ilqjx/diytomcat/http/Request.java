package cn.ilqjx.diytomcat.http;

import cn.hutool.core.util.StrUtil;
import cn.ilqjx.diytomcat.Bootstrap;
import cn.ilqjx.diytomcat.catalina.Context;
import cn.ilqjx.diytomcat.util.MiniBrowser;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * @author upfly
 * @create 2020-09-12 19:51
 */
public class Request {
    private String requestString;
    private String uri;
    private Socket socket;
    private Context context;

    public Request(Socket socket) throws IOException {
        this.socket = socket;
        parseHttpRequest();
        if (StrUtil.isEmpty(requestString)) {
            return;
        }
        parseUri();
        parseContext();
        if (!"/".equals(context.getPath())) {
            // 如果不是根路径，
            uri = StrUtil.removePrefix(uri, context.getPath());
        }
    }

    /**
     * 解析 Context 对象
     */
    private void parseContext() {
        // uri: ROOT目录下的路径 /index.html，a目录下的路径 /a/index.html 或 /a/b/index.html
        // StrUtil.subBetween(): 从前往后找到即返回，也就是找到后不会再继续向后寻找
        String path = StrUtil.subBetween(uri, "/", "/");
        if (path == null) {
            path = "/";
        } else {
            path = "/" + path;
        }

        context = Bootstrap.contextMap.get(path);
        // 如果 context 为 null，说明访问了一个不存在的路径
        if (context == null) {
            context = Bootstrap.contextMap.get("/");
        }
    }

    /**
     * 解析 http 请求
     * @throws IOException
     */
    private void parseHttpRequest() throws IOException {
        InputStream is = this.socket.getInputStream();
        byte[] bytes = MiniBrowser.readBytes(is);
        requestString = new String(bytes, "utf-8");
    }

    /**
     * 解析 uri
     */
    private void parseUri() {
        // 可能带参数的uri -> /index.html?name=gareen、/index.html
        String temp = StrUtil.subBetween(requestString, " ", " ");
        // 不带参数，没有 ? 就是不带参数
        if (!StrUtil.contains(temp, '?')) {
            uri = temp;
            return;
        }
        // 带参
        uri = StrUtil.subBefore(temp, "?", false);
    }

    public String getRequestString() {
        return requestString;
    }

    public String getUri() {
        return uri;
    }
}
