package com.example.administrator.control.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.control.R;

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
    private List<String> list;

    public ComupterAdapter(Context context, List<String> list) {
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
    public void onBindViewHolder(@NonNull ComputerViewHolder computerViewHolder, int i) {
        Glide.with(context).load(R.drawable.computer).into(computerViewHolder.imgComputer);
        computerViewHolder.tvComputerid.setText(list.get(i));

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
}
