package src;

/**
 * 线程的 wait和notify
 */
class WaitNotifyDemo {
    final static Object object = new Object();
    int num = 3;

    public synchronized  int get(){
        return 10;
    }

    public synchronized void set(){
        int tempNum = get();
        this.num = tempNum;
        System.out.println(num);
    }

    public static void main(String[] args) {
        // T1 t1 = new T1(object);
        // T2 t2 = new T2(object);
        // t1.start();
        // t2.start();
        WaitNotifyDemo notifyDemo = new WaitNotifyDemo();
        notifyDemo.set();
    }
}

/**
 * 线程1
 */
class T1 extends Thread {

    final Object obj;
    T1(Object obj) {
        this.obj = obj;
    }

    @Override
    public void run() {
        super.run();
        System.out.println("t1 is start");
        synchronized (obj) {
            try {
                System.out.println("t1 is wait");
                // 调用wait会释放obj的锁
                obj.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.println("t1 is end");
            }
        }
    }
}

/**
 * 线程2
 */
class T2 extends Thread {
    final Object obj;

    T2(Object obj) {
        this.obj = obj;
    }

    @Override
    public void run() {
        super.run();
        System.out.println("t2 is start");
        
        synchronized (obj) {
            
            System.out.println("t2 is notify");
            // 调用notify方法只是唤醒其他obj等待队列中的线程，
            // T2线程暂时不会释放obj锁，因为syn块还没走完
            obj.notify();
            System.out.println("t2 is sleep 2s");
            try {
                // 
                sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("t2 is end");
        }
        
    }
}

