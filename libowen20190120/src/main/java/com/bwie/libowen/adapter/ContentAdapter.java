package com.bwie.libowen.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

public class ContentAdapter extends PagerAdapter {
    private Context context;
    private List<String> list;

    public ContentAdapter(Context context) {
        this.context = context;
        list = new ArrayList<>();
    }

    public void setData(List<String> lists){
        list.clear();
        list.addAll(lists);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size()>0 ? 5000:0;
    }

    public String getItem(int i){
        return list.get(i%list.size());
    }
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        SimpleDraweeView simpleDraweeView = new SimpleDraweeView(context);
        container.addView(simpleDraweeView);
        simpleDraweeView.setImageURI(list.get(position%list.size()));
        return simpleDraweeView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
