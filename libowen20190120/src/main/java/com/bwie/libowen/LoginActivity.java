package com.bwie.libowen;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bwie.libowen.adapter.ContentAdapter;
import com.bwie.libowen.bean.ContentBean;
import com.bwie.libowen.presenter.IPresenterImpl;
import com.bwie.libowen.view.IView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.recker.flybanner.FlyBanner;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class LoginActivity extends AppCompatActivity implements IView {


    @BindView(R.id.user_img)
    SimpleDraweeView userImg;
    @BindView(R.id.pager)
    ViewPager pager;
    @BindView(R.id.item_name)
    TextView itemName;
    @BindView(R.id.item_price)
    TextView itemPrice;
    @BindView(R.id.share_qq)
    Button btnQq;
    @BindView(R.id.share_wechat)
    Button shareWechat;
    private IPresenterImpl iPresenter;
    private String path = "getProductDetail?pid=%s";
    private Unbinder bind;
    private ContentAdapter adapter;
     @SuppressLint("HandlerLeak")
     private Handler handler = new Handler(){
         @Override
         public void handleMessage(Message msg) {
             super.handleMessage(msg);
             pager.setCurrentItem(pager.getCurrentItem()+1);
             handler.sendEmptyMessageDelayed(0,2000);
         }
     };
    private List<String> list;
    private String title;
    private String img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        bind = ButterKnife.bind(this);
        iPresenter = new IPresenterImpl(this);
        Intent intent = getIntent();
        int pid = intent.getIntExtra("pid", 1);
        iPresenter.showRequestData(String.format(path, pid), ContentBean.class);
        adapter = new ContentAdapter(this);
        pager.setAdapter(adapter);
        int currentItem = pager.getCurrentItem();
        handler.sendEmptyMessageDelayed(currentItem,1000);
        //点击QQ第三方登录
        btnQq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UMShareAPI umShareAPI = UMShareAPI.get(LoginActivity.this);
                umShareAPI.getPlatformInfo(LoginActivity.this, SHARE_MEDIA.QQ, new UMAuthListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {

                    }

                    @Override
                    public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                        Log.i("TAGGG", map.toString());
                        String profile_image_url = map.get("profile_image_url");
                        userImg.setImageURI(profile_image_url);
                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {

                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media, int i) {

                    }
                });
            }
        });
        //点击分享进行分享
        shareWechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UMImage image = new UMImage(LoginActivity.this, img);//网络图片
                new ShareAction(LoginActivity.this)
                        .setPlatform(SHARE_MEDIA.WEIXIN)//传入平台
                        .withText(title)//分享内容
                        .withMedia(image)
                        .setCallback(shareListener)//回调监听器
                        .share();
            }
        });
    }
    private UMShareListener shareListener = new UMShareListener() {
        /**
         * @descrption 分享开始的回调
         * @param platform 平台类型
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        /**
         * @descrption 分享成功的回调
         * @param platform 平台类型
         */
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(LoginActivity.this,"成功了",Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(LoginActivity.this,"失败"+t.getMessage(),Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(LoginActivity.this,"取消了",Toast.LENGTH_LONG).show();

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void startRequestData(Object data) {
        if (data instanceof ContentBean) {
            ContentBean bean = (ContentBean) data;
            title = bean.getData().getTitle();
            if (bean.getCode().equals("0")) {
                itemName.setText(bean.getData().getTitle());
                itemPrice.setText("¥：" + bean.getData().getPrice());
                String[] split = bean.getData().getImages().split("\\|");
                list = new ArrayList<>();
                for (int i = 0; i < split.length; i++) {
                    list.add(split[i]);
                }
                adapter.setData(list);
            } else {
                Toast.makeText(LoginActivity.this, bean.getMsg().toString(), Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (iPresenter != null){
            iPresenter = null;
        }
        bind.unbind();
        handler.removeCallbacksAndMessages(null);
    }
}
