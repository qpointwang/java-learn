import java.util.concurrent.Semaphore;

public class SemaphoreDemo {

    Semaphore semaphore = new Semaphore(2);

    public static void main(String[] args) {
        SemaphoreDemo semaphoreDemo = new SemaphoreDemo();
        new Thread(()->semaphoreDemo.acquire(),"线程A").start();
        new Thread(()->semaphoreDemo.acquire(),"线程B").start();
        new Thread(()->semaphoreDemo.acquire(),"线程c").start();
    }

    public void acquire() {
        try {
            System.out.println(Thread.currentThread().getName() + " 获取信号量");
            this.semaphore.acquire();
            System.out.println(Thread.currentThread().getName() + " 获得到信号量");
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            this.semaphore.release();
            System.out.println(Thread.currentThread().getName() + " 释放信号量");
        }
    }
}
