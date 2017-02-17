package com.zh.oademo.oademo.util;


public class Page {
    public static int pageCompare(String oldPage, String newPage) {
        return Integer.parseInt(newPage) - Integer.parseInt(oldPage);
    }
}
