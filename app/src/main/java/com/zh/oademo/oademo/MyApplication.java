package com.zh.oademo.oademo;


import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;

public class MyApplication extends Application {
    public static String VERSION = "1.0";
    private static List<Activity> mList = new LinkedList();
    public static int mainPageTab = 0;
    private static Context context;

    private static MyApplication instance;

    @Override
    public void onCreate() {
        context = getApplicationContext();
        super.onCreate();
    }

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


    public static void customToast(String msg, boolean success) {
        View toastView = LayoutInflater.from(context).inflate(R.layout.toast, null);
        Toast toast = new Toast(context);
        TextView textView = (TextView) toastView.findViewById(R.id.toast_notice);
        if (success) {
            textView.setBackgroundColor(ContextCompat.getColor(context,R.color.success));
        } else {
            textView.setBackgroundColor(ContextCompat.getColor(context,R.color.error));
        }
        textView.setText(msg);
        toast.setView(toastView);
        toast.setDuration(Toast.LENGTH_SHORT);

        toast.show();
    }

    public static void toast(String msg, boolean success) {
        customToast(msg, success);
    }

    public static void toast(int resId, boolean success) {
        String msg = context.getResources().getString(resId);
        customToast(msg, success);
    }

    public static String getResString(int resId) {
        return context.getResources().getString(resId);
    }

}
