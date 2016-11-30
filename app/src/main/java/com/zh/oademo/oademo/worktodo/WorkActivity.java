package com.zh.oademo.oademo.worktodo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.zh.oademo.oademo.MyApplication;
import com.zh.oademo.oademo.R;
import com.zh.oademo.oademo.common.WorkContent;

import butterknife.ButterKnife;

public class WorkActivity extends AppCompatActivity {

    WorkContent workContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work);

        ButterKnife.inject(this);

        Bundle bundle = this.getIntent().getExtras();
        workContent = (WorkContent) bundle.getSerializable("workContent");

        ((MyApplication) getApplication()).getInstance().addActivity(this);
    }
}
