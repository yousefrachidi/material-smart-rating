package com.vimalcvs.materialrating.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import com.vimalcvs.materialrating.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private List<File> images;
    private Context context;
    private ItemListener listener;

    public interface ItemListener {
        void onItemDeleteClick(File file);

        void onItemSelectedClick();
    }


    public ImageAdapter(Context context, ItemListener listener) {
        this.images = new ArrayList<>(Arrays.asList(new File("")));
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pick_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public List<File> getItems() {
        return images;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void addImage(File file) {
        images.add(file);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView, close;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.icon);
            close = itemView.findViewById(R.id.close);
        }

        @SuppressLint("NotifyDataSetChanged")
        public void bind(int position) {
            File image = images.get(position);
            if (image.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath());
                imageView.setImageBitmap(bitmap);
            } else {
                close.setVisibility(View.GONE);
                imageView.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.icon_add_photo));
            }


            close.setOnClickListener(view -> {
                images.remove(position);
                notifyDataSetChanged();
            });

            imageView.setOnClickListener(view -> {
//                if (image.exists()) {
//                    images.remove(position);
//                    notifyDataSetChanged();
//                }else {
//
//                }

                listener.onItemSelectedClick();

            });
        }
    }
}

