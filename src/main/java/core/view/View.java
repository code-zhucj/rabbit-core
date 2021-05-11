package core.view;

/**
 * View
 *
 * @author zhuchuanji
 * @date 2021/3/2
 */
public interface View extends Runnable {

    long getViewId();

    boolean process();

    <D extends ViewData> D viewData();

    <P extends ViewParam> P viewParam();
}
