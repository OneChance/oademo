package com.zh.oademo.oademo.contact;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.ProgressBar;

import com.dexafree.materialList.card.Card;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.zh.oademo.oademo.R;
import com.zh.oademo.oademo.common.BaseFragment;
import com.zh.oademo.oademo.common.CardGenerator;
import com.zh.oademo.oademo.entity.ContactContent;
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


public class ContactFragment extends BaseFragment implements NetObserver.DataReceiver {

    @InjectView(R.id.contact_list)
    MaterialListView contactList;
    @InjectView(R.id.search_view)
    MaterialSearchView searchView;
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.refresh_component)
    SwipeRefreshLayout refreshComponent;
    @InjectView(R.id.loading_progress)
    ProgressBar loading_progress;
    private Context context;
    List<ContactContent> contents;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_contact, null);

        ButterKnife.inject(this, view);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        setHasOptionsMenu(true);

        context = getActivity();

        contents = new ArrayList<>();

        getContacts("");

        contactList.addOnItemTouchListener(new RecyclerItemClickListener.OnItemClickListener() {

            @Override
            public void onItemClick(@NonNull Card card, int position) {
                Uri uri = Uri.parse("tel:" + contents.get(position).getMobile());
                Intent intent = new Intent(Intent.ACTION_DIAL, uri);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(@NonNull Card card, int position) {

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
                if (loading_progress.getVisibility() == View.GONE) {
                    getContacts("");
                }
            }
        });

        refreshComponent.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);


        return view;
    }

    public void getContacts(String search) {
        Map<String, String> authParams = AuthParams.getPaams(getActivity().getSharedPreferences("loginInfo", Context.MODE_PRIVATE));
        NetUtil.SetObserverCommonAction(NetUtil.getServices().getContacts(search, authParams.get("m_timestamp"), authParams.get("userid"), authParams.get("m_auth_t")))
                .subscribe(new NetObserver(context, this));
    }

    public void queryContent(String query) {
        getContacts(query);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.search, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void handle(Object data) {

        //Log.d("oademo", "contacts:" + data);

        if (data != null) {

            ArrayList<Map> contacts = (ArrayList<Map>) data;

            contents.clear();
            contactList.getAdapter().clearAll();

            if (contacts.size() > 0) {
                for (Map contact : contacts) {

                    String mobile = contact.get("mobile").toString();

                    if (!mobile.equals("") && mobile.contains(",")) {
                        mobile = mobile.split(",")[0];
                    }

                    ContactContent content = new ContactContent(contact.get("uname").toString() + " " + mobile, contact.get("orgnamepath").toString(), "", CardGenerator.CARDTYPE.TEXT_CARD);
                    content.setMobile(mobile);
                    contents.add(content);
                }

                for (ContactContent content : contents) {
                    contactList.getAdapter().add(CardGenerator.getInstance().generateCard(context, content.getCardtype(), content));
                }
            }

            if (contactList.getAdapter().getItemCount() > 0) {
                contactList.scrollToPosition(0);
            }
        }

        getEnd();//清除加载或刷新的状态
    }

    @Override
    public void error() {
        getEnd();
    }

    public void getEnd() {
        loading_progress.setVisibility(View.GONE);
        refreshComponent.setRefreshing(false);
    }
}
