package com.zh.oademo.oademo.common;


import com.ashokvarma.bottomnavigation.BadgeItem;

public class TabItem {
    private int icon;
    private int text;
    private int color;
    private BadgeItem numberBadgeItem;

    public TabItem(int icon, int text, int color, int number) {
        this.icon = icon;
        this.text = text;
        this.color = color;

        if (number > 0) {
            this.numberBadgeItem = new BadgeItem()
                    .setBorderWidth(2)
                    .setText("" + number)
                    .setHideOnSelect(true);
        }
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

    public BadgeItem getNumberBadgeItem() {
        return numberBadgeItem;
    }

    public void setNumberBadgeItem(BadgeItem numberBadgeItem) {
        this.numberBadgeItem = numberBadgeItem;
    }
}
