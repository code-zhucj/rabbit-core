package core.view;

import core.exception.ViewException;
import core.util.CollectionUtils;

import java.util.Map;

public class ViewManager {

    private static Map<Long, View> allView = CollectionUtils.newConcurrentHashMap();

    public static View getViewById(long id) {
        View view = allView.get(id);
        if(view == null){
            throw new ViewException("视图不存在");
        }
        return allView.get(id);
    }
}
