package cn.ilqjx.diytomcat.http;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.ilqjx.diytomcat.catalina.Context;
import cn.ilqjx.diytomcat.catalina.Engine;
import cn.ilqjx.diytomcat.catalina.Service;
import cn.ilqjx.diytomcat.util.MiniBrowser;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

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
    private String method; // http 请求中的 method
    private ServletContext servletContext;

    private String queryString; // 查询字符串
    private Map<String, String[]> parameterMap; // 存放请求参数

    public Request(Socket socket, Service service) throws IOException {
        this.socket = socket;
        this.service = service;
        this.parameterMap = new LinkedHashMap<>();

        parseHttpRequest();
        if (StrUtil.isEmpty(requestString)) {
            return;
        }
        parseUri();
        parseContext();
        parseMethod();
        if (!"/".equals(context.getPath())) {
            // 如果不是根路径，需要对 uri 进行修正
            // uri: /a/index.html，path: /a，uri 就应该是 /index.html
            uri = StrUtil.removePrefix(uri, context.getPath());
            // 如果 uri 为 /a，那么此时的 uri 就变成 "" 了，所以将 uri 修改为 "/"
            if (StrUtil.isEmpty(uri)) {
                uri = "/";
            }
        }

        parseParameters();
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

    @Override
    public String getMethod() {
        return method;
    }

    @Override
    public ServletContext getServletContext() {
        return context.getServletContext();
    }

    @Override
    public String getRealPath(String path) {
        return getServletContext().getRealPath(path);
    }

    @Override
    public String getParameter(String name) {
        String[] values = parameterMap.get(name);
        // 什么时候存在 values 长度为 0 的情况？ name=
        if (values != null && values.length != 0) {
            return values[0];
        }
        return null;
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return parameterMap;
    }

    @Override
    public Enumeration<String> getParameterNames() {
        return Collections.enumeration(parameterMap.keySet());
    }

    @Override
    public String[] getParameterValues(String name) {
        return parameterMap.get(name);
    }

    /**
     * 解析 method
     *
     * @return
     */
    private void parseMethod() {
        method = StrUtil.subBefore(requestString, " ", false);
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

        // 根据 path 返回对应的 context
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
        // 可能带参数的 uri -> /index.html?name=gareen、/index.html
        String temp = StrUtil.subBetween(requestString, " ", " ");
        // 不带参数，没有 ? 就是不带参数
        if (!StrUtil.contains(temp, '?')) {
            uri = temp;
            return;
        }
        // 带参
        uri = StrUtil.subBefore(temp, "?", false);
    }

    /**
     * 解析参数
     */
    private void parseParameters() {
        if ("GET".equals(method)) {
            String url = StrUtil.subBetween(requestString, " ");
            // 如果没有 ? 或者 ? 后面没有参数都返回空串
            queryString = StrUtil.subAfter(url, "?", false);
        } else if ("POST".equals(method)) {
            queryString = StrUtil.subAfter(requestString, "\r\n\r\n", false);
        }

        if (queryString == null || queryString.equals("")) {
            return;
        }

        parseQueryString();
    }

    /**
     * 解析查询字符串
     */
    private void parseQueryString() {
        // 对 queryString 进行解码
        queryString = URLUtil.decode(queryString);
        String[] parameterValues = queryString.split("&");
        for (String parameterValue : parameterValues) {
            // 如果没有 = 会返回 parameterValue
            String name = StrUtil.subBefore(parameterValue, "=", false);
            String value = StrUtil.subAfter(parameterValue, "=", false);

            String[] values = parameterMap.get(name);
            if (values == null) {
                values = new String[] {value};
            } else {
                values = ArrayUtil.append(values, value);
            }

            parameterMap.put(name, values);
        }
    }
}
