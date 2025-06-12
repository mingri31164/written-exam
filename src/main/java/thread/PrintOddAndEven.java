package thread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 两个线程循环打印1-100，线程1打印奇数，线程2打印偶数
 */
public class PrintOddAndEven {

    private static int flag = 1;
    Lock lock = new ReentrantLock();
    Condition conditionEven = lock.newCondition();
    Condition conditionOdd = lock.newCondition();

    public void printEven(){
        lock.lock();
        try {
            while(flag % 2 != 0){
                conditionEven.await();
            }
            System.out.print(flag+" ");
            conditionOdd.signal();
            flag ++;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
    public void printOdd(){
        lock.lock();
        try {
            while(flag % 2 != 1){
                conditionOdd.await();
            }
            System.out.print(flag+" ");
            conditionEven.signal();
            flag ++;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        PrintOddAndEven printOddAndEven = new PrintOddAndEven();
        Thread thread1 = new Thread(()->{
            while (flag < 100) printOddAndEven.printEven();
        });
        Thread thread2 = new Thread(()->{
            while (flag < 100) printOddAndEven.printOdd();
        });
        thread1.start();
        thread2.start();
    }
}
