package com.example.baicizhan.adapter;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import java.io.File;

public class ImageViewBindingAdapter {
    Context context;

    public ImageViewBindingAdapter(Context context) {
        this.context = context;
    }
    @BindingAdapter("media")
    public static void setImage(ImageView imageView, String url){
        if(!TextUtils.isEmpty(url)){
            if(url.toLowerCase().endsWith(".jpg") || url.toLowerCase().endsWith(".jpeg") || url.toLowerCase().endsWith(".png")){
                imageView.setImageURI(Uri.fromFile(new File(url)));
            }
            if(url.toLowerCase().endsWith(".gif")){
                Glide.with(imageView.getContext())
                        .asGif()
                        .load(new File(url))
                        .into(imageView);
            }
            if(url.toLowerCase().endsWith(".mp4")){
                Glide
                        .with( imageView.getContext() )
                        .load(Uri.fromFile(new File(url)))
                        .into( imageView );
            }
        }
    }

    @BindingAdapter("word")
    public static void setWord(ImageView imageView, String word){
    }

}
