### DiyTomcat
---
> 参考how2j.cn上的实践项目，特此感谢。

项目结构：

![](https://s1.ax1x.com/2020/09/23/wjVtJA.png)

开发流程：
1. 迷你浏览器<br/>
这个浏览器会模拟发送 Http 协议的请求，并且获取完整的 Http 响应，通过这种方式，我们就可以更好的理解浏览器与服务器是如何通信的。
2. 自动测试<br/>
我们会对这个项目进行持续的改造和重构。为了重构之后以前的功能依旧可以使用，因此我们引入单元自动测试 JUnit。
3. HTTP 协议<br/>
了解 HTTP 的请求协议和响应协议。
4. Request 对象<br/>
引入 Request 对象，用来代表浏览器发过来的请求信息。
5. Response 对象<br/>
引入 Response 对象，用来代表服务器返回的响应信息。
6. 文本文件<br/>
实现访问文本文件的功能。
7. 日志<br/>
使用 log4j 生成日志文件。
8. 耗时任务<br/>
目前服务器是单线程的，为接下来的多线程做准备。
9. 线程池<br/>
我们采用 JDK 自带的线程池将服务器改造为多线程的。
10. 多应用<br/>
目前的做法只支持 ROOT 这么一个应用，那么我们将继续进行改造使得服务器支持多个应用。
11. 配置型多应用<br/>
通过配置 server.xml 来实现这个效果。
12. Tomcat 的内置对象<br/>
Host：Host 的意思是虚拟主机。通常都是 localhost，即表示本机。<br/>
Engine：Engine 表示 servlet 引擎，用来处理 servlet 的请求。<br/>
Service：Service 是 Engine 的父节点，用于代表 tomcat 提供的服务。<br/>
Server：Server 代表服务器本身。
13. 404<br/>
当返回文件不存在的时候，返回 404 页面。
14. 500<br/>
当服务器内部发生错误的时候，返回 500 页面。
15. 欢迎文件<br/>
所谓的欢迎文件就是指访问某个 context 的时候，如果没有指明文件，那么就默认访问 index.html / index.jsp 这种功能。
16. mime-type<br/>
描述消息内容的类型
17. 二进制文件<br/>
实现访问二进制文件的功能
18. Connector<br/>
支持多端口
19. HttpProcessor<br/>
重构，把 Connector 中处理请求的代码分离出来。
20. HelloServlet<br/>
由浅至深开发 Servlet
21. BaseRequest & BaseResponse<br/>
实现接口之后，Request 和 Response 中出现了很多空方法，看着很不清爽，所以创建 BaseRequest 和 BaseResponse 来解决这个问题。
22. 配置 Servlet<br/>
前面虽然开发了 Servlet 功能，但是是通过路径直接访问的，我们都知道 servlet 是在 WEB-INF/web.xml 里面进行配置的，接下来我们就来实现这个效果。
23. InvokerServlet<br/>
处理 Servlet
24. DefaultServlet<br/>
处理静态资源