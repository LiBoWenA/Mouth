package com.bwie.libowen.model;

import com.bwie.libowen.callback.MyCallBack;
import com.bwie.libowen.network.RetrofitManager;
import com.google.gson.Gson;

import java.util.Map;

public class IModelImpl implements IModel {
    @Override
    public void requestData(String path, final Class clazz, final MyCallBack myCallBack) {
        RetrofitManager.getInstance().get(path, new RetrofitManager.HttpLisener() {
            @Override
            public void sucess(String str) {
                Object o = new Gson().fromJson(str, clazz);
                if (myCallBack != null){
                    myCallBack.sucess(o);
                }
            }

            @Override
            public void failed(String err) {
                if (myCallBack != null){
                    myCallBack.failed(err);
                }
            }
        });
    }

    @Override
    public void requestData(String path, Map<String, String> map, final Class clazz, final MyCallBack myCallBack) {
        RetrofitManager.getInstance().post(path, map, new RetrofitManager.HttpLisener() {
            @Override
            public void sucess(String str) {
                Object o = new Gson().fromJson(str, clazz);
                if (myCallBack != null){
                    myCallBack.sucess(o);
                }
            }

            @Override
            public void failed(String err) {
                if (myCallBack != null){
                    myCallBack.failed(err);
                }
            }
        });
    }
}
