package com.zh.oademo.oademo.work;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.dexafree.materialList.card.Card;
import com.zh.oademo.oademo.MyApplication;
import com.zh.oademo.oademo.R;
import com.zh.oademo.oademo.common.CardGenerator;
import com.zh.oademo.oademo.entity.WorkContent;
import com.zh.oademo.oademo.net.NetObserver;
import com.zh.oademo.oademo.net.NetUtil;
import com.zh.oademo.oademo.plugins.materiallist.MaterialListView;
import com.zh.oademo.oademo.plugins.materiallist.RecyclerItemClickListener;
import com.zh.oademo.oademo.util.AuthParams;
import com.zh.oademo.oademo.util.Formatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class WorkListActivity extends AppCompatActivity implements NetObserver.DataReceiver {

    @InjectView(R.id.refresh_component)
    SwipeRefreshLayout refreshComponent;
    List<WorkContent> contents;
    @InjectView(R.id.workitems)
    MaterialListView workitems;
    int page;
    String type;
    boolean isLoading;
    List<WorkContent> contentsAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worklist);
        ButterKnife.inject(this);
        type = getIntent().getExtras().getString("worktype");

        page = 1;
        contents = new ArrayList<>();
        contentsAdd = new ArrayList<>();

        getWorkList();

        refreshComponent.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 执行刷新操作
                if (!isLoading) {
                    page = 1;
                    getWorkList();
                }
            }
        });

        workitems.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && ((LinearLayoutManager) workitems.getLayoutManager()).findLastVisibleItemPosition() + 1 == workitems.getAdapter().getItemCount()) {
                    if (!isLoading && !refreshComponent.isRefreshing()) {
                        isLoading = true;
                        workitems.getAdapter().addFoot();
                        refreshComponent.setRefreshing(false);
                        refreshComponent.setEnabled(false);
                        page++;
                        getWorkList();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        workitems.addOnItemTouchListener(new RecyclerItemClickListener.OnItemClickListener() {

            @Override
            public void onItemClick(Card card, int position) {
                Log.d("oademo", "card title" + contents.get(position).getTitle());
            }

            @Override
            public void onItemLongClick(Card card, int position) {

            }
        });

        refreshComponent.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
    }

    public void getWorkList() {
        Log.d("oademo", "get page:" + page);
        Map<String, String> authParams = AuthParams.getPaams(getSharedPreferences("loginInfo", Context.MODE_PRIVATE));
        NetUtil.SetObserverCommonAction(NetUtil.getServices().getWorkList(page + "", type, authParams.get("m_timestamp"), authParams.get("userid"), authParams.get("m_auth_t")))
                .subscribe(new NetObserver(this, this));
    }

    @Override
    @SuppressWarnings("unchecked")
    public void handle(Object data) {
        Log.d("oademo", "work items:" + data);

        if (data != null) {

            Map workList = (Map) data;
            ArrayList<Map> workitemList = (ArrayList<Map>) (workList.get("datas"));
            int nowPage = Formatter.removePointInt(workList.get("nowPage"));
            int scrollTo = 0;

            if (nowPage == 1) {
                contents.clear();
                scrollTo = 0;
                workitems.getAdapter().clearAll();
            } else {
                scrollTo = contents.size();
            }

            if (workitemList.size() > 0) {
                for (Map workitem : workitemList) {
                    WorkContent content = new WorkContent(workitem.get("title").toString(), workitem.get("description").toString(), "", CardGenerator.CARDTYPE.TEXT_CARD);
                    content.setUrl(workitem.get("url").toString());
                    contentsAdd.add(content);
                }

                contents.addAll(contentsAdd);

                for (WorkContent content : contentsAdd) {
                    workitems.getAdapter().add(CardGenerator.getInstance().generateCard(this, content.getCardtype(), content));
                }

                page = nowPage;

                contentsAdd.clear();
            }

            if (workitems.getAdapter().getItemCount() > 0) {
                workitems.scrollToPosition(scrollTo);
            }
        }

        getEnd();//清除加载或刷新的状态
    }

    @Override
    public void error() {
        getEnd();
    }

    public void getEnd() {
        refreshComponent.setRefreshing(false);
        refreshComponent.setEnabled(true);
        workitems.getAdapter().removeFoot();
        isLoading = false;
    }
}
