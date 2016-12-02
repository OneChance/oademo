package com.zh.oademo.oademo.worktodo;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.zh.oademo.oademo.MyApplication;
import com.zh.oademo.oademo.R;
import com.zh.oademo.oademo.common.WorkContent;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class WorkActivity extends AppCompatActivity {

    @InjectView(R.id.web_view)
    WebView mWebView;
    WorkContent workContent;
    Activity activity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work);
        activity = this;

        ButterKnife.inject(this);

        Bundle bundle = this.getIntent().getExtras();
        workContent = (WorkContent) bundle.getSerializable("workContent");

        final String workUrl = "http://10.4.200.62:9000/yinjia/flow/project/toApprove/joinsign/?processinsid=527&processInsId=527&workitemid=2119&workitemId=2119&loadfrom=index";

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
                view.loadUrl(url);
                return true;
            }
        });

        ((MyApplication) getApplication()).getInstance().addActivity(this);
    }

}
