package test;


import core.thread.ThreadManager;
import core.thread.WorkerQueueThreadPoolExecutor;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Consumer;
import java.util.function.Function;

public class Main {

    private static final int MAX_USER = 100;

    private static final int MAX_TASK = 100;

    public static void main(String[] args) {
        long lockCostTime = executeTime(Main::lockTask, createUsers());
        long noLockCostTime = executeTime(Main::noLockTask, createUsers());
        printResult(lockCostTime, noLockCostTime);
    }

    private static void noLockTask(Map<Integer, User> users) {
        WorkerQueueThreadPoolExecutor workerQueueThreadPool = ThreadManager.getInstance().getWorkerQueueThreadPool();
        List<Runnable> usersTasks = createUsersTasks(NoLockTask::new, users);
        for (Runnable r : usersTasks) {
            workerQueueThreadPool.commit(((NoLockTask) r).getId(), r);
        }
        workerQueueThreadPool.shutdown();
        print(users);
    }

    private static void printResult(long lockCostTime, long noLockCostTime) {
        System.out.println();
        System.out.println("noLock 执行耗时：" + noLockCostTime + " ms");
        System.out.println("lock 执行耗时：" + lockCostTime + " ms");
        float percent =
                (float) ((noLockCostTime > lockCostTime ? (double) noLockCostTime / lockCostTime :
                        (double) lockCostTime / noLockCostTime) - 1D) * 100;
        String fast = noLockCostTime < lockCostTime ? "noLock" : "lock";
        String slow = noLockCostTime < lockCostTime ? "lock" : "noLock";
        System.out.println(fast + " 效率比 " + slow + " 提高了 " + percent + " %");
        System.out.println();
    }


    private static void lockTask(Map<Integer, User> users) {
        ThreadPoolExecutor systemThreadPool = ThreadManager.getInstance().getSystemThreadPool();
        List<Runnable> usersTasks = createUsersTasks(LockTask::new, users);
        usersTasks.forEach(systemThreadPool::execute);
        systemThreadPool.shutdown();
        while (true) {
            if (systemThreadPool.isTerminated()) {
                break;
            }
        }
        print(users);
    }

    private static long executeTime(Consumer<Map<Integer, User>> consumer, Map<Integer, User> users) {
        long start = System.currentTimeMillis();
        consumer.accept(users);
        long end = System.currentTimeMillis();
        return end - start;
    }

    private static void print(Map<Integer, User> users) {
        users.values().forEach(v -> {
            if (v.count == MAX_TASK) {
                System.out.println(v);
            }
        });
    }

    private static List<Runnable> createUsersTasks(Function<User, Runnable> function, Map<Integer, User> users) {
        List<Runnable> userTasks = new LinkedList<>();
        for (int i = 0; i < MAX_USER; i++) {
            int id = i + 1;
            for (int j = 0; j < MAX_TASK; j++) {
                userTasks.add(function.apply(users.get(id)));
            }
        }
        return userTasks;
    }

    private static Map<Integer, User> createUsers() {
        Map<Integer, User> users = new HashMap<>();
        for (int i = 0; i < MAX_USER; i++) {
            users.put(i + 1, new User(i + 1, 0));
        }
        return users;
    }

    public static void sleep() {
        long sleep = 10;
        try {
            Thread.sleep(sleep);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
