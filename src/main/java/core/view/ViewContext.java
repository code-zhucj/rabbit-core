package core.view;

/**
 * ViewContext
 *
 * @author zhuchuanji
 * @date 2021/3/2
 */
public class ViewContext {

    private ViewParam viewParam;

    private long targetViewId;

    private long clientId;

    public ViewParam getViewParam() {
        return viewParam;
    }

    public long getTargetViewId() {
        return targetViewId;
    }

    public long getClientId() {
        return clientId;
    }
}
