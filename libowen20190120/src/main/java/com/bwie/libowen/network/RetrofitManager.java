package com.bwie.libowen.network;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RetrofitManager {
    private static volatile RetrofitManager instance;
    private String BASE_URL = "http://www.zhaoapi.cn/product/";
    private ReApis apis;

    //单例
    public static synchronized RetrofitManager getInstance(){
        if (instance == null){
            instance = new RetrofitManager();
        }
        return instance;
    }

    private RetrofitManager(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(10,TimeUnit.SECONDS);
        builder.readTimeout(10,TimeUnit.SECONDS);
        builder.writeTimeout(10,TimeUnit.SECONDS);
        builder.addInterceptor(interceptor);
        builder.retryOnConnectionFailure(true);

        OkHttpClient client = builder.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();

        apis = retrofit.create(ReApis.class);
    }

    public RetrofitManager get(String uri,HttpLisener lisener){
        apis.get(uri)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver(lisener));
        return instance;
    }

    public RetrofitManager post(String uri, Map<String,String> map, HttpLisener lisener){
        apis.post(uri,map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver(lisener));
        return instance;
    }

    public Observer getObserver(final HttpLisener lisener){
        Observer observer = new Observer<ResponseBody>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (lisener != null){
                    lisener.failed(e.getMessage());
                }
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    String string = responseBody.string();
                    if (lisener != null){
                        lisener.sucess(string);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    if (lisener != null){
                        lisener.failed(e.getMessage());
                    }
                }
            }
        };
        return observer;
    }

    public interface HttpLisener{
        void sucess(String str);
        void failed(String err);
    }
}
