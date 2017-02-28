package com.zh.oademo.oademo.net;


import com.zh.oademo.oademo.entity.NetObject;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

public interface IServices {

    public static final int CODE_CHECK_UPDATE = 0;
    public static int CODE_LOGIN = 1;
    public static int CODE_INFO_TYPE = 2;
    public static int CODE_INFO_LIST = 3;
    public static int CODE_WORK_TYPE = 4;
    public static int CODE_WORK_LIST = 5;
    public static int CODE_CONTACT_LIST = 6;

    @FormUrlEncoded
    @POST("mobile/api/user/login.json")
    public Observable<NetObject> Login(@Field("l") String username, @Field("p") String passwordMd5);

    @FormUrlEncoded
    @POST("mobile/api/work/type.json")
    public Observable<NetObject> getWorkType(@Field("m_timestamp") String timeStamp, @Field("m_auth_u") String userid, @Field("m_auth_t") String useridMD5);

    @FormUrlEncoded
    @POST("mobile/api/work/list.json")
    public Observable<NetObject> getWorkList(@Field("page") String page, @Field("type") String type, @Field("m_timestamp") String timeStamp, @Field("m_auth_u") String userid, @Field("m_auth_t") String useridMD5);

    @FormUrlEncoded
    @POST("mobile/api/info/type.json")
    public Observable<NetObject> getInfoType(@Field("m_timestamp") String timeStamp, @Field("m_auth_u") String userid, @Field("m_auth_t") String useridMD5);

    @FormUrlEncoded
    @POST("mobile/api/info/list.json")
    public Observable<NetObject> getInfoList(@Field("page") String page, @Field("type") String type, @Field("m_timestamp") String timeStamp, @Field("m_auth_u") String userid, @Field("m_auth_t") String useridMD5);

    @FormUrlEncoded
    @POST("mobile/api/user/tels.json")
    public Observable<NetObject> getContacts(@Field("search") String search, @Field("m_timestamp") String timeStamp, @Field("m_auth_u") String userid, @Field("m_auth_t") String useridMD5);

    @GET("mobile/api/system/info.json")
    public Observable<NetObject> checkUpdate();

}
