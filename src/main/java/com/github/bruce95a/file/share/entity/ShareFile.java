package com.github.bruce95a.file.share.entity;

import java.io.Serializable;

public class ShareFile implements Serializable {
    private String uuid;
    private String name;
    private String datetime;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
}
