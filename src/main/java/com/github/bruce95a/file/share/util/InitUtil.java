package com.github.bruce95a.file.share.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class InitUtil {
    private static final Logger logger = LoggerFactory.getLogger(InitUtil.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void init() {
        /*
        初始化表
         */
        logger.info("init database");
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS config (name TEXT PRIMARY KEY, value TEXT NOT NULL)");
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS report (uuid TEXT PRIMARY KEY, name TEXT UNIQUE NOT NULL, datetime DATETIME, last DATETIME)");
        jdbcTemplate.execute("CREATE INDEX IF NOT EXISTS index_report_1 ON report (datetime)");
        List<String> value = jdbcTemplate.queryForList("SELECT value FROM config", String.class);
        if (value.size() == 0) {
            jdbcTemplate.execute("INSERT INTO config(name, value) VALUES('storePath', './')");
            jdbcTemplate.execute("INSERT INTO config(name, value) VALUES('siteAddress', 'localhost:8080')");
            jdbcTemplate.execute("INSERT INTO config(name, value) VALUES('userName', 'admin')");
            jdbcTemplate.execute("INSERT INTO config(name, value) VALUES('userPassword', 'admin')");
        }
    }
}
