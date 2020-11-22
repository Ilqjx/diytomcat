package cn.ilqjx.diytomcat.http;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import java.util.*;

/**
 * @author upfly
 * @create 2020-11-22 19:33
 */
public class StandardSession implements HttpSession {
    private Map<String, Object> attributesMap; // 存放数据

    private String id; // session 的唯一 id
    private long creationTime; // 创建时间
    // 用于对session自动失效，一般默认是30分钟，如果不登录，session就会自动失效
    private long lastAccessedTime; // 最后一次访问时间
    private ServletContext servletContext;
    private int maxInactiveInterval; // 最大存活时间

    public StandardSession(String jsessionid, ServletContext servletContext) {
        this.attributesMap = new HashMap<>();
        this.id = jsessionid;
        this.creationTime = System.currentTimeMillis();
        this.servletContext = servletContext;
    }

    @Override
    public long getCreationTime() {
        return creationTime;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public long getLastAccessedTime() {
        return lastAccessedTime;
    }

    public void setLastAccessedTime(long lastAccessedTime) {
        this.lastAccessedTime = lastAccessedTime;
    }

    @Override
    public ServletContext getServletContext() {
        return servletContext;
    }

    @Override
    public void setMaxInactiveInterval(int maxInactiveInterval) {
        this.maxInactiveInterval = maxInactiveInterval;
    }

    @Override
    public int getMaxInactiveInterval() {
        return maxInactiveInterval;
    }

    @Override
    public HttpSessionContext getSessionContext() {
        return null;
    }

    @Override
    public Object getAttribute(String name) {
        return attributesMap.get(name);
    }

    @Override
    public Object getValue(String s) {
        return null;
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        Set<String> names = attributesMap.keySet();
        return Collections.enumeration(names);
    }

    @Override
    public String[] getValueNames() {
        return null;
    }

    @Override
    public void setAttribute(String name, Object value) {
        attributesMap.put(name, value);
    }

    @Override
    public void putValue(String s, Object o) {
    }

    @Override
    public void removeAttribute(String name) {
        attributesMap.remove(name);
    }

    @Override
    public void removeValue(String s) {
    }

    @Override
    public void invalidate() {
    }

    @Override
    public boolean isNew() {
        return creationTime == lastAccessedTime;
    }
}
