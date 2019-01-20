package com.bwie.libowen;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Button;

import com.bwie.libowen.adapter.HomePageAdapter;
import com.bwie.libowen.bean.ShopBean;
import com.bwie.libowen.presenter.IPresenterImpl;
import com.bwie.libowen.view.IView;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity implements IView {

    @BindView(R.id.baidumap)
    Button btnMap;
    @BindView(R.id.contentrv)
    XRecyclerView contentRv;
    private IPresenterImpl iPresenter;
    private HomePageAdapter adapter;
    private int page;
    private String path = "searchProducts?keywords=笔记本&page=%d";
    private Unbinder bind;
    private ShopBean bean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bind = ButterKnife.bind(this);
        page = 1;
        iPresenter = new IPresenterImpl(this);
        //添加布局管理者
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        contentRv.setLayoutManager(manager);
        //支持刷新加载
        contentRv.setLoadingMoreEnabled(true);
        contentRv.setPullRefreshEnabled(true);
        adapter = new HomePageAdapter(this);
        contentRv.setAdapter(adapter);
        contentRv.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                //刷新
                page = 1;
                iPresenter.showRequestData(String.format(path,page),ShopBean.class);
            }

            @Override
            public void onLoadMore() {
                iPresenter.showRequestData(String.format(path,page),ShopBean.class);
            }
        });
        iPresenter.showRequestData(String.format(path,page),ShopBean.class);
        //点击跳转详情页
        init();
    }

    private void init() {
        adapter.setOnClick(new HomePageAdapter.OnClick() {
            @Override
            public void click(int pid) {
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                intent.putExtra("pid",pid);
                startActivity(intent);
            }
        });
        adapter.setOnLongClick(new HomePageAdapter.OnLongClick() {
            @Override
            public void longClick(int i) {
                ObjectAnimator om = ObjectAnimator.ofFloat(bean.getData().get(i),"Alpha",1f,0f);
                om.setDuration(1000);
                om.start();
                bean.getData().remove(i);
            }
        });
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,MapActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (iPresenter != null){
            iPresenter = null;
        }
        bind.unbind();
    }

    @Override
    public void startRequestData(Object data) {
        if (data instanceof ShopBean){
            bean = (ShopBean) data;
            if (bean.getMsg().equals("查询成功")){
                if (page == 1){
                    adapter.setData(bean.getData());
                }else{
                    adapter.setData(bean.getData());
                }
                page++;
                //停止刷新加载
                contentRv.loadMoreComplete();
                contentRv.refreshComplete();
            }
        }
    }
}
