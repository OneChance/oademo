package com.zh.oademo.oademo.util;


import android.content.SharedPreferences;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AuthParams {
    public static Map<String, String> getPaams(SharedPreferences sharedPreferences) {

        Map<String, String> params = new HashMap<>();

        String userid = sharedPreferences.getString("userid", "");
        params.put("userid", userid);

        String passwordMD5 = sharedPreferences.getString("passwordMD5", "");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String now = sdf.format(new Date());
        params.put("m_timestamp", now);

        String authT = "";

        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
            md.update(((Long.parseLong(now) * 3 - 99) + passwordMD5).getBytes());
            authT = new BigInteger(1, md.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        params.put("m_auth_t", authT);

        return params;
    }
}
