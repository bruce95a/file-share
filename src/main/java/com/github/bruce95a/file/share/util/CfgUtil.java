package com.github.bruce95a.file.share.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CfgUtil {
    private static final Logger logger = LoggerFactory.getLogger(CfgUtil.class);
    // 是否配置了用户
    private static boolean userConfigured = false;
    // 是否配置了保存目录
    private static boolean pathConfigured = false;
    // 是否配置了网址
    private static boolean siteConfigured = false;

    public static boolean isUserInit() {
        if (userConfigured) {
            return true;
        }

        try {
            SQLiteUtil sqLiteUtil = SQLiteUtil.getInstance();
            ResultSet rs = sqLiteUtil.select("SELECT value FROM config WHERE name = 'userName';");
            if (rs != null && rs.next()) {
                userConfigured = true;
            }
        } catch (SQLException e) {
            logger.error("=== SQL ERR ===", e);
        }
        return userConfigured;
    }

    public static boolean isPathInit() {
        if (pathConfigured) {
            return true;
        }
        try {
            SQLiteUtil sqLiteUtil = SQLiteUtil.getInstance();
            ResultSet rs = sqLiteUtil.select("SELECT value FROM config WHERE name = 'storePath';");
            if (rs != null && rs.next()) {
                String path = rs.getString("value");
                if (new File(path).exists()) {
                    pathConfigured = true;
                }
            }
        } catch (SQLException e) {
            logger.error("=== SQL ERR ===", e);
        }
        return pathConfigured;
    }

    public static boolean isSiteInit() {
        if (siteConfigured) {
            return true;
        }
        try {
            SQLiteUtil sqLiteUtil = SQLiteUtil.getInstance();
            ResultSet rs = sqLiteUtil.select("SELECT value FROM config WHERE name = 'siteAddress';");
            if (rs != null && rs.next()) {
                siteConfigured = true;
            }
        } catch (SQLException e) {
            logger.error("=== SQL ERR ===", e);
        }
        return siteConfigured;
    }
}
