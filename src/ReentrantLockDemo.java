import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
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
    private static final ReentrantLock lock = new ReentrantLock(true);
    private static Condition condition1 = lock.newCondition();
    private static Condition condition2 = lock.newCondition();
    private static int a = 1;

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
        // testForTryLock();
        // CAS();


        new Thread(() -> testForCondition1(), "线程1").start();
        new Thread(() -> testForCondition2(), "线程2").start();
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

    // CAS
    public static void CAS() {
        AtomicInteger index = new AtomicInteger(10);

        if (index.compareAndSet(10, 11)) {
            System.out.println(index.get()); // 11
        } else {
            System.out.println("失败");
        }

        index.decrementAndGet();
        System.out.println(index.get()); // 10
        System.out.println(index.getAndSet(13)); // 10
        System.out.println(index.get()); // 13
        index.incrementAndGet();
    }

    // condition
    public static void testForCondition1() {
        for (int i = 0; i < 10; i++) {
            try {
                lock.lock();

                while (a == 1) {
                    condition1.await();
                }
                System.out.println(Thread.currentThread().getName() + " ");
                a = 1;
                condition2.signal();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }


    }

    // condition
    public static void testForCondition2() {
        for (int i = 0; i < 10; i++) {
            try {
                lock.lock();

                while (a == 2) {
                    condition2.await();
                }
                lock.lock();
                System.out.println(Thread.currentThread().getName());
                System.out.println("是否被锁 " + lock.isLocked());
                System.out.println("当前线程保持此锁的次数，也就是执行此线程执行lock方法的次数 " + lock.getHoldCount());
                System.out.println("返回正等待获取此锁的线程估计数 " + lock.getQueueLength());
                lock.unlock();
                a = 2;
                condition1.signal();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
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
