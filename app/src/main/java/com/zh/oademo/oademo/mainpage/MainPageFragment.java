package com.zh.oademo.oademo.mainpage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.listeners.RecyclerItemClickListener;
import com.dexafree.materialList.view.MaterialListView;
import com.zh.oademo.oademo.R;
import com.zh.oademo.oademo.common.BaseFragment;
import com.zh.oademo.oademo.common.CardContent;
import com.zh.oademo.oademo.common.CardGenerator;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainPageFragment extends BaseFragment implements NavigationView.OnNavigationItemSelectedListener {

    @InjectView(R.id.news)
    MaterialListView news;
    @InjectView(R.id.refresh_component)
    SwipeRefreshLayout refreshComponent;
    @InjectView(R.id.drawer_layout)
    DrawerLayout drawer;
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.nav_view)
    NavigationView navigationView;

    List<CardContent> contents;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, null);

        ButterKnife.inject(this, view);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        contents = new ArrayList<>();

        contents.add(new CardContent("SmallImage", "SmallImageCard is a simple card which shows a title, an icon, and a description.", "http://pic.qiantucdn.com/58pic/21/44/46/76Z58PICzCu_1024.jpg", CardGenerator.CARDTYPE.SMALL_IMAGE_CARD));
        contents.add(new CardContent("BigImage", "BigImageCard is a Card which shows a big image, a title inside the image, and a description below the image.", "http://img2.imgtn.bdimg.com/it/u=1887006003,1873570913&fm=21&gp=0.jpg", CardGenerator.CARDTYPE.BIG_IMAGE_CARD));

        for (CardContent content : contents) {
            news.getAdapter().add(CardGenerator.getInstance().generateCard(getActivity(), content.getCardtype(), content));
        }

        news.addOnItemTouchListener(new RecyclerItemClickListener.OnItemClickListener() {

            @Override
            public void onItemClick(Card card, int position) {
                Toast.makeText(getActivity(), card.getProvider().getTitle(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(Card card, int position) {
                Toast.makeText(getActivity(), card.getProvider().getDescription(), Toast.LENGTH_LONG).show();
            }
        });

        refreshComponent.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 执行刷新操作
                refreshComponent.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        addCard();

                        refreshComponent.setRefreshing(false);
                    }
                }, 2000);

            }
        });

        refreshComponent.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);

        return view;
    }

    public void addCard() {
        CardContent content = new CardContent("Add Card", "Appear after refresh.", "http://img2.imgtn.bdimg.com/it/u=1887006003,1873570913&fm=21&gp=0.jpg", CardGenerator.CARDTYPE.SMALL_IMAGE_CARD);
        news.getAdapter().add(0, CardGenerator.getInstance().generateCard(getActivity(), content.getCardtype(), content));
        news.getAdapter().notifyDataSetChanged();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
