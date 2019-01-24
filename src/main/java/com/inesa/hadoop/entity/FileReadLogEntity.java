package com.inesa.hadoop.entity;

import com.inesa.common.entity.BaseEntity;

public class FileReadLogEntity extends BaseEntity {

    private String app;
    private String service;
    private String name;

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
