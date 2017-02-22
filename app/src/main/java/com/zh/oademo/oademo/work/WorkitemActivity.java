package com.zh.oademo.oademo.work;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.zh.oademo.oademo.MyApplication;
import com.zh.oademo.oademo.R;
import com.zh.oademo.oademo.entity.WorkContent;
import com.zh.oademo.oademo.net.NetConfig;
import com.zh.oademo.oademo.util.AuthParams;

import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class WorkitemActivity extends AppCompatActivity {

    @InjectView(R.id.web_view)
    WebView mWebView;

    @InjectView(R.id.loading_progress)
    ProgressBar loading_progress;

    WorkContent workContent;
    Activity activity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_one);
        activity = this;

        ButterKnife.inject(this);

        workContent = (WorkContent) this.getIntent().getSerializableExtra("workitem");

        String workUrl = workContent.getUrl();

        Map<String, String> authParams = AuthParams.getPaams(getSharedPreferences("loginInfo", Context.MODE_PRIVATE));

        workUrl = "http://" + NetConfig.IP + workUrl + "&m_timestamp=" + authParams.get("m_timestamp") +
                "&m_auth_u=" + authParams.get("userid") +
                "&m_auth_t=" + authParams.get("m_auth_t");

        mWebView.loadUrl(workUrl);

        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setDomStorageEnabled(true);

        mWebView.setWebChromeClient(new WebChromeClient());

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d("oademo", "url:" + url);
                //view.loadUrl(url);
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                loading_progress.setVisibility(View.GONE);
            }
        });

        ((MyApplication) getApplication()).getInstance().addActivity(this);
    }
}
