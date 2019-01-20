package com.bwie.libowen.presenter;

import com.bwie.libowen.callback.MyCallBack;
import com.bwie.libowen.model.IModel;
import com.bwie.libowen.model.IModelImpl;
import com.bwie.libowen.view.IView;

import java.util.Map;

public class IPresenterImpl implements IPresenter {
    private IView iView;
    private IModelImpl iModel;

    public IPresenterImpl(IView iView) {
        this.iView = iView;
        iModel = new IModelImpl();
    }

    @Override
    public void showRequestData(String path, Class clazz) {
        iModel.requestData(path, clazz, new MyCallBack() {
            @Override
            public void sucess(Object data) {
                iView.startRequestData(data);
            }

            @Override
            public void failed(String error) {
                iView.startRequestData(error);
            }
        });
    }

    @Override
    public void showRequestData(String path, Map<String, String> map, Class clazz) {
        iModel.requestData(path, map, clazz, new MyCallBack() {
            @Override
            public void sucess(Object data) {
                iView.startRequestData(data);
            }

            @Override
            public void failed(String error) {
                iView.startRequestData(error);
            }
        });
    }

    public void onDestory(){
        if (iModel != null){
            iModel = null;
        }
        if (iView != null){
            iView = null;
        }
    }
}
