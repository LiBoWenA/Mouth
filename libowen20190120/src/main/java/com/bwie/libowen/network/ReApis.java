package com.bwie.libowen.network;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;

public interface ReApis {

    @GET
    Observable<ResponseBody> get(@Url String uri);

    @POST
    Observable<ResponseBody> post(@Url String uri, @QueryMap Map<String,String> map);
}
