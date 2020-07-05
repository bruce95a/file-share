package com.github.bruce95a.file.share.service.impl;

import com.github.bruce95a.file.share.entity.ShareFile;
import com.github.bruce95a.file.share.service.ConfigService;
import com.github.bruce95a.file.share.service.FileService;
import com.github.bruce95a.file.share.util.SQLiteUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class FileServiceImpl implements FileService {
    private static final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Autowired
    private ConfigService configService;

    /**
     * 保存报告文件信息到数据库
     *
     * @param shareFile 报告信息
     * @return 是否成功
     */
    @Override
    public boolean saveFile(ShareFile shareFile) {
        if (shareFile != null) {
            try {
                SQLiteUtil sqLiteUtil = SQLiteUtil.getInstance();
                String udtSql = String.format("UPDATE report SET last = NULL WHERE name = '%s';", shareFile.getName());
                if (sqLiteUtil.update(udtSql) > 0) {
                    return true;
                }
                String uuid = UUID.randomUUID().toString().replaceAll("-", "");
                String storePath = configService.getStorePath();
                File file = new File(String.format("%s%s", storePath, shareFile.getName()));
                String datetime = "";
                Path filePath = file.toPath();
                BasicFileAttributes attributes = null;
                try {
                    attributes = Files.readAttributes(filePath, BasicFileAttributes.class);
                } catch (IOException e) {
                    logger.error("获取文件属性出错", e);
                }
                if (attributes != null) {
                    Date date = new Date(attributes.lastModifiedTime().toMillis());
                    datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
                }
                String sql = String.format("INSERT INTO report (uuid, name, datetime) values ('%s', '%s', '%s');", uuid, shareFile.getName(), datetime);
                if (sqLiteUtil.update(sql) > 0) {
                    return true;
                }
            } catch (SQLException e) {
                logger.error("=== SQL ERR ===", e);
            }
        }
        return false;
    }

    /**
     * 通过uuid获取文件名
     *
     * @param uuid 文件的uuid
     * @return 文件名
     */
    @Override
    public String getFileName(String uuid) {
        String fileName = "";
        String sql = String.format("SELECT name FROM report WHERE uuid = '%s';", uuid);
        try {
            SQLiteUtil sqLiteUtil = SQLiteUtil.getInstance();
            ResultSet rs = sqLiteUtil.select(sql);
            if (rs.next()) {
                fileName = rs.getString("name");
            }
        } catch (SQLException e) {
            logger.error("=== SQL ERR ===", e);
        }
        return fileName;
    }

    @Override
    public boolean delFile(String uuid) {
        String sql = String.format("DELETE FROM report WHERE uuid = '%s';", uuid);
        Integer count = 0;
        try {
            SQLiteUtil sqLiteUtil = SQLiteUtil.getInstance();
            count = sqLiteUtil.update(sql);
        } catch (SQLException e) {
            logger.error("=== SQL ERR ===", e);
        }
        return count > 0;
    }

    /**
     * 扫描目录下的文件，更新数据库
     *
     * @return 是否成功
     */
    @Override
    public boolean rescan() {
        String storePath = configService.getStorePath();
        if (storePath == null) {
            return false;
        }
        File dir = new File(storePath);
        if (!dir.exists()) {
            return false;
        }
        if (!dir.isDirectory()) {
            return false;
        }
        final String updateDt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        try {
            SQLiteUtil sqLiteUtil = SQLiteUtil.getInstance();
            Integer udtCount = sqLiteUtil.update(String.format("UPDATE report SET last = '%s';", updateDt));
            logger.info("标记报告数量" + udtCount);
            File[] files = dir.listFiles();
            int count = 0;
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        ShareFile shareFile = new ShareFile();
                        shareFile.setName(file.getName());
                        if (saveFile(shareFile)) {
                            count++;
                        }
                    }
                }
            }
            logger.info("添加或更新报告数量" + count);
            String delSql = String.format("DELETE FROM report WHERE last = '%s';", updateDt);
            Integer delCount = sqLiteUtil.update(delSql);
            logger.info("删除报告数量" + delCount);
        } catch (SQLException e) {
            logger.error("=== SQL ERR ===", e);
        }
        return true;
    }

    @Override
    public Map<String, Object> getFiles(String page, String fileName, String sdt, String edt) {
        Map<String, Object> map = new HashMap<>();
        List<ShareFile> result = new ArrayList<>();
        int offset = 0;
        if (page != null && !"".equals(page)) {
            offset = (Integer.parseInt(page) - 1) * 10;
        }
        final String sqlTplCount = "SELECT count(*) as amt FROM report WHERE 1 = 1 %s;";
        final String sqlTpl = "SELECT uuid, name, datetime FROM report WHERE 1 = 1 %s ORDER BY datetime DESC LIMIT %s, 10;";
        String condition = "";
        if (!"".equals(fileName)) {
            condition = String.format("%s AND name like '%%%s%%'", condition, fileName);
        }
        if (!"".equals(sdt)) {
            condition = String.format("%s AND datetime > '%s'", condition, sdt);
        }
        if (!"".equals(edt)) {
            condition = String.format("%s AND datetime < '%s'", condition, edt);
        }
        String sqlCount = String.format(sqlTplCount, condition);
        String sql = String.format(sqlTpl, condition, offset);
        try {
            SQLiteUtil sqLiteUtil = SQLiteUtil.getInstance();
            ResultSet rsAmt = sqLiteUtil.select(sqlCount);
            if (rsAmt.next()) {
                map.put("count", rsAmt.getInt("amt"));
            }
            ResultSet rs = sqLiteUtil.select(sql);
            while (rs.next()) {
                ShareFile shareFile = new ShareFile();
                shareFile.setUuid(rs.getString("uuid"));
                shareFile.setName(rs.getString("name"));
                shareFile.setDatetime(rs.getString("datetime"));
                result.add(shareFile);
            }
            if (result.size() > 0) {
                map.put("list", result);
            }
        } catch (SQLException e) {
            logger.error("=== SQL ERR ===", e);
        }
        return map;
    }
}
