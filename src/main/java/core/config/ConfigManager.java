package core.config;

public final class ConfigManager {

    private GlobalConfig globalConfig;

    private static final ConfigManager INSTANCE = new ConfigManager();

    public static ConfigManager getInstance() {
        return INSTANCE;
    }

    private ConfigManager() {
        throw new UnsupportedOperationException();
    }

    public static GlobalConfig getGlobalConfig() {
        return INSTANCE.globalConfig;
    }

}
