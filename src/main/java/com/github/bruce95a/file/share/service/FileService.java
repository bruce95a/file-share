package com.github.bruce95a.file.share.service;

import com.github.bruce95a.file.share.entity.ShareFile;

import java.util.Map;

public interface FileService {
    public abstract boolean saveFile(ShareFile shareFile);

    public abstract String getFileName(String uuid);

    public abstract boolean delFile(String uuid);

    public abstract boolean rescan();

    public abstract Map<String, Object> getFiles(String page, String fileName, String sdt, String edt);
}
