package cn.ilqjx.diytomcat.http;

import cn.hutool.core.util.StrUtil;
import cn.ilqjx.diytomcat.catalina.Context;
import cn.ilqjx.diytomcat.catalina.Engine;
import cn.ilqjx.diytomcat.catalina.Service;
import cn.ilqjx.diytomcat.util.MiniBrowser;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.security.Principal;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

/**
 * @author upfly
 * @create 2020-09-12 19:51
 */
public class Request implements HttpServletRequest {
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
    public String getAuthType() {
        return null;
    }

    @Override
    public Cookie[] getCookies() {
        return new Cookie[0];
    }

    @Override
    public long getDateHeader(String s) {
        return 0;
    }

    @Override
    public String getHeader(String s) {
        return null;
    }

    @Override
    public Enumeration<String> getHeaders(String s) {
        return null;
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        return null;
    }

    @Override
    public int getIntHeader(String s) {
        return 0;
    }

    @Override
    public String getMethod() {
        return null;
    }

    @Override
    public String getPathInfo() {
        return null;
    }

    @Override
    public String getPathTranslated() {
        return null;
    }

    @Override
    public String getContextPath() {
        return null;
    }

    @Override
    public String getQueryString() {
        return null;
    }

    @Override
    public String getRemoteUser() {
        return null;
    }

    @Override
    public boolean isUserInRole(String s) {
        return false;
    }

    @Override
    public Principal getUserPrincipal() {
        return null;
    }

    @Override
    public String getRequestedSessionId() {
        return null;
    }

    @Override
    public String getRequestURI() {
        return null;
    }

    @Override
    public StringBuffer getRequestURL() {
        return null;
    }

    @Override
    public String getServletPath() {
        return null;
    }

    @Override
    public HttpSession getSession(boolean b) {
        return null;
    }

    @Override
    public HttpSession getSession() {
        return null;
    }

    @Override
    public boolean isRequestedSessionIdValid() {
        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromCookie() {
        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromURL() {
        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromUrl() {
        return false;
    }

    @Override
    public boolean authenticate(HttpServletResponse httpServletResponse) throws IOException, ServletException {
        return false;
    }

    @Override
    public void login(String s, String s1) throws ServletException {

    }

    @Override
    public void logout() throws ServletException {

    }

    @Override
    public Collection<Part> getParts() throws IOException, IllegalStateException, ServletException {
        return null;
    }

    @Override
    public Part getPart(String s) throws IOException, IllegalStateException, ServletException {
        return null;
    }

    @Override
    public Object getAttribute(String s) {
        return null;
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        return null;
    }

    @Override
    public String getCharacterEncoding() {
        return null;
    }

    @Override
    public void setCharacterEncoding(String s) throws UnsupportedEncodingException {

    }

    @Override
    public int getContentLength() {
        return 0;
    }

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return null;
    }

    @Override
    public String getParameter(String s) {
        return null;
    }

    @Override
    public Enumeration<String> getParameterNames() {
        return null;
    }

    @Override
    public String[] getParameterValues(String s) {
        return new String[0];
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return null;
    }

    @Override
    public String getProtocol() {
        return null;
    }

    @Override
    public String getScheme() {
        return null;
    }

    @Override
    public String getServerName() {
        return null;
    }

    @Override
    public int getServerPort() {
        return 0;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return null;
    }

    @Override
    public String getRemoteAddr() {
        return null;
    }

    @Override
    public String getRemoteHost() {
        return null;
    }

    @Override
    public void setAttribute(String s, Object o) {

    }

    @Override
    public void removeAttribute(String s) {

    }

    @Override
    public Locale getLocale() {
        return null;
    }

    @Override
    public Enumeration<Locale> getLocales() {
        return null;
    }

    @Override
    public boolean isSecure() {
        return false;
    }

    @Override
    public RequestDispatcher getRequestDispatcher(String s) {
        return null;
    }

    @Override
    public String getRealPath(String s) {
        return null;
    }

    @Override
    public int getRemotePort() {
        return 0;
    }

    @Override
    public String getLocalName() {
        return null;
    }

    @Override
    public String getLocalAddr() {
        return null;
    }

    @Override
    public int getLocalPort() {
        return 0;
    }

    @Override
    public ServletContext getServletContext() {
        return null;
    }

    @Override
    public AsyncContext startAsync() {
        return null;
    }

    @Override
    public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse) {
        return null;
    }

    @Override
    public boolean isAsyncStarted() {
        return false;
    }

    @Override
    public boolean isAsyncSupported() {
        return false;
    }

    @Override
    public AsyncContext getAsyncContext() {
        return null;
    }

    @Override
    public DispatcherType getDispatcherType() {
        return null;
    }
}
