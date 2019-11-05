package com.yzy.example.widget.discretescrollview.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.yzy.example.widget.discretescrollview.holder.DiscreteHolder;
import com.yzy.example.widget.discretescrollview.holder.DiscreteHolderCreator;
import com.yzy.example.widget.discretescrollview.listener.OnItemClickListener;

import org.jetbrains.annotations.NotNull;

import java.util.List;


/**
 * Description:
 *
 * @author: caiyoufei
 * @date: 2019/10/14 12:09
 */
public class DiscretePageAdapter<T> extends RecyclerView.Adapter<DiscreteHolder<T>> {
    private List<T> datas;
    private DiscreteHolderCreator creator;
    private OnItemClickListener onItemClickListener;

    public DiscretePageAdapter(DiscreteHolderCreator creator, List<T> datas) {
        this.creator = creator;
        this.datas = datas;
    }

    @SuppressWarnings("unchecked")
    @NotNull
    @Override
    public DiscreteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId = creator.getLayoutId();
        View itemView = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        return creator.createHolder(itemView);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(DiscreteHolder holder, final int position) {
        holder.updateUI(datas.get(position), position, getItemCount());

        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(position, datas.get(position));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
