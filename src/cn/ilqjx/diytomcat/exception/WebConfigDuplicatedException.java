package cn.ilqjx.diytomcat.exception;

/**
 * 在 web.xml 中重复配置 Servlet 时抛出此异常
 *
 * @author upfly
 * @create 2020-10-08 19:23
 */
public class WebConfigDuplicatedException extends Exception {

    public WebConfigDuplicatedException(String msg) {
        super(msg);
    }
}
