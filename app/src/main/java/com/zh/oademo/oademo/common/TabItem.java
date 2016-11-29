package com.zh.oademo.oademo.common;


public class TabItem {
    private int icon;
    private int text;
    private int color;

    public TabItem(int icon, int text, int color) {
        this.icon = icon;
        this.text = text;
        this.color = color;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getText() {
        return text;
    }

    public void setText(int text) {
        this.text = text;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
