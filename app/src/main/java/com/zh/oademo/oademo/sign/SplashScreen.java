package com.zh.oademo.oademo.sign;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.zh.oademo.oademo.MyApplication;
import com.zh.oademo.oademo.R;
import com.zh.oademo.oademo.mainframe.MainPageActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SplashScreen extends AppCompatActivity {

    @InjectView(R.id.app_version)
    TextView appVersion;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.splashscreen);

        ButterKnife.inject(this);

        appVersion.setText(("v" + MyApplication.VERSION));

        new Handler().postDelayed(new Runnable() {
            public void run() {

                Intent to;

                SharedPreferences sharedPreferences = getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
                String userid = sharedPreferences.getString("userid", "");
                if (userid.equals("")) {
                    to = new Intent(SplashScreen.this, LoginActivity.class);
                } else {
                    to = new Intent(SplashScreen.this, MainPageActivity.class);
                }

                SplashScreen.this.startActivity(to);
                SplashScreen.this.finish();
            }
        }, 2000);
    }
}
