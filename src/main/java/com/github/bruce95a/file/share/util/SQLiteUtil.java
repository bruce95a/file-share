package com.github.bruce95a.file.share.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayDeque;
import java.util.Deque;

public class SQLiteUtil {
    private static final Logger logger = LoggerFactory.getLogger(SQLiteUtil.class);

    private static SQLiteUtil su;

    private final Deque<Connection> connectionPool;

    private SQLiteUtil(Integer poolSize) throws SQLException {
        // init pool container
        if (null == poolSize || poolSize <= 0) {
            throw new RuntimeException("error input 'poolSize', it must be larger than 0.");
        } else {
            connectionPool = new ArrayDeque<>(poolSize);
        }

        for (int i = 0; i < poolSize; i++) {
            Connection c = DriverManager.getConnection("jdbc:sqlite:data.db");
            c.setAutoCommit(false);
            connectionPool.add(c);
        }
    }

    public static SQLiteUtil getInstance() throws SQLException {
        if (null == su) {
            su = new SQLiteUtil(5);
        }
        return su;
    }

    private synchronized Connection getConn() {
        return connectionPool.removeLast();
    }

    private void releaseConn(Connection c) {
        connectionPool.addLast(c);
    }

    private Statement getStatement(Connection con) {
        try {
            return con.createStatement();
        } catch (SQLException e) {
            logger.error("=== SQL ERR ===", e);
            return null;
        }
    }

    public ResultSet select(String sql) {
        Connection c = this.getConn();
        Statement stm = this.getStatement(c);
        if (stm == null) return null;
        try {
            return stm.executeQuery(sql);
        } catch (SQLException e) {
            logger.error("=== SQL ERR ===", e);
        } finally {
            this.releaseConn(c);
        }
        return null;
    }

    public Integer update(String sql) {
        Connection c = this.getConn();
        Statement stm = this.getStatement(c);
        if (stm == null) return null;
        try {
            return stm.executeUpdate(sql);
        } catch (SQLException e) {
            logger.error("=== SQL ERR ===", e);
            return 0;
        } finally {
            try {
                c.commit();
            } catch (SQLException e) {
                logger.error("=== SQL ERR ===", e);
            }
            this.releaseConn(c);
        }
    }
}
