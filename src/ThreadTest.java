import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class ThreadByThreadDemo extends Thread {

    @Override
    public void run() {
        System.out.println("Thread " + Thread.currentThread().getName());
    }


}

class ThreadByRunnableDemo implements Runnable {

    @Override
    public void run() {
        System.out.println("Runnable " + Thread.currentThread().getName());
    }
}

class ThreadByCallableDemo implements Callable<Person> {

    @Override
    public Person call() throws Exception {
        System.out.println("Callable " + Thread.currentThread().getName());
        Thread.sleep(1000);
        return new Person("qpointwang", 25);
    }
}


class ThreadForSynchronizedDemo implements Runnable {

    Lock lock = new ReentrantLock();
    private Integer number = 0;

    @Override
    public void run() {
//        try {
//            lock.lock();
//            setNumber(number);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            lock.unlock();
//        }

        // this 锁住的是当前对象
        synchronized (this) {
            setNumber();
        }


//        setNumber();
        // System.out.println("Synchronized Runnable " + Thread.currentThread().getName() + " " + number);
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber() {
        this.number = number + 1;
    }
}


public class ThreadTest {

    public static void main(String[] args) {
        /*ThreadByThreadDemo threadByThread = new ThreadByThreadDemo();
        threadByThread.start();
        threadByThread.run();

        ThreadByRunnableDemo threadByRunnableDemo = new ThreadByRunnableDemo();
        Thread threadByRunnable = new Thread(threadByRunnableDemo);
        threadByRunnable.start();
        threadByRunnable.run();

        ThreadByCallableDemo threadByCallableDemo = new ThreadByCallableDemo();
        ExecutorService threadPool = Executors.newSingleThreadExecutor();
        Future<Person> future = threadPool.submit(threadByCallableDemo);
        try {
            // Future的get()方法获取结果时，当前线程就会阻塞，知道call()方法返回结果
            System.out.println(future.get());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            threadPool.shutdown();
        }
        try {
            System.out.println(threadByCallableDemo.call());
        } catch (Exception e) {
            e.printStackTrace();
        }

        threadPool = Executors.newSingleThreadExecutor();
        // 线程池提交runnable
        Future result = threadPool.submit(threadByRunnableDemo);
        System.out.println(result);
        threadPool.shutdown(); */

        ThreadForSynchronizedDemo threadForSynchronizedDemo = new ThreadForSynchronizedDemo();
        ExecutorService service = Executors.newFixedThreadPool(50);
        for (int i = 0; i < 100000; i++) {

            service.submit(threadForSynchronizedDemo);
//            Thread threadForSynchronized = new Thread(threadForSynchronizedDemo);
//            threadForSynchronized.start();
        }
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(threadForSynchronizedDemo.getNumber());
        service.shutdown();
    }
}


class Solution1 {
    List<String> res = new LinkedList<>();
    int num = 0;
    int m = 4;
    char[] c;
    public String[] permutation(String s) {
        c = s.toCharArray();
        dfs(0);
        return res.toArray(new String[res.size()]);
    }

    void dfs(int x) {
        if (x == c.length - 1) {
            if (c[0] != '0' && Integer.parseInt(String.valueOf(c)) % m == 0) {
                res.add(String.valueOf(c)); // 添加排列方案
                num++;
            }
            return;
        }
        HashSet<Character> set = new HashSet<>();
        for (int i = x; i < c.length; i++) {
            if (set.contains(c[i])) continue; // 重复，因此剪枝
            set.add(c[i]);
            swap(i, x); // 交换，将 c[i] 固定在第 x 位
            dfs(x + 1); // 开启固定第 x + 1 位字符
            swap(i, x); // 恢复交换
        }
    }
    void swap(int a, int b) {
        char tmp = c[a];
        c[a] = c[b];
        c[b] = tmp;
    }
    public static void main(String[] args) {
        Solution1 solution1 = new Solution1();
        solution1.permutation("97284");
        // System.out.println(solution1.res);
        System.out.println(solution1.num);
    }
}
