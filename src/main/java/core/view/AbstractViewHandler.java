package core.view;

/**
 * 抽象视图处理器
 *
 * @author zhuchuanji
 */
public abstract class AbstractViewHandler<P extends ViewParam, D extends ViewData> implements View {

    @Override
    public void run() {
        P viewParam = viewParam();
        D viewData = viewData();
    }

    @Override
    public long getViewId() {
        return 0;
    }

    @Override
    public abstract boolean process();

    @Override
    final public <D extends ViewData> D viewData() {
        return null;
    }

    @Override
    final public <P extends ViewParam> P viewParam() {
        return null;
    }
}
