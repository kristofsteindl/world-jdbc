package com.ksteindl.worldjdbc.connectionpool;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionPoolFactory {

    private static final String DB_PROPERTIES_PATH= "world1-db.properties";
    private static final String DEFAULT_DB_PROPERTIES_PATH= "/default-world1-db.properties";

    private static Properties dbProperties;
    private static ConnectionPool connectionPool;

    public static ConnectionPool getConnectionPool() {
        if (dbProperties == null) {
            dbProperties = getDbProperties();
        }
        if (connectionPool == null) {
            try {
                connectionPool = SimpleConnectionPool.create(getDbUrl(), getDbCreds());
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        }
        return connectionPool;
    }


    private static String getDbUrl() {
        String url =
                "jdbc:" + dbProperties.getProperty("driver") +
                        "://" + dbProperties.getProperty("url") +
                        ":" + dbProperties.getProperty("port") +
                        "/" + dbProperties.getProperty("dbName");
        return url;
    }

    private static Properties getDbCreds() {
        Properties info = new Properties();
        info.setProperty("user", dbProperties.getProperty("user"));
        info.setProperty("password", dbProperties.getProperty("password"));
        return info;
    }

    private static Properties getDbProperties() {
        try {
            Properties defaultProperties = getDefaultProperties();
            Properties customProperties = getCustomProperties();
            return overloadProperties(defaultProperties, customProperties);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

    }

    private static Properties overloadProperties(Properties... propertieses) {
        Properties mergedProperties = new Properties();
        for (Properties properties : propertieses) {
            mergedProperties.putAll(properties);
        }
        return mergedProperties;
    }

    private static Properties getCustomProperties() throws IOException {
        Properties customProperties = new Properties();
        try (var customPropsInputStream = new FileInputStream(DB_PROPERTIES_PATH)) {
            customProperties.load(customPropsInputStream);
        }
        return customProperties;
    }

    private static Properties getDefaultProperties() throws IOException {
        Properties defaultProperties = new Properties();
//        try (InputStream input = Controller.class.getResourceAsStream(DEFAULT_DB_PROPERTIES_PATH)) {
//            bundeldProperties.load(input);
//        }
        return defaultProperties;
    }
}
