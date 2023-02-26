package com.vimalcvs.materialrating.utilis;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

public class Utils {


    @SuppressLint("CheckResult")
    public static void loadImgEveyTime(Context context, View parent, ImageView imgView, String url){
        RequestBuilder<Drawable> requestBuilder = Glide.with(context)
                .load(url);
        requestBuilder.addListener(new RequestListener<Drawable>() {
            @SuppressLint("CheckResult")
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                Toast.makeText(context, "handle loading failure", Toast.LENGTH_SHORT).show();
                return false;
            }

            @SuppressLint("CheckResult")
            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                // handle loading success
                Toast.makeText(context, "handle loading success", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        requestBuilder.into(imgView);

    }
}
