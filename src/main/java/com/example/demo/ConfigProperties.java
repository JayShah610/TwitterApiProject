package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;


public class ConfigProperties {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigProperties.class);
    private static final Properties PROPERTIES;
    private static final String PROP_FILE = "application.properties";

    private ConfigProperties() {
    }

    static {
        PROPERTIES = new Properties();
        final URL props = ClassLoader.getSystemResource(PROP_FILE);
        try {
            PROPERTIES.load(props.openStream());
        } catch (IOException ex) {

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(ex.getClass().getName() + "PropertiesReader method");
            }
        }
    }
    public static String getProperty(final String name) {
        return PROPERTIES.getProperty(name);
    }

}
