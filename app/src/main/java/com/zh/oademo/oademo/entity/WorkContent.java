package com.zh.oademo.oademo.entity;


import com.zh.oademo.oademo.common.CardContent;
import com.zh.oademo.oademo.common.CardGenerator;

import java.io.Serializable;

public class WorkContent extends CardContent implements Serializable {

    private String workType;
    private String workitemId;
    private String url;

    public WorkContent(String title, String description, String imgUrl, CardGenerator.CARDTYPE cardtype) {
        super(title, description, imgUrl, cardtype);
    }

    public String getWorkitemId() {
        return workitemId;
    }

    public void setWorkitemId(String workitemId) {
        this.workitemId = workitemId;
    }

    public String getWorkType() {
        return workType;
    }

    public void setWorkType(String workType) {
        this.workType = workType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
