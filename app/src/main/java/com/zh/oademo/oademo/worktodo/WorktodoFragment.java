package com.zh.oademo.oademo.worktodo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.CardProvider;
import com.dexafree.materialList.view.MaterialListView;
import com.zh.oademo.oademo.R;
import com.zh.oademo.oademo.common.BaseFragment;
import com.zh.oademo.oademo.common.CardContent;
import com.zh.oademo.oademo.common.CardGenerator;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class WorktodoFragment extends BaseFragment {

    @InjectView(R.id.worklist)
    MaterialListView worklist;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_worktodo, null);
        ButterKnife.inject(this, view);

        Card card = new Card.Builder(getActivity())
                .setTag("SMALL_IMAGE_CARD")
                .withProvider(new CardProvider())
                .setLayout(R.layout.material_small_image_card)
                .setTitle("I'm new")
                .setDescription("I've been generated on runtime!")
                .setDrawable(R.mipmap.ic_launcher)
                .endConfig()
                .build();

        List<CardContent> contents = new ArrayList<>();

        contents.add(new CardContent("lakers", "kobe home", "http://pic.qiantucdn.com/58pic/21/44/46/76Z58PICzCu_1024.jpg"));
        contents.add(new CardContent("bulls", "jordan home", "http://img2.imgtn.bdimg.com/it/u=1887006003,1873570913&fm=21&gp=0.jpg"));

        for (CardContent content : contents) {
            worklist.getAdapter().add(CardGenerator.getInstance().generateCard(getActivity(), CardGenerator.CARDTYPE.SMALL_IMAGE_CARD, content));
        }

        return view;
    }
}
