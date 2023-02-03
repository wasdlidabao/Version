package com.tz.version.domain;

public enum ApplicationType {

    khd("客户端"),
    htgl("后台管理"),
    java("java");

    String remark;

    ApplicationType(String remark) {
        this.remark = remark;
    }
}
