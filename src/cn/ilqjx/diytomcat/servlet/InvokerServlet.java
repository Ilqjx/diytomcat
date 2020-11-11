package cn.ilqjx.diytomcat.servlet;

import cn.hutool.core.util.ReflectUtil;
import cn.ilqjx.diytomcat.catalina.Context;
import cn.ilqjx.diytomcat.http.Request;
import cn.ilqjx.diytomcat.http.Response;
import cn.ilqjx.diytomcat.util.Constant;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author upfly
 * @create 2020-10-15 20:02
 */
public class InvokerServlet extends HttpServlet {
    private static InvokerServlet instance = new InvokerServlet();

    private InvokerServlet() {
    }

    public static InvokerServlet getInstance() {
        return instance;
    }

    @Override
    public void service(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        Request request = (Request) httpServletRequest;
        Response response = (Response) httpServletResponse;

        String uri = request.getUri();
        Context context = request.getContext();
        String servletClassName = context.getServletClassName(uri);

        try {
            // 必须调用类加载器进行加载，因为没有类加载器对其加载
            Class<?> servletClazz = context.getWebappClassLoader().loadClass(servletClassName);
            // Object servletObject = ReflectUtil.newInstance(servletClazz);
            Object servletObject = context.getServlet(servletClazz);

            /*
             调用目标 Servlet 的 service()，因为目标 Servlet 也继承了 HttpServlet，
             从而提供了 service()，然后根据 request 的 METHOD 来访问对应的 doGet() / doPost()
             */
            ReflectUtil.invoke(servletObject, "service", request, response);
            response.setStatus(Constant.CODE_200); // 表示处理成功
        } catch (ClassNotFoundException e) {
            throw new RuntimeException();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ServletException e) {
            e.printStackTrace();
        }
    }
}
