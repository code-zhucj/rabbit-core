package core.view.collector;

import core.util.CollectionUtils;
import core.view.ViewData;

import java.util.Map;

/**
 * 状态收集器
 */
public class StatusCollector extends Collector<Map<String, ?>> {

    private Map<String, ?> collector = CollectionUtils.newHashMap();

    private ViewData viewData;

    public StatusCollector(ViewData viewData) {
        this.viewData = viewData;
    }

    @Override
    public Map<String, ?> collectResults() {
        return collector;
    }

    @Override
    public <V> void set(String field, V value) {

    }

    @Override
    public <V> V get(String field) {
        return (V) collector.get(field);
    }


}
