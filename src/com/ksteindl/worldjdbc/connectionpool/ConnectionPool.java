package com.ksteindl.worldjdbc.connectionpool;

import java.sql.Connection;

public interface ConnectionPool {

    Connection getConnection();
    boolean releaseConnection(Connection connection);
}
