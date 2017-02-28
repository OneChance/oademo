package com.zh.oademo.oademo.net;

import android.content.Context;

import com.zh.oademo.oademo.MyApplication;
import com.zh.oademo.oademo.R;
import com.zh.oademo.oademo.entity.NetObject;


public class NetObserver implements rx.Observer<NetObject> {

    public Context context;
    private DataReceiver receiver;
    private int code;

    public NetObserver(Context context, DataReceiver receiver, int code) {
        this.context = context;
        this.receiver = receiver;
        this.code = code;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        MyApplication.toast(MyApplication.getResString(R.string.net_error) + ":" + e.getMessage(), false);
        receiver.error();
    }


    @Override
    public void onNext(NetObject netObject) {
        if (netObject.getSuccess().equals("true")) {
            receiver.handle(netObject.getData(), code);
        } else {
            MyApplication.toast(netObject.getMessage(), false);
            receiver.error();
        }
    }

    public interface DataReceiver {
        void handle(Object data, int code);

        void error();
    }
}
