package com.example.baicizhan.adapter

import android.content.Context
import android.net.Uri
import android.text.TextUtils
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import java.io.File
import java.util.Locale

class ImageViewBindingAdapter(var context: Context) {
    companion object {
        @JvmStatic
        @BindingAdapter("media")
        fun setImage(imageView: ImageView, url: String) {
            if (!TextUtils.isEmpty(url)) {
                if (url.lowercase(Locale.getDefault())
                        .endsWith(".jpg") || url.lowercase(Locale.getDefault())
                        .endsWith(".jpeg") || url.lowercase(
                        Locale.getDefault()
                    ).endsWith(".png")
                ) {
                    imageView.setImageURI(Uri.fromFile(File(url)))
                }
                if (url.lowercase(Locale.getDefault()).endsWith(".gif")) {
                    Glide.with(imageView.context)
                        .asGif()
                        .load(File(url))
                        .into(imageView)
                }
                if (url.lowercase(Locale.getDefault()).endsWith(".mp4")) {
                    Glide
                        .with(imageView.context)
                        .load(Uri.fromFile(File(url)))
                        .into(imageView)
                }
            }
        }

        @JvmStatic
        @BindingAdapter("word")
        fun setWord(imageView: ImageView?, word: String?) {
        }
    }
}