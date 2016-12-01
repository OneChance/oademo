package com.zh.oademo.oademo.worktodo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.zh.oademo.oademo.R;
import com.zh.oademo.oademo.common.BaseFragment;
import com.zh.oademo.oademo.common.CardGenerator;
import com.zh.oademo.oademo.common.WorkContent;

import java.util.ArrayList;
import java.util.List;


public class WorktodoFragment extends BaseFragment {

    //@InjectView(R.id.worklist)
    //MaterialListView worklist;
    //@InjectView(R.id.search_view)
    //MaterialSearchView searchView;
    //@InjectView(R.id.toolbar)
    //Toolbar toolbar;

    private Context context;
    List<WorkContent> contents;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_worktodo, null);

        //ButterKnife.inject(this, view);

        //((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        //setHasOptionsMenu(true);

        context = getActivity();

        contents = new ArrayList<>();
        contents.add(new WorkContent("个人资金", "123412", "", CardGenerator.CARDTYPE.TEXT_CARD, "0"));
        contents.add(new WorkContent("立项申请", "742342", "", CardGenerator.CARDTYPE.TEXT_CARD, "1"));
        contents.add(new WorkContent("出差审批", "298764", "", CardGenerator.CARDTYPE.TEXT_CARD, "2"));

        /*for (CardContent content : contents) {
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
        });*/


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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.search, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        //searchView.setMenuItem(item);
    }
}
