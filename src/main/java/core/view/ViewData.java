package core.view;

import core.unique.UniqueManager;
import core.view.collector.Collector;

import java.util.HashSet;
import java.util.Set;


public abstract class ViewData implements View {

    private long userId;

    protected Collector<?> collector;

    private static ViewConfig viewConfig;

    private Set<Long> registered = new HashSet<>();

    public Set<Long> getRegistered() {
        return registered;
    }

    public void registered(long userId) {
        registered.add(userId);
    }

    public void cancel(long userId) {
        registered.remove(userId);
    }

    @Override
    public long getViewId() {
        return UniqueManager.vid(this.getClass().getSimpleName());
    }

    public long getViewHandlerId() {
        return viewConfig.getViewHandlerId();
    }
}
