package com.example.baicizhan.adapter

import android.content.Context
import android.net.Uri
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.VideoView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import java.io.File
import java.util.Locale

class ImageViewBindingAdapter(var context: Context) {
    companion object {
        @JvmStatic
        @BindingAdapter("imageUrl")
        fun setImage(imageView: ImageView, url: String) {
            if (!TextUtils.isEmpty(url)) {
                if (url.lowercase(Locale.getDefault())
                        .endsWith(".jpg") || url.lowercase(Locale.getDefault())
                        .endsWith(".jpeg") || url.lowercase(
                        Locale.getDefault()
                    ).endsWith(".png")
                ) {
                    imageView.setImageURI(Uri.fromFile(File(url)))
                    return
                }
                if (url.lowercase(Locale.getDefault()).endsWith(".gif")) {
                    Glide.with(imageView.context)
                        .asGif()
                        .load(File(url))
                        .into(imageView)
                    return
                }
                imageView.visibility = View.GONE
                return
            }
        }

        @JvmStatic
        @BindingAdapter("videoUrl")
        fun setImage(videoView: VideoView, url: String) {
            if (!TextUtils.isEmpty(url)) {

                if(url.lowercase(Locale.getDefault()).endsWith(".mp4")){

                    val video = File(url)


                    videoView.setVideoURI(Uri.parse(video.absolutePath))

                    // Remove the sound
                    videoView.setOnPreparedListener { mediaPlayer ->
                        mediaPlayer.setVolume(0f, 0f)
                    }

                    // Remove the media controller bar
                    videoView.setMediaController(null)

                    // Loop the video
                    videoView.setOnCompletionListener { videoView.start() }

                    videoView.start()
                }else{
                    videoView.visibility = View.GONE
                }

            }
        }
    }
}