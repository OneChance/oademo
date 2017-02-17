package com.zh.oademo.oademo.util;


public class Formatter {
    public static String removePointString(Object object) {
        if (object != null) {
            if (object.toString().contains(".")) {
                return object.toString().split("\\.")[0];
            } else {
                return object.toString();
            }
        }
        return "";
    }

    public static int removePointInt(Object object) {
        if (object != null) {
            try {
                if (object.toString().contains(".")) {
                    return Integer.parseInt(object.toString().split("\\.")[0]);
                } else {
                    return Integer.parseInt(object.toString());
                }
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }
}
