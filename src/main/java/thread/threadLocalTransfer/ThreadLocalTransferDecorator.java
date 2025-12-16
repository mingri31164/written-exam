package thread.threadLocalTransfer;

import java.util.HashMap;
import java.util.Map;

public class ThreadLocalTransferDecorator {

    public static Runnable decorate(Runnable runnable) {
        return new ThreadLocalTransferRunnable(runnable);
    }

    private static class ThreadLocalTransferRunnable implements Runnable {

        private Runnable runnable;
        private Map<ThreadLocal, Object> context;
        private Thread mainThread;

        ThreadLocalTransferRunnable(Runnable runnable) {
            this.runnable = runnable;
            mainThread = Thread.currentThread();
            Map<ThreadLocal<?>, Object> tlMap = TransferableThreadLocal.getTlMap();
            context = new HashMap<>(tlMap);
        }

        private void contextTransfer() {
            context.forEach(ThreadLocal::set);
            context = null;
        }

        @Override
        public void run() {
            contextTransfer();
            try {
                runnable.run();
            } finally {
                TransferableThreadLocal.clear(mainThread);
            }
        }
    }
}