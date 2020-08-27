import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @anthor qpointwang
 * @date 2020/8/27 14:38
 */
public class ReentrantLockDemo {
    //    非公平锁 默认构建的锁fair = false，公平锁是关闭的
//    private static final Lock lock = new ReentrantLock();
//    公平锁
    private static final Lock lock = new ReentrantLock(true);
    private static int a = 0;

    public static void main(String[] args) {
//        new Thread(() -> test(), "线程A").start();
//        new Thread(() -> test(), "线程B").start();
//
//        new Thread(() -> testForFairLock(), "线程1").start();
//        new Thread(() -> testForFairLock(), "线程2").start();
//        new Thread(() -> testForFairLock(), "线程3").start();
//        new Thread(() -> testForFairLock(), "线程4").start();
//        new Thread(() -> testForFairLock(), "线程5").start();
//        new Thread(() -> testForFairLock(), "线程6").start();

        // testForInterrupted();
        testForTryLock();
    }

    public static void test() {
        lock.lock();
        for (int i = 0; i < 100000; i++) {
            a++;
        }
        System.out.println(Thread.currentThread().getName() + " " + a);
        lock.unlock();
    }

    public static void testForFairLock() {
        for (int i = 0; i < 2; i++) {
            try {
                lock.lock();
                System.out.println(Thread.currentThread().getName() + "获得锁");
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }

    // 响应中断
    public static void testForInterrupted() {
        Lock firstLock = new ReentrantLock();
        Lock secondLock = new ReentrantLock();
        Thread firstThread = new Thread(new ThreadDemo(firstLock, secondLock));
        Thread secondThread = new Thread(new ThreadDemo(secondLock, firstLock));
        firstThread.start();
        secondThread.start();
        firstThread.interrupt();
    }

    // 限时等待
    public static void testForTryLock() {
        Lock firstLock = new ReentrantLock();
        Lock secondLock = new ReentrantLock();
        Thread firstThread = new Thread(new ThreadDemo(firstLock, secondLock));
        Thread secondThread = new Thread(new ThreadDemo(secondLock, firstLock));
        firstThread.start();
        secondThread.start();
    }

}


class ThreadDemo implements Runnable {
    Lock firstLock;
    Lock secondLock;

    ThreadDemo(Lock firstLock, Lock secondLock) {
        this.firstLock = firstLock;
        this.secondLock = secondLock;
    }

//    @Override
//    public void run() {
//        try {
//            firstLock.lockInterruptibly();
//            System.out.println(Thread.currentThread().getName() + "获得到锁" + firstLock.toString());
//            Thread.sleep(5000);
//            secondLock.lockInterruptibly();
//            System.out.println(Thread.currentThread().getName() + "获得到锁" + secondLock.toString());
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } finally {
//            firstLock.unlock();
//            secondLock.unlock();
//            System.out.println(Thread.currentThread().getName() + " " + firstLock.toString() + " " + secondLock.toString() + "正常结束");
//        }
//    }

    @Override
    public void run() {
        try {
            if (firstLock.tryLock(2, TimeUnit.SECONDS)) {
                System.out.println(Thread.currentThread().getName() + "获得到锁" + firstLock.toString());
                Thread.sleep(5000);
            }
            if (secondLock.tryLock(2, TimeUnit.SECONDS)) {
                System.out.println(Thread.currentThread().getName() + "获得到锁" + secondLock.toString());
            } else {
                System.out.println("没");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            firstLock.unlock();
            secondLock.unlock();
            System.out.println(Thread.currentThread().getName() + " " + firstLock.toString() + " " + secondLock.toString() + "正常结束");
        }
    }
}
