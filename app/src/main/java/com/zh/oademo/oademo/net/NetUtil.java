package com.zh.oademo.oademo.net;


import android.util.Log;

import com.zh.oademo.oademo.MyApplication;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class NetUtil {
    private static IServices services;

    public static IServices getServices() {

        if (services == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(NetConfig.CONNECT_TIMEOUT, TimeUnit.SECONDS)
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request oldRequest = chain.request();

                            HttpUrl.Builder authorizedUrlBuilder = oldRequest.url()
                                    .newBuilder()
                                    .scheme(oldRequest.url().scheme())
                                    .host(oldRequest.url().host())
                                    .addQueryParameter("m_version", MyApplication.VERSION)
                                    .addQueryParameter("m_device", "1")
                                    .addQueryParameter("m_language", "chinese");

                            Request newRequest = oldRequest.newBuilder()
                                    .method(oldRequest.method(), oldRequest.body())
                                    .url(authorizedUrlBuilder.build())
                                    .build();

                            Log.d("oademo", "net url:" + newRequest.url());

                            if ("POST".equals(newRequest.method())) {
                                StringBuilder sb = new StringBuilder();
                                if (newRequest.body() instanceof FormBody) {
                                    FormBody body = (FormBody) newRequest.body();
                                    for (int i = 0; i < body.size(); i++) {
                                        sb.append(body.encodedName(i) + "=" + body.encodedValue(i) + ",");
                                    }
                                    sb.delete(sb.length() - 1, sb.length());
                                    Log.d("oademo", "| RequestParams:{" + sb.toString() + "}");
                                }
                            }

                            return chain.proceed(newRequest);
                        }
                    }).build();


            Retrofit restAdapter = new Retrofit.Builder()
                    .baseUrl("http://" + NetConfig.IP + "/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();

            services = restAdapter.create(IServices.class);
        }

        return services;
    }

    public static <T> Observable<T> SetObserverCommonAction(Observable<T> observable) {

        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


}
