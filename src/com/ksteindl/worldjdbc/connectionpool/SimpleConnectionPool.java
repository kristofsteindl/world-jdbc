package com.ksteindl.worldjdbc.connectionpool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

    /*  This is a single client, one thread application, single connection is enough in connection pool.
    *   If this application would grow in future (eg calls were expected simultaneously in multiple threads),
    *   INITIAL_POOL_SIZE can be increased, or a more sophisticated ConnectionPool can be implemented.
    *   https://www.baeldung.com/java-connection-pooling
    *
    */

public class SimpleConnectionPool implements ConnectionPool{

    private String url;
    private String user;
    private String password;
    private List<Connection> connectionPool;
    private List<Connection> usedConnections = new ArrayList<>();
    private static int INITIAL_POOL_SIZE = 1;

    public static SimpleConnectionPool create(String url, Properties dbCreds) throws SQLException {
        List<Connection> pool = new ArrayList<>(INITIAL_POOL_SIZE);
        for (int i = 0; i < INITIAL_POOL_SIZE; i++) {
            pool.add(createConnection(url, dbCreds));
        }
        return new SimpleConnectionPool(url, dbCreds.getProperty("user"), dbCreds.getProperty("password"), pool);
    }

    public SimpleConnectionPool(String url, String user, String password, List<Connection> connectionPool) {
        this.url = url;
        this.user = user;
        this.password = password;
        this.connectionPool = connectionPool;
    }

    @Override
    public Connection getConnection() {
        Connection connection = connectionPool
                .remove(connectionPool.size() - 1);
        usedConnections.add(connection);
        return connection;
    }

    @Override
    public boolean releaseConnection(Connection connection) {
        connectionPool.add(connection);
        return usedConnections.remove(connection);
    }

    private static Connection createConnection(String url, Properties dbCreds) throws SQLException {
        return DriverManager.getConnection(url, dbCreds);
    }

    public int getSize() {
        return connectionPool.size() + usedConnections.size();
    }
}
