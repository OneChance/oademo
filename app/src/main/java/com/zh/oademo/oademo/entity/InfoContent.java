package com.zh.oademo.oademo.entity;


import com.zh.oademo.oademo.common.CardContent;
import com.zh.oademo.oademo.common.CardGenerator;

import java.io.Serializable;

public class InfoContent extends CardContent implements Serializable {

    private String infoID;
    private String url;
    private String infoType;

    public InfoContent(String title, String description, String imgUrl, CardGenerator.CARDTYPE cardtype) {
        super(title, description, imgUrl, cardtype);
    }

    public String getInfoID() {
        return infoID;
    }

    public void setInfoID(String infoID) {
        this.infoID = infoID;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getInfoType() {
        return infoType;
    }

    public void setInfoType(String infoType) {
        this.infoType = infoType;
    }
}
