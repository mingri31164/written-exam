package thread.threadLocalTransfer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;

public class TransferableThreadLocal<T> extends InheritableThreadLocal<T> {

    private static final ThreadLocal<Map<ThreadLocal<?>, Object>> THREAD_LOCAL_MAP =
            ThreadLocal.withInitial(WeakHashMap::new);

    @Override
    protected T initialValue() {
        T value = super.initialValue();
        Map<ThreadLocal<?>, Object> map = THREAD_LOCAL_MAP.get();
        map.put(this, value);
        return value;
    }

    @Override
    public void set(T value) {
        super.set(value);
        Map<ThreadLocal<?>, Object> map = THREAD_LOCAL_MAP.get();
        map.put(this, value);
    }

    @Override
    public void remove() {
        super.remove();
        Map<ThreadLocal<?>, Object> map = THREAD_LOCAL_MAP.get();
        map.remove(this);
    }

    public static Map<ThreadLocal<?>, Object> getTlMap() {
        return THREAD_LOCAL_MAP.get();
    }

    public static void clear(Thread mainThread) {
        if (!Objects.equals(Thread.currentThread(), mainThread)) {
            Map<ThreadLocal<?>, Object> map = THREAD_LOCAL_MAP.get();
            //防止迭代器遍历过程中删除元素导致ConcurrentModificationException
            List<Map.Entry<ThreadLocal<?>, Object>> list = new ArrayList<>(map.entrySet());
            list.forEach(entry -> entry.getKey().remove());
            map.clear();
            THREAD_LOCAL_MAP.remove();
        }
    }
}