package com.zh.oademo.oademo.entity;


public class NetObject {
    private Object data;
    private String errorCode;
    private String fromCache;
    private String message;
    private String success;
    private String updateTime;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getFromCache() {
        return fromCache;
    }

    public void setFromCache(String fromCache) {
        this.fromCache = fromCache;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }
}
