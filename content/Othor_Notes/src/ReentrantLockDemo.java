package src;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 公平锁Demo
 */
class ReentrantLockDemo implements Runnable {
    public static ReentrantLock fairLock = new ReentrantLock(true);

    @Override
    public void run() {
        while (true) {
            try {
                fairLock.lock();
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName()+" 获到锁");
            }finally{
                fairLock.unlock();    
            }
            
        }
    }

    public static void main(String[] args) {
        ExecutorService
        ReentrantLockDemo reentrantLockDemo = new ReentrantLockDemo(); 
        Thread t1 = new Thread(reentrantLockDemo,"t1");
        Thread t2 = new Thread(reentrantLockDemo,"t2");
        t1.start();
        t2.start();
    }

}