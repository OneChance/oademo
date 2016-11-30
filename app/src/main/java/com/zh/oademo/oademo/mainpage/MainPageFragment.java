package com.zh.oademo.oademo.mainpage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
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


public class MainPageFragment extends BaseFragment {

    @InjectView(R.id.news)
    MaterialListView news;
    @InjectView(R.id.refresh_component)
    SwipeRefreshLayout refreshComponent;

    List<CardContent> contents;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_page, null);

        ButterKnife.inject(this, view);

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
}
