package com.vimalcvs.materialrating.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import com.vimalcvs.materialrating.R;
import com.vimalcvs.materialrating.data.FeedBack;

import java.util.List;

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.MyViewHolder> {

    private List<FeedBack> feedBacks;

    ItemListener itemListener;

    Context context;

    public interface ItemListener {
        void onItemClick(FeedBack feedBack);
    }


    public FeedbackAdapter(Context context, List<FeedBack> data, ItemListener itemListener) {
        this.context = context;
        this.feedBacks = data;
        this.itemListener = itemListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_option, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bind(context, feedBacks, position, itemListener);
    }

    @Override
    public int getItemCount() {
        return feedBacks.size();
    }

    public List<FeedBack> getItems() {
        return feedBacks ;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView mTextView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.text_view);
        }

        @SuppressLint("NotifyDataSetChanged")
        public void bind(Context context, List<FeedBack> feedBacks, int position, ItemListener itemListener) {
            mTextView.setText(feedBacks.get(position).getBody());

            if (!feedBacks.get(position).isCheck()) {
                mTextView.setBackground(AppCompatResources.getDrawable(context, R.drawable.border_item_checked));
            } else {
                mTextView.setBackground(AppCompatResources.getDrawable(context, R.drawable.border_item));
            }

            mTextView.setOnClickListener(view -> {
                feedBacks.get(position).setCheck(!feedBacks.get(position).isCheck());
                itemListener.onItemClick(feedBacks.get(position));
                notifyDataSetChanged();
            });
        }
    }

}
