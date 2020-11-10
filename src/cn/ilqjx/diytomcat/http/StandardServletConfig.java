package cn.ilqjx.diytomcat.http;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import java.util.*;

/**
 * ServletConfig 是 Servlet 初始化的时候，传进去的参数对象
 *
 * @author upfly
 * @create 2020-11-10 20:09
 */
public class StandardServletConfig implements ServletConfig {
    private ServletContext servletContext;
    private Map<String, String> initParameters;
    private String servletName;

    public StandardServletConfig(ServletContext servletContext, String servletName,
                                 Map<String, String> initParameters) {
        this.servletContext = servletContext;
        this.initParameters = initParameters;
        this.servletName = servletName;

        if (initParameters == null) {
            initParameters = new HashMap<>();
        }
    }

    @Override
    public String getServletName() {
        return servletName;
    }

    @Override
    public ServletContext getServletContext() {
        return servletContext;
    }

    @Override
    public String getInitParameter(String name) {
        return initParameters.get(name);
    }

    @Override
    public Enumeration<String> getInitParameterNames() {
        Set<String> keys = initParameters.keySet();
        return Collections.enumeration(keys);
    }
}
