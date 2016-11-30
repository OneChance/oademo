package com.zh.oademo.oademo;


import android.app.Activity;
import android.app.Application;

import java.util.LinkedList;
import java.util.List;

public class MyApplication extends Application {
    private static Integer VERSION = 1;
    private static List<Activity> mList = new LinkedList();
    public static int mainPageTab = 0;

    private static MyApplication instance;

    public MyApplication getInstance() {
        if (null == instance) {
            instance = new MyApplication();
        }
        return instance;
    }

    public void addActivity(Activity activity) {
        mList.add(activity);
    }

    public void exit() {
        try {
            for (Activity activity : mList) {
                if (activity != null)
                    activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }
}
