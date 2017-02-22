package com.zh.oademo.oademo.mainframe;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.zh.oademo.oademo.MyApplication;
import com.zh.oademo.oademo.R;
import com.zh.oademo.oademo.common.BaseFragment;
import com.zh.oademo.oademo.common.TabItem;
import com.zh.oademo.oademo.contact.ContactFragment;
import com.zh.oademo.oademo.info.InfoFragment;
import com.zh.oademo.oademo.more.MoreFragment;
import com.zh.oademo.oademo.work.WorkFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainPageActivity extends AppCompatActivity
        implements BottomNavigationBar.OnTabSelectedListener {


    @InjectView(R.id.bottom_navigation_bar)
    BottomNavigationBar bottomNavigationBar;

    int lastSelectedPosition = 0;
    FragmentManager fm;
    WorkFragment workFragment;
    InfoFragment infoFragment;
    ContactFragment contactFragment;
    MoreFragment moreFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_frame);
        ButterKnife.inject(this);
        initView();

        ((MyApplication) getApplication()).getInstance().addActivity(this);
    }

    private void initView() {

        bottomNavigationBar.setTabSelectedListener(this);
        bottomNavigationBar.clearAll();
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);

        setTabs(getTabs());

        fm = getSupportFragmentManager();

        workFragment = new WorkFragment();
        infoFragment = new InfoFragment();
        contactFragment = new ContactFragment();
        moreFragment = new MoreFragment();

        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.layFrame, workFragment);

        transaction.commit();

    }

    public List<TabItem> getTabs() {
        List<TabItem> tabs = new ArrayList<>();
        tabs.add(new TabItem(R.mipmap.tabbar_home, R.string.tab_item_work, R.color.colorPrimary, 0));
        tabs.add(new TabItem(R.mipmap.tabbar_message, R.string.tab_item_info, R.color.colorPrimary, 0));
        tabs.add(new TabItem(R.mipmap.tabbar_profile, R.string.tab_item_contact, R.color.colorPrimary, 0));
        tabs.add(new TabItem(R.mipmap.tabbar_more, R.string.tab_item_more, R.color.colorPrimary, 0));
        return tabs;
    }

    public void setTabs(List<TabItem> tabs) {

        for (TabItem item : tabs) {
            bottomNavigationBar.addItem(new BottomNavigationItem(item.getIcon(), item.getText()).setActiveColorResource(item.getColor()).setBadgeItem(item.getNumberBadgeItem()));
        }

        bottomNavigationBar.setFirstSelectedPosition(lastSelectedPosition > (getTabs().size() - 1) ? (getTabs().size() - 1) : lastSelectedPosition)
                .initialise();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onTabSelected(int position) {

        BaseFragment currentFragment = getCurrentFragment(position);

        if (currentFragment != null) {
            MyApplication.mainPageTab = position;
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.replace(R.id.layFrame, currentFragment).commit();
        }
    }

    public BaseFragment getCurrentFragment(int position) {
        switch (position) {
            case 0:
                return workFragment;
            case 1:
                return infoFragment;
            case 2:
                return contactFragment;
            case 3:
                return moreFragment;
        }
        return null;
    }

    @Override
    protected void onResume() {

        bottomNavigationBar.selectTab(MyApplication.mainPageTab);

        super.onResume();
    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }
}
