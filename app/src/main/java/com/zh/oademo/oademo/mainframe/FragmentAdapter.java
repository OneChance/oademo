package com.zh.oademo.oademo.mainframe;


import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.zh.oademo.oademo.common.BaseFragment;

import java.util.List;

public class FragmentAdapter extends FragmentPagerAdapter {

    List<BaseFragment> list;

    public FragmentAdapter(FragmentManager fm, List<BaseFragment> list) {
        super(fm);
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public BaseFragment getItem(int position) {
        return list.get(position);
    }
}
