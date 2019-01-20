package com.bwie.libowen.model;

import com.bwie.libowen.callback.MyCallBack;

import java.util.Map;

public interface IModel {
    void requestData(String path, Class clazz, MyCallBack myCallBack);
    void requestData(String path, Map<String,String> map, Class clazz, MyCallBack myCallBack);
}
