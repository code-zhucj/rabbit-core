package core.view.collector;

import java.util.List;

/**
 * 帧收集器
 *
 * @author zhuchuanji
 */
public class FrameCollector<T> extends Collector<List<T>>{


    @Override
    public List<T> collectResults() {
        return null;
    }

    @Override
    public <V> void set(String field, V value) {

    }

    @Override
    public <V> V get(String field) {
        return null;
    }


}
