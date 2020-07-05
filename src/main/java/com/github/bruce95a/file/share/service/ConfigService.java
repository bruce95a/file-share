package com.github.bruce95a.file.share.service;

public interface ConfigService {

    public abstract String getStorePath();

    public abstract boolean setStorePath(String path);

    public abstract String getSiteAddress();

    public abstract boolean setSiteAddress(String address);

    public abstract String getUserName();

    public abstract boolean setUserName(String name);

    public abstract String getUserPassword();

    public abstract boolean setUserPassword(String password);
}
