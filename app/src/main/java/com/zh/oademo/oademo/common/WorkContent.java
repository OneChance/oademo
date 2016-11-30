package com.zh.oademo.oademo.common;


public class WorkContent extends CardContent{

    private String workitemId;

    public WorkContent(String title, String description, String imgUrl, CardGenerator.CARDTYPE cardtype, String workitemId) {
        super(title, description, imgUrl, cardtype);
        this.workitemId = workitemId;
    }

    public String getWorkitemId() {
        return workitemId;
    }

    public void setWorkitemId(String workitemId) {
        this.workitemId = workitemId;
    }
}
