package com.github.bruce95a.file.share.service.impl;

import com.github.bruce95a.file.share.service.ConfigService;
import com.github.bruce95a.file.share.util.SQLiteUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;

@Service
public class ConfigServiceImpl implements ConfigService {
    private static final Logger logger = LoggerFactory.getLogger(ConfigServiceImpl.class);

    @Override
    public String getStorePath() {
        String storePath = "";
        try {
            SQLiteUtil sqLiteUtil = SQLiteUtil.getInstance();
            ResultSet rs = sqLiteUtil.select("SELECT value FROM config WHERE name = 'storePath';");
            if (rs != null && rs.next()) {
                storePath = rs.getString("value");
                if (storePath != null && !storePath.endsWith(File.separator)) {
                    storePath += File.separator;
                }
            }
        } catch (SQLException e) {
            logger.error("=== SQL ERR ===", e);
        }
        return storePath;
    }

    @Override
    public boolean setStorePath(String path) {
        String udtSql = String.format("UPDATE config SET value = '%s' WHERE name = 'storePath';", path);
        Integer count = 0;
        try {
            SQLiteUtil sqLiteUtil = SQLiteUtil.getInstance();
            if (sqLiteUtil.update(udtSql) > 0) {
                return true;
            }
            String sql = String.format("INSERT INTO config (name, value) values ('storePath', '%s');", path);
            count = sqLiteUtil.update(sql);
        } catch (SQLException e) {
            logger.error("=== SQL ERR ===", e);
        }
        return count > 0;
    }

    @Override
    public String getSiteAddress() {
        String siteAddress = null;
        try {
            SQLiteUtil sqLiteUtil = SQLiteUtil.getInstance();
            ResultSet rs = sqLiteUtil.select("SELECT value FROM config WHERE name = 'siteAddress';");
            if (rs.next()) {
                siteAddress = rs.getString("value");
            }
        } catch (SQLException e) {
            logger.error("=== SQL ERR ===", e);
        }
        return siteAddress;
    }

    @Override
    public boolean setSiteAddress(String address) {
        String udtSql = String.format("UPDATE config SET value = '%s' WHERE name = 'siteAddress';", address);
        Integer count = 0;
        try {
            SQLiteUtil sqLiteUtil = SQLiteUtil.getInstance();
            if (sqLiteUtil.update(udtSql) > 0) {
                return true;
            }
            String sql = String.format("INSERT INTO config (name, value) values ('siteAddress', '%s');", address);
            count = sqLiteUtil.update(sql);
        } catch (SQLException e) {
            logger.error("=== SQL ERR ===", e);
        }
        return count > 0;
    }

    @Override
    public String getUserName() {
        String userName = "";
        try {
            SQLiteUtil sqLiteUtil = SQLiteUtil.getInstance();
            ResultSet rs = sqLiteUtil.select("SELECT value FROM config WHERE name = 'userName';");
            if (rs.next()) {
                userName = rs.getString("value");
            }
        } catch (SQLException e) {
            logger.error("=== SQL ERR ===", e);
        }
        return userName;
    }

    @Override
    public boolean setUserName(String name) {
        String udtSql = String.format("UPDATE config SET value = '%s' WHERE name = 'userName';", name);
        Integer count = 0;
        try {
            SQLiteUtil sqLiteUtil = SQLiteUtil.getInstance();
            if (sqLiteUtil.update(udtSql) > 0) {
                return true;
            }
            String sql = String.format("INSERT INTO config (name, value) values ('userName', '%s');", name);
            count = sqLiteUtil.update(sql);
        } catch (SQLException e) {
            logger.error("=== SQL ERR ===", e);
        }
        return count > 0;
    }

    @Override
    public String getUserPassword() {
        String userPassword = null;
        try {
            SQLiteUtil sqLiteUtil = SQLiteUtil.getInstance();
            ResultSet rs = sqLiteUtil.select("SELECT value FROM config WHERE name = 'userPassword';");
            if (rs.next()) {
                userPassword = rs.getString("value");
            }
        } catch (SQLException e) {
            logger.error("=== SQL ERR ===", e);
        }
        return userPassword;
    }

    @Override
    public boolean setUserPassword(String password) {
        String udtSql = String.format("UPDATE config SET value = '%s' WHERE name = 'userPassword';", password);
        Integer count = 0;
        try {
            SQLiteUtil sqLiteUtil = SQLiteUtil.getInstance();
            if (sqLiteUtil.update(udtSql) > 0) {
                return true;
            }
            String sql = String.format("INSERT INTO config (name, value) values ('userPassword', '%s');", password);
            count = sqLiteUtil.update(sql);
        } catch (SQLException e) {
            logger.error("=== SQL ERR ===", e);
        }
        return count > 0;
    }
}
