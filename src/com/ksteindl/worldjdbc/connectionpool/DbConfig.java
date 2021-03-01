package com.ksteindl.worldjdbc.connectionpool;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class DbConfig {

    private static final String DB_PROPERTIES_PATH= "world1-db.properties";
    private static final String DEFAULT_DB_PROPERTIES_PATH= "/default-world1-db.properties";

    private Properties dbProperties;

    public DbConfig() {
        this.dbProperties = getDbProperties();
    }

    public String getDbUrl() {
        String url =
                "jdbc:" + dbProperties.getProperty("driver") +
                        "://" + dbProperties.getProperty("url") +
                        ":" + dbProperties.getProperty("port") +
                        "/" + dbProperties.getProperty("dbName");
        return url;
    }

    public Properties getDbUsernamePassword() {
        Properties info = new Properties();
        info.setProperty("user", dbProperties.getProperty("user"));
        info.setProperty("password", dbProperties.getProperty("password"));
        return info;
    }

    private Properties getDbProperties() {
        try {
            Properties defaultProperties = getDefaultProperties();
            Properties customProperties = getCustomProperties();
            return overloadProperties(defaultProperties, customProperties);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

    }

    private Properties overloadProperties(Properties... propertieses) {
        Properties mergedProperties = new Properties();
        for (Properties properties : propertieses) {
            mergedProperties.putAll(properties);
        }
        return mergedProperties;
    }

    private Properties getCustomProperties() throws IOException {
        Properties customProperties = new Properties();
        try (var customPropsInputStream = new FileInputStream(DB_PROPERTIES_PATH)) {
            customProperties.load(customPropsInputStream);
        }
        return customProperties;
    }

    private Properties getDefaultProperties() throws IOException {
        Properties defaultProperties = new Properties();
//        try (InputStream input = Controller.class.getResourceAsStream(DEFAULT_DB_PROPERTIES_PATH)) {
//            bundeldProperties.load(input);
//        }
        return defaultProperties;
    }
}
