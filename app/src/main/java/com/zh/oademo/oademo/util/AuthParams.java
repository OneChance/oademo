package com.zh.oademo.oademo.util;


import android.content.SharedPreferences;
import android.util.Log;

import java.security.MessageDigest;
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

        Log.d("oademo","passwordMD5:"+passwordMD5);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String now = sdf.format(new Date());
        params.put("m_timestamp", now);

        String authT =  getMD5Code((Long.parseLong(now) * 3 - 99) + passwordMD5.toLowerCase()).toLowerCase();
        params.put("m_auth_t", authT);

        return params;
    }

    public static String getMD5Code(String strObj) {
        String resultString = null;
        try {
            resultString = new String(strObj);
            MessageDigest md = MessageDigest.getInstance("MD5");
            return bytesToHex(md.digest(strObj.getBytes("utf-8")));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return resultString;
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        int t;
        for (int i = 0; i < 16; i++) {// 16 == bytes.length;
            t = bytes[i];
            if (t < 0)
                t += 256;
            sb.append(hexDigits[(t >>> 4)]);
            sb.append(hexDigits[(t % 16)]);
        }
        return sb.toString();
    }

    private final static char[] hexDigits = { '0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
}
