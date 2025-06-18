package designpattern.singleton;

/**
 * @Description: 单例模式
 * @Author: mingri31164
 * @Date: 2025/6/18 12:07
 **/
public class SingletonTest {
    public static void main(String[] args) {
        // 创建多个线程，尝试同时获取 SingletonLazy 实例
        Runnable task = () -> {
            SingletonLazy instance = SingletonLazy.getInstance();
            System.out.println(Thread.currentThread().getName() + ": " + instance);
        };

        // 假如不加 synchronized 锁则会出现 instance 哈希码不一致的情况
        for (int i = 0; i < 10; i++) {
            new Thread(task, "Thread-" + i).start();
        }
    }
}



/**
 * 饿汉式（实例在类加载时就被创建）
 * 线程安全：类加载过程是线程安全的

 * 可能会导致实例过早创建，如果实例创建很耗时或占用大量资源，可能会导致类加载时间过长，
 * 而在程序运行初期并不需要该实例，就会造成资源浪费
 **/
class SingletonHungry {
    // 1.私有静态成员变量，在类加载时d就创建实例
    private static SingletonHungry instance = new SingletonHungry();

    // 2.私有构造函数，防止外部通过构造函数创建实例
    private SingletonHungry() {}

    // 3.公共静态方法，用于获取唯一的实例
    public static SingletonHungry getInstance() {
        return instance;
    }
}



/**
 * 懒汉式（第一次调用 getInstance 方法时才创建实例，避免实例过早创建）
 * 线程不安全：创建实例时，可能会被多个线程同时访问进入if语句，导致创建多个实例

 * 加 synchronized 锁解决，但会使效率降低，因为每次调用 getInstance 时都需要获取锁
 **/
class SingletonLazy {
    // 1.私有静态成员变量，初始化为null
    private static SingletonLazy instance = null;

    // 2.私有构造函数，防止外部通过构造函数创建实例
    private SingletonLazy() {}

    // 3.公共静态方法，用于获取唯一的实例
    public static synchronized SingletonLazy getInstance() {
        if (instance == null) {
            instance = new SingletonLazy();
        }
        return instance;
    }
}



/**
 * 双重检查锁（两次检查 instance 是否为 null）
 * 既保证了在第一次需要实例时创建实例，又在一定程度上避免了每次调用 getInstance 都获取锁的情况

 * 由于指令重排序等问题，需要在 instance 变量前添加 volatile 关键字来解决
 **/
class SingletonDoubleCheck {
    // 1.私有静态成员变量，初始化为null
    private volatile static SingletonDoubleCheck instance = null;

    // 2.私有构造函数，防止外部通过构造函数创建实例
    private SingletonDoubleCheck() {}

    // 3.公共静态方法，用于获取唯一的实例
    public static SingletonDoubleCheck getInstance() {
        if (instance == null) {
            synchronized (SingletonDoubleCheck.class) {
                if (instance == null) {
                    instance = new SingletonDoubleCheck();
                }
            }
        }
        return instance;
    }
}



/**
 * 静态内部类（当外部类被加载时，静态内部类不会被加载）
 * 只有当调用 getInstance 方法时，静态内部类才会被加载，此时才创建单例实例

 * 既保证了线程安全，又避免了在不需要实例时过早创建实例
 **/
class SingletonStaticInnerClass {
    // 1.私有构造函数，防止外部通过构造函数创建实例
    private SingletonStaticInnerClass() {}

    // 2.静态内部类，其中包含单例实例
    private static class SingletonHolder {
        private static final SingletonStaticInnerClass instance = new SingletonStaticInnerClass();
    }

    // 3.公共静态方法，用于获取唯一的实例
    public static SingletonStaticInnerClass getInstance() {
        return SingletonHolder.instance;
    }
}
