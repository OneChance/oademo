package com.zh.oademo.oademo.net;

import android.content.Context;
import android.util.Log;

import com.zh.oademo.oademo.MyApplication;
import com.zh.oademo.oademo.R;
import com.zh.oademo.oademo.entity.NetObject;


public class NetObserver implements rx.Observer<NetObject> {

    public Context context;
    DataReceiver receiver;

    public NetObserver(Context context, DataReceiver receiver) {
        this.context = context;
        this.receiver = receiver;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        Log.d("oademo", "error");
        MyApplication.toast(MyApplication.getResString(R.string.net_error) + ":" + e.getMessage(), false);
        receiver.error();
    }


    @Override
    public void onNext(NetObject netObject) {

        Log.d("oademo", "next" + netObject.getSuccess());

        if (netObject.getSuccess().equals("true")) {
            receiver.handle(netObject.getData());
        } else {
            MyApplication.toast(netObject.getMessage(), false);
            receiver.error();
        }
    }

    public interface DataReceiver {
        void handle(Object data);

        void error();
    }
}