package com.zh.oademo.oademo.entity;


import com.zh.oademo.oademo.common.CardContent;
import com.zh.oademo.oademo.common.CardGenerator;

import java.io.Serializable;

public class ContactContent extends CardContent implements Serializable {

    private String phone;
    private String mobile;

    public ContactContent(String title, String description, String imgUrl, CardGenerator.CARDTYPE cardtype) {
        super(title, description, imgUrl, cardtype);
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
