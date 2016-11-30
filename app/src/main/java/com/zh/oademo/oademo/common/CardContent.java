package com.zh.oademo.oademo.common;


import java.io.Serializable;

public class CardContent implements Serializable {
    String title;
    String description;
    String imgUrl;
    CardGenerator.CARDTYPE cardtype;

    public CardContent(String title, String description, String imgUrl, CardGenerator.CARDTYPE cardtype) {
        this.title = title;
        this.description = description;
        this.imgUrl = imgUrl;
        this.cardtype = cardtype;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public CardGenerator.CARDTYPE getCardtype() {
        return cardtype;
    }

    public void setCardtype(CardGenerator.CARDTYPE cardtype) {
        this.cardtype = cardtype;
    }
}
