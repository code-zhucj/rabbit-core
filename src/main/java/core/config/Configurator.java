package core.config;

import core.module.Module;
import org.apache.logging.log4j.core.config.ConfigurationSource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Configurator implements Module {
    @Override
    public void init() {

    }

    @Override
    public void execute() {
        logConfig();
    }

    private void logConfig() {
        try {
            File file = new File("properties/log4j2.xml");
            ConfigurationSource source = new ConfigurationSource(new FileInputStream(file), file);
            org.apache.logging.log4j.core.config.Configurator.initialize(null, source);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void destroy() {

    }
}
