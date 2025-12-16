package limit;

import java.time.LocalTime;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 固定窗口限流
 * 会有时间窗口的临界突变问题：1s 中的后 500 ms 和第 2s 的前 500ms 时，虽然是加起来是 1s 时间，却可以被请求 4 次
 */
public class RateLimiterSimpleWindow {
    // 阈值
    private static Integer QPS = 2;
    // 时间窗口（毫秒）
    private static long TIME_WINDOWS = 1000;
    // 计数器
    private static AtomicInteger REQ_COUNT = new AtomicInteger();

    private static long START_TIME = System.currentTimeMillis();

    public synchronized static boolean tryAcquire() {
        if ((System.currentTimeMillis() - START_TIME) > TIME_WINDOWS) {
            REQ_COUNT.set(0);
            START_TIME = System.currentTimeMillis();
        }
        return REQ_COUNT.incrementAndGet() <= QPS;
    }

    public static void main(String[] args) throws InterruptedException {
        // 先休眠 400ms，可以更快的到达时间窗口（测试时间窗口临界突变）。
//        Thread.sleep(400);
        for (int i = 0; i < 10; i++) {
            Thread.sleep(250);
            LocalTime now = LocalTime.now();
            if (!tryAcquire()) {
                System.out.println(now + " 被限流");
            } else {
                System.out.println(now + " 执行业务");
            }
        }
    }
}
