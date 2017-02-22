package com.zh.oademo.oademo.info;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dexafree.materialList.card.Card;
import com.zh.oademo.oademo.R;
import com.zh.oademo.oademo.common.BaseFragment;
import com.zh.oademo.oademo.common.CardGenerator;
import com.zh.oademo.oademo.entity.InfoContent;
import com.zh.oademo.oademo.mainframe.MainPageActivity;
import com.zh.oademo.oademo.net.NetObserver;
import com.zh.oademo.oademo.net.NetUtil;
import com.zh.oademo.oademo.plugins.materiallist.MaterialListView;
import com.zh.oademo.oademo.plugins.materiallist.RecyclerItemClickListener;
import com.zh.oademo.oademo.util.AuthParams;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class InfoFragment extends BaseFragment implements NetObserver.DataReceiver {

    @InjectView(R.id.worktypes)
    MaterialListView workTypes;
    List<InfoContent> contents;
    MainPageActivity activity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_types, null);

        ButterKnife.inject(this, view);

        activity = (MainPageActivity) getActivity();

        Map<String, String> authParams = AuthParams.getPaams(activity.getSharedPreferences("loginInfo", Context.MODE_PRIVATE));

        contents = new ArrayList<>();
        NetUtil.SetObserverCommonAction(NetUtil.getServices().getInfoType(authParams.get("m_timestamp"), authParams.get("userid"), authParams.get("m_auth_t")))
                .subscribe(new NetObserver(activity, this));

        workTypes.scrollToPosition(0);

        workTypes.addOnItemTouchListener(new RecyclerItemClickListener.OnItemClickListener() {

            @Override
            public void onItemClick(Card card, int position) {
                toTypeDetail(position);
            }

            @Override
            public void onItemLongClick(Card card, int position) {

            }
        });

        return view;
    }

    private void toTypeDetail(int position) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), InfoListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("infoType", contents.get(position).getInfoType());
        intent.putExtras(bundle);
        getActivity().startActivity(intent);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void handle(Object data) {
        //Log.d("oademo", "info types:" + data);

        if (data != null) {
            Map info = (Map) data;
            ArrayList<Map> typeList = (ArrayList<Map>) info.get("types");
            for (Map infoType : typeList) {
                InfoContent content = new InfoContent(infoType.get("title").toString(), infoType.get("description").toString(), "", CardGenerator.CARDTYPE.TEXT_CARD);
                content.setInfoType(infoType.get("type").toString());
                contents.add(content);
            }

            for (InfoContent content : contents) {
                workTypes.getAdapter().add(CardGenerator.getInstance().generateCard(getActivity(), content.getCardtype(), content));
            }
        }
    }

    @Override
    public void error() {

    }
}
