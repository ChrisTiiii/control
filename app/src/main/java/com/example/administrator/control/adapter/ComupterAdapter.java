package com.example.administrator.control.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.control.R;
import com.example.administrator.control.bean.EqupmentBean;

import java.util.List;


import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * author: ZhongMing
 * DATE: 2019/1/14 0014
 * Description:
 **/
public class ComupterAdapter extends RecyclerView.Adapter<ComupterAdapter.ComputerViewHolder> {
    private Context context;
    private List<EqupmentBean> list;
    // 利用接口 -> 给RecyclerView设置点击事件
    private ItemClickListener mItemClickListener;
    private int _position;

    public void update(List<EqupmentBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public int getPosition() {
        return _position;
    }

    public void setPosition(int _position) {
        this._position = _position;
        notifyDataSetChanged();
    }

    public ComupterAdapter(Context context, List<EqupmentBean> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public ComputerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.computer_item, null);
        return new ComputerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ComputerViewHolder computerViewHolder, final int position) {
        Glide.with(context).load(R.drawable.computer).into(computerViewHolder.imgComputer);
        computerViewHolder.tvComputerid.setText(list.get(position).getName());
        // 点击事件一般都写在绑定数据这里，当然写到上边的创建布局时候也是可以的
        if (mItemClickListener != null) {
            computerViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 这里利用回调来给RecyclerView设置点击事件
                    mItemClickListener.onItemClick(position);
                }
            });
        }
        if (position == getPosition()) {
            computerViewHolder.tvComputerid.setTextColor(Color.parseColor("#FA8072"));
        } else {
            computerViewHolder.tvComputerid.setTextColor(Color.parseColor("#1F1F1F"));
        }
    }

    @Override
    public int getItemCount() {
        return list.size() != 0 ? list.size() : 0;
    }

    public class ComputerViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_computerid)
        TextView tvComputerid;
        @BindView(R.id.img_computer)
        ImageView imgComputer;

        ComputerViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface ItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(ItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;

    }


}
