package com.zh.oademo.oademo.worktodo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.listeners.RecyclerItemClickListener;
import com.dexafree.materialList.view.MaterialListView;
import com.zh.oademo.oademo.R;
import com.zh.oademo.oademo.common.BaseFragment;
import com.zh.oademo.oademo.common.CardContent;
import com.zh.oademo.oademo.common.CardGenerator;
import com.zh.oademo.oademo.common.WorkContent;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class WorktodoFragment extends BaseFragment {

    @InjectView(R.id.worklist)
    MaterialListView worklist;

    private Context context;
    List<WorkContent> contents;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_worktodo, null);
        context = getActivity();

        ButterKnife.inject(this, view);

        contents = new ArrayList<>();
        contents.add(new WorkContent("个人资金", "123412", "", CardGenerator.CARDTYPE.TEXT_CARD, "0"));
        contents.add(new WorkContent("立项申请", "742342", "", CardGenerator.CARDTYPE.TEXT_CARD, "1"));
        contents.add(new WorkContent("出差审批", "298764", "", CardGenerator.CARDTYPE.TEXT_CARD, "2"));

        for (CardContent content : contents) {
            worklist.getAdapter().add(CardGenerator.getInstance().generateCard(getActivity(), content.getCardtype(), content));
        }

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

        return view;
    }

    public void toWork(int position) {
        Intent intent = new Intent();
        intent.setClass(context, WorkActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("workContent", contents.get(position));
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}
