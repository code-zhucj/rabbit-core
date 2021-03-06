package core.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description: 集合工具类
 * @author: zhuchuanji
 * @create: 2021-01-02 01:24
 */
public class CollectionUtils {

    private static final float DEFAULT_LOAD_FACTOR = 0.75F;

    private static final float MAP_DEFAULT_SIZE = 16;

    public static <T> boolean isEmpty(Collection<T> collection) {
        return collection == null || collection.isEmpty();
    }

    public static <T> boolean isNotEmpty(Collection<T> collection) {
        return !isEmpty(collection);
    }

    public static <K, V> boolean isEmpty(Map<K, V> map) {
        return map == null || map.isEmpty();
    }

    public static <K, V> boolean isNotEmpty(Map<K, V> map) {
        return !isEmpty(map);
    }

    public static <K, V> HashMap<K, V> newHashMap() {
        return new HashMap<>((int) (MAP_DEFAULT_SIZE / DEFAULT_LOAD_FACTOR) + 1);
    }

    public static <K, V> HashMap<K, V> newHashMap(int size) {
        return new HashMap<>((int) (size / DEFAULT_LOAD_FACTOR) + 1);
    }

    public static <K, V> ConcurrentHashMap<K, V> newConcurrentHashMap() {
        return new ConcurrentHashMap<>((int) (MAP_DEFAULT_SIZE / DEFAULT_LOAD_FACTOR) + 1);
    }

    public static <K, V> ConcurrentHashMap<K, V> newConcurrentHashMap(int size) {
        return new ConcurrentHashMap<>((int) (size / DEFAULT_LOAD_FACTOR) + 1);
    }
}
