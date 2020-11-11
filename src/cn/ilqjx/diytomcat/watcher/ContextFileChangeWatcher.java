package cn.ilqjx.diytomcat.watcher;

import cn.hutool.core.io.watch.WatchMonitor;
import cn.hutool.core.io.watch.WatchUtil;
import cn.hutool.core.io.watch.Watcher;
import cn.hutool.log.LogFactory;
import cn.ilqjx.diytomcat.catalina.Context;

import java.nio.file.Path;
import java.nio.file.WatchEvent;

/**
 * @author upfly
 * @create 2020-11-08 20:00
 */
public class ContextFileChangeWatcher {
    private WatchMonitor monitor; // 监听器
    private boolean stop = false; // 标记是否已经暂停

    public ContextFileChangeWatcher(Context context) {
        /*
         WatchUtil.createAll(String path, int maxDepth, Watcher watcher) 会返回一个 WatchMonitor 对象，
         path: 监听的文件或目录，
         maxDepth: 监听的深入，
         watcher: 当有文件发生变化时就会访问 Watcher 对应的方法
         */
        this.monitor = WatchUtil.createAll(context.getDocBase(), Integer.MAX_VALUE, new Watcher() {
            private void dealWith(WatchEvent<?> event) {
                // 这是一个异步处理的事件，当文件发生变化时，可能会发过来很多事件，
                // 我们需要一个一个的进行处理，否则可能会造成 Context 重载多次
                synchronized (ContextFileChangeWatcher.class) {
                    // 获取当前发生变化的文件或目录名称
                    String fileName = event.context().toString();

                    if (stop) {
                        return;
                    }

                    // idea 编译文件的方式很迷，删除目录在重新创建
                    if (fileName.endsWith(".class") || fileName.endsWith(".jar") || fileName.endsWith(".xml")) {
                        stop = true;
                        LogFactory.get().info(ContextFileChangeWatcher.this + " 检测到了Web应用下的重要文件变化 {} ", fileName);
                        context.reload();
                    }
                }
            }

            @Override
            public void onCreate(WatchEvent<?> watchEvent, Path path) {
                dealWith(watchEvent);
            }

            @Override
            public void onModify(WatchEvent<?> watchEvent, Path path) {
                dealWith(watchEvent);
            }

            @Override
            public void onDelete(WatchEvent<?> watchEvent, Path path) {
                dealWith(watchEvent);
            }

            /**
             * 丢失的事件
             *
             * @param watchEvent
             * @param path
             */
            @Override
            public void onOverflow(WatchEvent<?> watchEvent, Path path) {
                dealWith(watchEvent);
            }
        });

        this.monitor.setDaemon(true);
    }

    public void start() {
        monitor.start();
    }

    public void close() {
        monitor.close();
    }
}
