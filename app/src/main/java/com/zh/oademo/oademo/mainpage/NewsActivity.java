package com.zh.oademo.oademo.mainpage;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.zh.oademo.oademo.R;

import butterknife.ButterKnife;

public class NewsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        ButterKnife.inject(this);
    }

}
