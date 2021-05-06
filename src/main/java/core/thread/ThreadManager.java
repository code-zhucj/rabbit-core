package core.thread;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程管理器
 *
 * @author zhuchuanji
 */
public final class ThreadManager {

    protected static int corePoolSize = 200;
    protected static int maximumPoolSize = 1000;
    protected static long keepAliveTime = 60;
    protected static int maxUser;
    protected static int aliveTime;
    protected static TimeUnit unit = TimeUnit.SECONDS;
    protected static RejectedExecutionHandler handler = new ThreadPoolExecutor.AbortPolicy();
    public final WorkerQueueThreadPoolExecutor workerQueue;
    public final ThreadPoolExecutor system;
    public final Map<String, ThreadPoolExecutor> pools = new ConcurrentHashMap<>();
    public static final Logger log = LogManager.getLogger(ThreadManager.class);

    public static final ThreadManager MANAGER = new ThreadManager();

    public static ThreadManager getInstance() {
        return MANAGER;
    }

    private ThreadManager() {
        system = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, new LinkedTransferQueue<>(),
                createThreadFactory("system", false), handler);
        workerQueue = new WorkerQueueThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, new LinkedTransferQueue<>(),
                createThreadFactory("user", false), handler);
    }

    public ThreadPoolExecutor getSystemThreadPool() {
        return this.system;
    }

    public WorkerQueueThreadPoolExecutor getWorkerQueueThreadPool() {
        return this.workerQueue;
    }

    public ThreadPoolExecutor getThreadPoolExecutor(String poolName) {
        return pools.get(poolName);
    }

    public ThreadPoolExecutor newThreadPoolExecutor(String poolName) {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, new LinkedTransferQueue<>(),
                createThreadFactory(poolName, false), handler);
        pools.put(poolName, threadPoolExecutor);
        return threadPoolExecutor;
    }

    /**
     * 创建线程工厂
     *
     * @param executorName 线程归属名称
     * @param daemon       是否为守护线程
     * @return threadFactory
     */
    private static ThreadFactory createThreadFactory(String executorName, boolean daemon) {
        return r -> new Worker(executorName, daemon, r);
    }

    /**
     * 关闭线程管理器，并且关闭相关的所有线程池
     */
    public void shutdown() {
        workerQueue.shutdown();
        system.shutdown();
        pools.forEach((k, v) -> v.shutdown());
    }

    /**
     * 封装线程
     */
    private static class Worker extends Thread {

        private Worker(String executorName, boolean daemon, Runnable runnable) {
            super(runnable);
            this.setDaemon(daemon);
            this.setName(executorName + "." + this.getId());
        }

        @Override
        public void run() {
            try {
                super.run();
            } catch (Throwable e) {
                log.error(this.getName() + "线程执行异常", e);
            }
        }

    }

//    class ThreadPoolExecutorWrapper extends AbstractExecutorService {
//
//        private final ThreadPoolExecutor executor;
//        private final AtomicInteger running = new AtomicInteger();
//        private volatile Thread shutdownThread;
//
//        ThreadPoolExecutorWrapper(ThreadPoolExecutor executor) {
//            this.executor = executor;
//        }
//
//        @Override
//        public void shutdown() {
//            shutdownThread = Thread.currentThread();
//        }
//
//        @Override
//        public List<Runnable> shutdownNow() {
//            return null;
//        }
//
//        @Override
//        public boolean isShutdown() {
//            return false;
//        }
//
//        @Override
//        public boolean isTerminated() {
//            return false;
//        }
//
//        @Override
//        public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
//            return false;
//        }
//
//        @Override
//        public void execute(Runnable command) {
//            this.executor.execute(command);
//        }
//    }

}
