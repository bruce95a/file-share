package com.github.bruce95a.file.share.service.impl;

import com.github.bruce95a.file.share.entity.ShareFile;
import com.github.bruce95a.file.share.mapper.FileMapper;
import com.github.bruce95a.file.share.service.ConfigService;
import com.github.bruce95a.file.share.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class FileServiceImpl implements FileService {
    private static final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Autowired
    private ConfigService configService;

    @Autowired
    private FileMapper dao;

    /**
     * 保存报告文件信息到数据库
     *
     * @param shareFile 报告信息
     * @return 是否成功
     */
    @Override
    public boolean saveFile(ShareFile shareFile) {
        if (shareFile != null) {
            if (dao.updateName(shareFile) > 0) {
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
            shareFile.setUuid(uuid);
            shareFile.setDatetime(datetime);
            return dao.insert(shareFile) > 0;
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
        return dao.selectName(uuid);
    }

    @Override
    public boolean delFile(String uuid) {
        return dao.delete(uuid) > 0;
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
        int udtCount = dao.updateLast(updateDt);
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
        int delCount = dao.deleteLast(updateDt);
        logger.info("删除报告数量" + delCount);
        return true;
    }

    @Override
    public Map<String, Object> getFiles(String page) {
        Map<String, Object> map = new HashMap<>();
        int offset = 0;
        if (page != null && !"".equals(page)) {
            offset = (Integer.parseInt(page) - 1) * 10;
        }
        map.put("count", dao.selectAllCount());
        map.put("list", dao.selectAll(offset));
        return map;
    }
}
