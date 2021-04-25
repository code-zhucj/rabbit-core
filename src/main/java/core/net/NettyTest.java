package core.net;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Netty test
 */
public class NettyTest {

    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("properties/log4j2.xml");
        ConfigurationSource source = new ConfigurationSource(new FileInputStream(file), file);
        Configurator.initialize(null, source);
        Logger logger = LogManager.getLogger(NettyTest.class);
        logger.debug("debug");
        logger.info("info");
        logger.warn("warn");
        logger.error("error");
    }
}
