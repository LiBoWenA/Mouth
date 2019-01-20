package com.bwie.libowen.adapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;

import com.bwie.libowen.R;
import com.bwie.libowen.bean.ShopBean;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomePageAdapter extends RecyclerView.Adapter<HomePageAdapter.ViewHolder> {

    private Context context;
    private List<ShopBean.DataBean> list;

    public HomePageAdapter(Context context) {
        this.context = context;
        list = new ArrayList<>();
    }

    public void setData(List<ShopBean.DataBean> lists) {
        list.clear();
        list.addAll(lists);
        notifyDataSetChanged();
    }

    public void addData(List<ShopBean.DataBean> lists) {
        list.addAll(lists);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.mainactivity_item, null);
        ViewHolder viewHolder = new ViewHolder(view);
        ButterKnife.bind(viewHolder, view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        String[] split = list.get(i).getImages().split("\\|");
        viewHolder.itemImg.setImageURI(split[0]);
        viewHolder.itemTitle.setText(list.get(i).getTitle());
        viewHolder.itemPrice.setText("¥："+list.get(i).getPrice());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClick != null){
                    onClick.click(list.get(i).getPid());
                }
            }
        });
        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onLongClick != null) {
                    list.remove(i);
                    ObjectAnimator animator = new ObjectAnimator();
                    AlphaAnimation alphaAnimation = new AlphaAnimation(1f,0f);
                    animator.setDuration(1000);
                    notifyDataSetChanged();
                    onLongClick.longClick(i);
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_img)
        SimpleDraweeView itemImg;
        @BindView(R.id.item_title)
        TextView itemTitle;
        @BindView(R.id.item_price)
        TextView itemPrice;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
    OnClick onClick;

    public void setOnClick(OnClick onClick) {
        this.onClick = onClick;
    }

    public interface OnClick{
        void click(int pid);
    }

    OnLongClick onLongClick;

    public void setOnLongClick(OnLongClick onClick) {
        onLongClick = onClick;
    }

    public interface OnLongClick{
        void longClick(int i);
    }


}
