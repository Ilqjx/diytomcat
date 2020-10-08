package cn.ilqjx.diytomcat.webappservlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author upfly
 * @create 2020-10-08 15:57
 */
public class HelloServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.getWriter().println("Hello DIY Tomcat from HelloServlet");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
