package thread.threadLocalTransfer;

public class ThreadLocalTransferTest {

    private static final ThreadLocal<String> normalThreadLocal = new ThreadLocal<>();
    private static final InheritableThreadLocal<String> inheritableThreadLocal = new InheritableThreadLocal<>();
    private static final TransferableThreadLocal<String> transferableThreadLocal = new TransferableThreadLocal<>();

    public static void main(String[] args) throws InterruptedException {
        System.out.println("========== ThreadLocal传递对比测试 ==========\n");

        normalThreadLocal.set("普通ThreadLocal的值");
        inheritableThreadLocal.set("InheritableThreadLocal的值");
        transferableThreadLocal.set("TransferableThreadLocal的值");

        System.out.println("【1. 普通ThreadLocal】");
        Thread t1 = new Thread(() -> {
            System.out.println("  异步线程读取: " + normalThreadLocal.get() + " (null，无法传递)");
        });
        t1.start();
        t1.join();

        System.out.println("\n【2. InheritableThreadLocal】");
        Thread t2 = new Thread(() -> {
            System.out.println("  异步线程读取: " + inheritableThreadLocal.get() + " (可以传递)");
        });
        t2.start();
        t2.join();

        System.out.println("\n【3. TransferableThreadLocal + 装饰器】");
        Thread t3 = new Thread(ThreadLocalTransferDecorator.decorate(() -> {
            System.out.println("  异步线程读取: " + transferableThreadLocal.get() + " (可以传递且可控制清理)");
        }));
        t3.start();
        t3.join();
    }
}
