package com.github.bruce95a.file.share.service;

import com.github.bruce95a.file.share.entity.ShareFile;

import java.util.Map;

public interface FileService {
    boolean saveFile(ShareFile shareFile);

    String getFileName(String uuid);

    int delFile(String uuid);

    boolean rescan();

    Map<String, Object> getFiles(int page, int size);
}
