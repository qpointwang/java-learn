
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
                // 调用某个对象的wait()方法能让当前线程阻塞，并且当前线程必须拥有此对象的monitor
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
            person.notify();
            // 唤醒所有当前对象上的等待线程
            // person.notifyAll();
        }

    }
}
