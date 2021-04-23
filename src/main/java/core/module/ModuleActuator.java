package core.module;

import core.net.NetworkEngine;

/**
 * @description: 模块执行器
 * @author: zhuchuanji
 * @create: 2021-01-02 00:34
 */
public class ModuleActuator {

    public void start() {
        // 注解解析器
        // 网络引擎
        NetworkEngine.getInstance().start();
    }

}
