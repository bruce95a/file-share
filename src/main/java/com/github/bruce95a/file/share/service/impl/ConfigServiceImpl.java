package com.github.bruce95a.file.share.service.impl;

import com.github.bruce95a.file.share.mapper.CfgMapper;
import com.github.bruce95a.file.share.service.ConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class ConfigServiceImpl implements ConfigService {
    private static final Logger logger = LoggerFactory.getLogger(ConfigServiceImpl.class);

    @Autowired
    private CfgMapper dao;

    @Override
    public String getStorePath() {
        String storePath = dao.selectValue("storePath");
        if (storePath != null && !storePath.endsWith(File.separator)) {
            storePath += File.separator;
        }
        return storePath;
    }

    @Override
    public boolean setStorePath(String path) {
        return dao.updateValue("storePath", path) > 0;
    }

    @Override
    public String getSiteAddress() {
        return dao.selectValue("siteAddress");
    }

    @Override
    public boolean setSiteAddress(String address) {
        return dao.updateValue("siteAddress", address) > 0;
    }

    @Override
    public String getUserName() {
        return dao.selectValue("userName");
    }

    @Override
    public boolean setUserName(String name) {
        return dao.updateValue("userName", name) > 0;
    }

    @Override
    public String getUserPassword() {
        return dao.selectValue("userPassword");
    }

    @Override
    public boolean setUserPassword(String password) {
        return dao.updateValue("userPassword", password) > 0;
    }
}
