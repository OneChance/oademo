package com.zh.oademo.oademo.mainframe;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.zh.oademo.oademo.MyApplication;
import com.zh.oademo.oademo.R;
import com.zh.oademo.oademo.common.BaseFragment;
import com.zh.oademo.oademo.common.TabItem;
import com.zh.oademo.oademo.mainpage.MainPageFragment;
import com.zh.oademo.oademo.worktodo.WorktodoFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainPageActivity extends AppCompatActivity
        implements BottomNavigationBar.OnTabSelectedListener {


    @InjectView(R.id.bottom_navigation_bar)
    BottomNavigationBar bottomNavigationBar;


    List<BaseFragment> fragments = new ArrayList<>();
    int lastSelectedPosition = 0;
    FragmentManager fm;

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

        fragments.add(new MainPageFragment());
        fragments.add(new WorktodoFragment());

        setDefaultFragment();
    }

    public List<TabItem> getTabs() {
        List<TabItem> tabs = new ArrayList<>();
        tabs.add(new TabItem(R.mipmap.ic_home_white_24dp, R.string.tab_item_home, R.color.colorPrimary, 0));
        tabs.add(new TabItem(R.mipmap.ic_book_white_24dp, R.string.tab_item_worktodo, R.color.colorPrimary, 3));
        tabs.add(new TabItem(R.mipmap.ic_music_note_white_24dp, R.string.tab_item_flows, R.color.colorPrimary, 0));
        tabs.add(new TabItem(R.mipmap.ic_tv_white_24dp, R.string.tab_item_contact, R.color.colorPrimary, 0));
        return tabs;
    }

    public void setTabs(List<TabItem> tabs) {

        for (TabItem item : tabs) {
            bottomNavigationBar.addItem(new BottomNavigationItem(item.getIcon(), item.getText()).setActiveColorResource(item.getColor()).setBadgeItem(item.getNumberBadgeItem()));
        }

        bottomNavigationBar.setFirstSelectedPosition(lastSelectedPosition > (getTabs().size() - 1) ? (getTabs().size() - 1) : lastSelectedPosition)
                .initialise();
    }

    private void setDefaultFragment() {
        FragmentTransaction transaction = fm.beginTransaction();

        for (int i = 0; i < fragments.size(); i++) {
            transaction.add(R.id.layFrame, fragments.get(i));
            if (i == 0) {
                transaction.show(fragments.get(i));
            } else {
                transaction.hide(fragments.get(i));
            }
        }

        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onTabSelected(int position) {
        if (fragments != null) {
            if (position < fragments.size()) {
                FragmentTransaction transaction = fm.beginTransaction();
                //transaction.hide(fragments.get(MyApplication.mainPageTab)).show(fragments.get(position)).commit();

                transaction.replace(R.id.layFrame, fragments.get(position)).commit();

                MyApplication.mainPageTab = position;
            }
        }
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
