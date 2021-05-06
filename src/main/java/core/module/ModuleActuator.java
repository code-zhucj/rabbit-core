package core.module;

import core.config.Configurator;
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
        new Configurator().start();
        NetworkEngine.getInstance().start();
    }

}
