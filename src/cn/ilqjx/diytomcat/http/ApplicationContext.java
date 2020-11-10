package cn.ilqjx.diytomcat.http;

import cn.ilqjx.diytomcat.catalina.Context;

import java.io.File;
import java.util.*;

/**
 * jsp 里的这个 <% application.setAttribute()%>，
 * application 内置对象就是这个 ApplicationContext
 *
 * @author upfly
 * @create 2020-11-10 19:27
 */
public class ApplicationContext extends BaseServletContext {
    private Map<String, Object> attributesMap; // 存放属性
    private Context context;

    public ApplicationContext(Context context) {
        this.attributesMap = new HashMap<>();
        this.context = context;
    }

    @Override
    public void setAttribute(String name, Object value) {
        attributesMap.put(name, value);
    }

    @Override
    public Object getAttribute(String name) {
        return attributesMap.get(name);
    }

    @Override
    public void removeAttribute(String name) {
        attributesMap.remove(name);
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        Set<String> keys = attributesMap.keySet();
        return Collections.enumeration(keys);
    }

    @Override
    public String getRealPath(String path) {
        return new File(context.getDocBase(), path).getAbsolutePath();
    }
}
