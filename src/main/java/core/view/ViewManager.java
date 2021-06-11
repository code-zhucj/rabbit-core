package core.view;

import core.config.ConfigManager;
import core.util.CollectionUtils;

import java.util.LinkedHashMap;
import java.util.Map;


public class ViewManager {

    private static Map<Long, ViewCache> viewIndexes = CollectionUtils.newConcurrentHashMap();
    private static Map<Long, ViewHandler> viewHandlerMap = CollectionUtils.newHashMap();

    public static ViewTask getViewTaskById(long uid, long vid) {
        ViewCache viewCache = viewIndexes.get(uid);
        return new ViewTask(viewHandlerMap.get(vid));
    }


    private static class ViewCache {
        Map<Long, ViewData> views = new LinkedHashMap<>();

        private ViewData getViewData(long vid) {
            return views.get(vid);
        }
    }

    private static class ViewTask implements Runnable {

        private ViewHandler viewHandler;

        public ViewTask(ViewHandler viewHandler) {
            this.viewHandler = viewHandler;
        }

        @Override
        public void run() {
            // todo
            try {
                this.viewHandler.process();
            } catch (Throwable throwable) {
                this.viewHandler.throwable(throwable);
                if (ConfigManager.getGlobalConfig().isOpenAlarm()) {
                    ConfigManager.getGlobalConfig().getAlarm().process();
                }
            }
        }
    }
}
