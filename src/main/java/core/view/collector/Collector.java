package core.view.collector;

/**
 * 收集器接口
 *
 * @author zhuchuanji
 */
public abstract class Collector<C> {

    public abstract C collectResults();

    public abstract <V> void set(String field, V value);

    public abstract <V> V get(String field);

}
