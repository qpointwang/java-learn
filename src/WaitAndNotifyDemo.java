
/**
 * @anthor qpointwang
 * @date 2020/8/27 21:04
 */
public class WaitAndNotifyDemo {

    // wait()、notify()和notifyAll()方法是本地方法，并且为final方法，无法被重写。
    Person person = new Person("qpointwang", 25);

    public static void main(String[] args) {
        WaitAndNotifyDemo waitAndNotifyDemo = new WaitAndNotifyDemo();
        new Thread(() -> waitAndNotifyDemo.test1(), "wait1").start();
        new Thread(() -> waitAndNotifyDemo.test1(), "wait2").start();
        new Thread(() -> waitAndNotifyDemo.test2(), "notify").start();
    }

    public void test1() {
        try {
            // 在线线程中调用wait方法的时候 要用synchronized锁住对象，确保代码段不会被多个线程调用
            synchronized (person) {
                System.out.println(Thread.currentThread().getName()+" "+"is waiting");
                // Thread.sleep 不会让出对象锁，在sleep期间不允许其他线程访问person代码块
                Thread.sleep(2000);
                // 调用某个对象的wait()方法能让当前线程阻塞，并且当前线程必须拥有此对象的monitor
                // wait()会让出对象锁，wait后其他线程可以访问person代码块
                person.wait(5000);
                System.out.println(Thread.currentThread().getName()+" "+"is notified");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void test2() {
        synchronized (person) {
            // 唤醒一个当前对象上的等待线程
            System.out.println("开始唤醒");
            person.notify();
            System.out.println("成功唤醒");
            // 唤醒所有当前对象上的等待线程
            // person.notifyAll();
        }

    }
}
