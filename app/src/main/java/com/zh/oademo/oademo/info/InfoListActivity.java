package com.zh.oademo.oademo.info;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.dexafree.materialList.card.Card;
import com.zh.oademo.oademo.R;
import com.zh.oademo.oademo.common.CardGenerator;
import com.zh.oademo.oademo.entity.InfoContent;
import com.zh.oademo.oademo.net.IServices;
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

import static com.zh.oademo.oademo.R.id.workitems;

public class InfoListActivity extends AppCompatActivity implements NetObserver.DataReceiver {

    @InjectView(R.id.refresh_component)
    SwipeRefreshLayout refreshComponent;
    List<InfoContent> contents;
    @InjectView(workitems)
    MaterialListView infos;
    @InjectView(R.id.loading_progress)
    ProgressBar loading_progress;
    int page;
    String type;
    boolean isLoading;
    List<InfoContent> contentsAdd;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_list);
        ButterKnife.inject(this);
        type = getIntent().getExtras().getString("infoType");

        page = 1;
        contents = new ArrayList<>();
        contentsAdd = new ArrayList<>();
        context = this;

        getInfoList();

        refreshComponent.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 执行刷新操作
                if (!isLoading && loading_progress.getVisibility() == View.GONE) {
                    page = 1;
                    getInfoList();
                }
            }
        });

        infos.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && ((LinearLayoutManager) infos.getLayoutManager()).findLastVisibleItemPosition() + 1 == infos.getAdapter().getItemCount()) {
                    if (!isLoading && !refreshComponent.isRefreshing() && loading_progress.getVisibility() == View.GONE) {
                        isLoading = true;
                        infos.getAdapter().addFoot();
                        refreshComponent.setRefreshing(false);
                        refreshComponent.setEnabled(false);
                        page++;
                        getInfoList();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        infos.addOnItemTouchListener(new RecyclerItemClickListener.OnItemClickListener() {

            @Override
            public void onItemClick(@NonNull Card card, int position) {
                Log.d("oademo", "card title" + contents.get(position).getTitle());
                Intent intent = new Intent();
                intent.setClass(context, InfoItemActivity.class);
                intent.putExtra("infoItem", contents.get(position));
                context.startActivity(intent);
            }

            @Override
            public void onItemLongClick(@NonNull Card card, int position) {

            }
        });

        refreshComponent.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
    }

    public void getInfoList() {
        Map<String, String> authParams = AuthParams.getPaams(getSharedPreferences("loginInfo", Context.MODE_PRIVATE));
        NetUtil.SetObserverCommonAction(NetUtil.getServices().getInfoList(page + "", type, authParams.get("m_timestamp"), authParams.get("userid"), authParams.get("m_auth_t")))
                .subscribe(new NetObserver(this, this, IServices.CODE_INFO_LIST));
    }

    @Override
    @SuppressWarnings("unchecked")
    public void handle(Object data,int code) {
        //Log.d("oademo", "work items:" + data);
        if (data != null) {

            Map infoList = (Map) data;
            ArrayList<Map> infoItemList = (ArrayList<Map>) (infoList.get("datas"));
            int nowPage = Formatter.removePointInt(infoList.get("nowPage"));
            int scrollTo = 0;

            if (nowPage == 1) {
                contents.clear();
                scrollTo = 0;
                infos.getAdapter().clearAll();
            } else {
                scrollTo = contents.size();
            }

            if (infoItemList.size() > 0) {
                for (Map infoItem : infoItemList) {
                    InfoContent content = new InfoContent(infoItem.get("title").toString(), infoItem.get("description").toString(), "", CardGenerator.CARDTYPE.TEXT_CARD);
                    content.setUrl(infoItem.get("url").toString());
                    content.setInfoType(type);
                    contentsAdd.add(content);
                }

                contents.addAll(contentsAdd);

                for (InfoContent content : contentsAdd) {
                    infos.getAdapter().add(CardGenerator.getInstance().generateCard(this, content.getCardtype(), content));
                }

                page = nowPage;

                contentsAdd.clear();
            }

            if (infos.getAdapter().getItemCount() > 0) {
                infos.scrollToPosition(scrollTo);
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
        infos.getAdapter().removeFoot();
        isLoading = false;
        loading_progress.setVisibility(View.GONE);
    }
}
