package cn.ilqjx.diytomcat.http;

import cn.hutool.core.util.StrUtil;
import cn.ilqjx.diytomcat.catalina.Context;
import cn.ilqjx.diytomcat.catalina.Engine;
import cn.ilqjx.diytomcat.catalina.Service;
import cn.ilqjx.diytomcat.util.MiniBrowser;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * @author upfly
 * @create 2020-09-12 19:51
 */
public class Request extends BaseRequest {
    private String requestString;
    private String uri;
    private Socket socket;
    private Context context;
    private Service service;

    public Request(Socket socket, Service service) throws IOException {
        this.socket = socket;
        this.service = service;
        parseHttpRequest();
        if (StrUtil.isEmpty(requestString)) {
            return;
        }
        parseUri();
        parseContext();
        if (!"/".equals(context.getPath())) {
            // 如果不是根路径，需要对 uri 进行修正
            // uri: /a/index.html，path: /a，uri 就应该是 /index.html
            uri = StrUtil.removePrefix(uri, context.getPath());
            // 如果 uri 为 /a，那么此时的 uri 就变成 "" 了，所以将 uri 修改为 "/"
            if (StrUtil.isEmpty(uri)) {
                uri = "/";
            }
        }
    }

    public String getRequestString() {
        return requestString;
    }

    public String getUri() {
        return uri;
    }

    public Context getContext() {
        return context;
    }

    /**
     * 解析 Context 对象
     */
    private void parseContext() {
        Engine engine = service.getEngine();
        context = engine.getDefaultHost().getContext(uri);
        // 如果 context 不为 null，说明访问了一个存在的路径，此时访问的是例如 /a 这样的路径
        if (context != null) {
            return;
        }

        // uri: ROOT目录下的路径 /index.html，a目录下的路径 /a/index.html 或 /a/b/index.html
        // StrUtil.subBetween(): 从前往后找到即返回，也就是找到后不会再继续向后寻找
        String path = StrUtil.subBetween(uri, "/", "/");
        if (path == null) {
            path = "/";
        } else {
            path = "/" + path;
        }

        context = engine.getDefaultHost().getContext(path);
        // 如果 context 为 null，说明访问了一个不存在的路径，跳转到根路径
        if (context == null) {
            context = engine.getDefaultHost().getContext("/");
        }
    }

    /**
     * 解析 http 请求
     *
     * @throws IOException
     */
    private void parseHttpRequest() throws IOException {
        // 接收从浏览器发送过来的信息
        InputStream is = this.socket.getInputStream();
        /*
         fully 为 false 的原因：
            因为浏览器使用长连接，发出的连接不会主动关闭，那么 Request 读取数据的时候就会卡在那里
         */
        byte[] bytes = MiniBrowser.readBytes(is, false);
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
}
