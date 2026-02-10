package thread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 三个线程循环打印ABC，线程一打印A，线程二打印B，线程三打印C，打印10遍即可
 * 这个问题主要是考察怎么使用线程同步的问题，通常情况下我们有多种解法，
 * 可以使用Condition，Synchronize，semaphore等等，这里使用 ReentrantLock 来解答这个问题
 */
public class PrintABC_Cycle_Condition {

    private static int state = 0; // 控制打印顺序
    private static Lock lock = new ReentrantLock();
    private static Condition conditionA = lock.newCondition();
    private static Condition conditionB = lock.newCondition();
    private static Condition conditionC = lock.newCondition();

    public static void main(String[] args) {

        new Thread(() -> print("A", 0, conditionA, conditionB)).start();
        new Thread(() -> print("B", 1, conditionB, conditionC)).start();
        new Thread(() -> print("C", 2, conditionC, conditionA)).start();

    }

    private static void  print(String content, int targetState, Condition current, Condition next){
        for(int i = 0; i < 10; i++){
            lock.lock();
            try {
                while (state % 3 != targetState){
                    current.await();
                }
                System.out.print(content);
                state++;
                next.signal();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                lock.unlock();
            }
        }
    }

}
