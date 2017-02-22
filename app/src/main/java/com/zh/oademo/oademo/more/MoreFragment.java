package com.zh.oademo.oademo.more;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.zh.oademo.oademo.R;
import com.zh.oademo.oademo.common.BaseFragment;
import com.zh.oademo.oademo.mainframe.MainPageActivity;
import com.zh.oademo.oademo.sign.LoginActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MoreFragment extends BaseFragment {

    @InjectView(R.id.sign_out_button)
    Button bSignout;

    MainPageActivity activity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more, null);

        ButterKnife.inject(this, view);

        activity = (MainPageActivity) getActivity();

        bSignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = activity.getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("userid", "");
                editor.putString("passwordMD5", "");
                editor.apply();

                activity.startActivity(new Intent(activity, LoginActivity.class));
                activity.finish();
            }
        });

        return view;
    }
}
