package core.unique;

import core.util.CollectionUtils;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 唯一名管理器
 */
public class UniqueManager {

    private static final AtomicLong UID = new AtomicLong();
    private static final AtomicLong VID = new AtomicLong();
    private static final AtomicLong CHANNEL_ID = new AtomicLong();
    private static final Map<String, Long> VID_MAP = CollectionUtils.newConcurrentHashMap();


    public static long generateUid() {
        return UID.getAndIncrement();
    }

    public static long generateVid() {
        return VID.getAndIncrement();
    }

    public static long generateChannelId() {
        return CHANNEL_ID.getAndIncrement();
    }

    public static long vid(String viewName) {
        return VID_MAP.computeIfAbsent(viewName, v -> generateVid());
    }

}
