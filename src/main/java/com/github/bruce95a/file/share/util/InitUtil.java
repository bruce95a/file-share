package com.github.bruce95a.file.share.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.SQLException;

@Component
public class InitUtil {
    private static final Logger logger = LoggerFactory.getLogger(InitUtil.class);

    @PostConstruct
    public void init() {
        /*
        初始化表
         */
        try {
            SQLiteUtil sqLiteUtil = SQLiteUtil.getInstance();
            Integer cCount = sqLiteUtil.update("CREATE TABLE IF NOT EXISTS config (name TEXT PRIMARY KEY, value TEXT NOT NULL);");
            logger.info("config表创建" + cCount);

            Integer rCount = sqLiteUtil.update("CREATE TABLE IF NOT EXISTS report (uuid TEXT PRIMARY KEY, name TEXT UNIQUE NOT NULL, datetime DATETIME, last DATETIME);");
            logger.info("report表创建" + rCount);
            Integer iCount = sqLiteUtil.update("CREATE INDEX IF NOT EXISTS index_report_1 ON report (datetime);");
            logger.info("report表INDEX创建" + iCount);
        } catch (SQLException e) {
            logger.error("=== SQL ===", e);
        }
    }
}
