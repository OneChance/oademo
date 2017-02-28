package com.zh.oademo.oademo.sign;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zh.oademo.oademo.MyApplication;
import com.zh.oademo.oademo.R;
import com.zh.oademo.oademo.mainframe.MainPageActivity;
import com.zh.oademo.oademo.net.IServices;
import com.zh.oademo.oademo.net.NetConfig;
import com.zh.oademo.oademo.net.NetObserver;
import com.zh.oademo.oademo.net.NetUtil;
import com.zh.oademo.oademo.util.AuthParams;
import com.zh.oademo.oademo.util.Formatter;

import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements NetObserver.DataReceiver {

    // UI references.
    @InjectView(R.id.account)
    EditText mAccountView;
    @InjectView(R.id.password)
    EditText mPasswordView;
    @InjectView(R.id.login_progress)
    View mProgressView;
    @InjectView(R.id.login_form)
    View mLoginFormView;
    @InjectView(R.id.email_sign_in_button)
    Button signInButton;
    @InjectView(R.id.downloading)
    TextView downloading;
    String passwordMD5;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        context = this;
        ButterKnife.inject(this);

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        signInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        MyApplication.addActivity(this);

        //检查是否需要更新
        NetUtil.SetObserverCommonAction(NetUtil.getServices().checkUpdate())
                .subscribe(new NetObserver(context, this, IServices.CODE_CHECK_UPDATE));
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        // Reset errors.
        mAccountView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String account = mAccountView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(account)) {
            mAccountView.setError(getString(R.string.error_field_required));
            focusView = mAccountView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            try {
                passwordMD5 = AuthParams.getMD5Code(password);
                NetUtil.SetObserverCommonAction(NetUtil.getServices().Login(account, passwordMD5))
                        .subscribe(new NetObserver(context, this, IServices.CODE_LOGIN));
                showProgress(true);
            } catch (Exception e) {

            }
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        MyApplication.exit();
    }

    @Override
    protected void onResume() {
        showProgress(false);
        downloading.setVisibility(View.INVISIBLE);
        super.onResume();
    }

    @Override
    public void handle(Object data, int code) {
        final Map loginInfo = ((Map) data);

        Log.d("oademo", data + "");

        if (code == IServices.CODE_CHECK_UPDATE) {
            if (loginInfo.get("updateTips") != null) {
                showProgress(true);
                downloading.setText(loginInfo.get("updateTips").toString());
                downloading.setVisibility(View.VISIBLE);
                String url = loginInfo.get("updateUrl").toString();
                if (!url.startsWith("http")) {
                    url = "http://" + NetConfig.IP + url;
                }
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(url);
                intent.setData(content_url);
                startActivity(intent);
            }
        } else if (code == IServices.CODE_LOGIN) {
            SharedPreferences sharedPreferences = getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("userid", Formatter.removePointString(loginInfo.get("userid")));
            editor.putString("passwordMD5", passwordMD5);
            editor.apply();
            Intent intent = new Intent();
            intent.setClass(context, MainPageActivity.class);
            Bundle bundle = new Bundle();
            intent.putExtras(bundle);
            context.startActivity(intent);
        }
    }

    @Override
    public void error() {
        showProgress(false);
    }
}

