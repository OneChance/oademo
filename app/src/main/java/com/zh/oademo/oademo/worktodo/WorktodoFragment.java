package com.zh.oademo.oademo.worktodo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.listeners.RecyclerItemClickListener;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.zh.oademo.oademo.R;
import com.zh.oademo.oademo.common.BaseFragment;
import com.zh.oademo.oademo.common.CardContent;
import com.zh.oademo.oademo.common.CardGenerator;
import com.zh.oademo.oademo.entity.WorkContent;
import com.zh.oademo.oademo.plugins.materiallist.MaterialListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class WorktodoFragment extends BaseFragment {

    @InjectView(R.id.worklist)
    MaterialListView worklist;
    @InjectView(R.id.search_view)
    MaterialSearchView searchView;
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.refresh_component)
    SwipeRefreshLayout refreshComponent;

    private Context context;
    List<WorkContent> contents;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_worktodo, null);

        ButterKnife.inject(this, view);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        setHasOptionsMenu(true);

        context = getActivity();

        contents = new ArrayList<>();

        getWorkData();

        worklist.addOnItemTouchListener(new RecyclerItemClickListener.OnItemClickListener() {

            @Override
            public void onItemClick(Card card, int position) {
                toWork(position);
            }

            @Override
            public void onItemLongClick(Card card, int position) {
                toWork(position);
            }
        });

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                queryContent(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
            }

            @Override
            public void onSearchViewClosed() {

            }
        });

        refreshComponent.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 执行刷新操作
                refreshComponent.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        getWorkData();

                        refreshComponent.setRefreshing(false);
                    }
                }, 500);

            }
        });

        refreshComponent.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);


        return view;
    }

    public void addCards() {

        worklist.getAdapter().clearAll();

        for (CardContent content : contents) {
            worklist.getAdapter().add(CardGenerator.getInstance().generateCard(getActivity(), content.getCardtype(), content));
        }
    }

    public void getWorkData() {

    }

    public void queryContent(String query) {

        getWorkData();

        if (!query.equals("")) {
            for (int i = 0; i < contents.size(); i++) {
                if (!contents.get(i).getTitle().contains(query)) {
                    contents.remove(i);
                    i--;
                }
            }
            addCards();
        }
    }

    public void toWork(int position) {
        Intent intent = new Intent();
        intent.setClass(context, WorkActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("workContent", contents.get(position));
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.search, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
    }
}
