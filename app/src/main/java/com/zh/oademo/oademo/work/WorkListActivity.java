package com.zh.oademo.oademo.work;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.zh.oademo.oademo.MyApplication;
import com.zh.oademo.oademo.R;
import com.zh.oademo.oademo.common.CardGenerator;
import com.zh.oademo.oademo.entity.WorkContent;
import com.zh.oademo.oademo.net.NetObserver;
import com.zh.oademo.oademo.net.NetUtil;
import com.zh.oademo.oademo.plugins.materiallist.MaterialListView;
import com.zh.oademo.oademo.plugins.scroll.MySwipe;
import com.zh.oademo.oademo.util.AuthParams;
import com.zh.oademo.oademo.util.Formatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class WorkListActivity extends AppCompatActivity implements NetObserver.DataReceiver {

    @InjectView(R.id.refresh_component)
    MySwipe refreshComponent;
    List<WorkContent> contents;
    @InjectView(R.id.workitems)
    MaterialListView workitems;
    int page;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worklist);
        ButterKnife.inject(this);
        type = getIntent().getExtras().getString("worktype");

        page = 1;
        contents = new ArrayList<>();

        getWorkList();

        refreshComponent.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 执行刷新操作
                page = 1;
                getWorkList();
            }
        });

        refreshComponent.setOnLoadListener(new MySwipe.OnLoadListener() {
            @Override
            public void onLoad() {
                page++;
                Log.d("oademo", "1234");
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

        refreshComponent.setRefreshing(false);
        refreshComponent.setLoading(false);

        Map workList = (Map) data;
        ArrayList<Map> workitemList = (ArrayList<Map>) (workList.get("datas"));

        if (workitemList.size() > 0) {
            if (page == 1) {
                if (workitemList.size() == contents.size()) {
                    MyApplication.toast(R.string.no_more_data, true);
                }
                contents.clear();
                workitems.getAdapter().clearAll();
            }

            for (Map workitem : workitemList) {
                WorkContent content = new WorkContent(workitem.get("title").toString(), workitem.get("description").toString(), "", CardGenerator.CARDTYPE.TEXT_CARD);
                content.setUrl(workitem.get("url").toString());
                contents.add(content);
            }

            for (WorkContent content : contents) {
                workitems.getAdapter().add(CardGenerator.getInstance().generateCard(this, content.getCardtype(), content));
            }

            page = Formatter.removePointInt(workList.get("nowPage"));

        } else {
            MyApplication.toast(R.string.no_more_data, true);
        }
    }

    @Override
    public void error() {

    }
}
