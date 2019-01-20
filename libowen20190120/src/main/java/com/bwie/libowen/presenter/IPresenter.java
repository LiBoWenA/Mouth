package com.bwie.libowen.presenter;

import java.util.Map;

public interface IPresenter {
    void showRequestData(String path,Class clazz);
    void showRequestData(String path, Map<String,String> map, Class clazz);
}
