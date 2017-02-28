package com.zh.oademo.oademo.work;

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
import com.zh.oademo.oademo.entity.WorkContent;
import com.zh.oademo.oademo.mainframe.MainPageActivity;
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


public class WorkFragment extends BaseFragment implements NetObserver.DataReceiver {

    @InjectView(R.id.worktypes)
    MaterialListView workTypes;
    List<WorkContent> contents;
    MainPageActivity activity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_types, null);

        ButterKnife.inject(this, view);

        activity = (MainPageActivity) getActivity();

        Map<String, String> authParams = AuthParams.getPaams(activity.getSharedPreferences("loginInfo", Context.MODE_PRIVATE));

        contents = new ArrayList<>();
        NetUtil.SetObserverCommonAction(NetUtil.getServices().getWorkType(authParams.get("m_timestamp"), authParams.get("userid"), authParams.get("m_auth_t")))
                .subscribe(new NetObserver(activity, this, IServices.CODE_WORK_TYPE));

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
        intent.setClass(getActivity(), WorkListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("worktype", contents.get(position).getWorkType());
        intent.putExtras(bundle);
        getActivity().startActivity(intent);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void handle(Object data,int code) {
        //Log.d("oademo", "work types:" + data);

        ArrayList<Map> typeList = (ArrayList<Map>) data;
        for (Map workType : typeList) {
            WorkContent content = new WorkContent(workType.get("title").toString(), "", "", CardGenerator.CARDTYPE.TITLE_CARD);
            int count = Formatter.removePointInt(workType.get("count"));
            if (count > 0) {
                content.setShowNumber(count);
            }
            content.setWorkType(workType.get("type").toString());
            contents.add(content);
        }

        for (WorkContent content : contents) {
            workTypes.getAdapter().add(CardGenerator.getInstance().generateCard(activity, content.getCardtype(), content));
        }
    }

    @Override
    public void error() {

    }
}
