package com.example.baicizhan.adapter

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.VideoView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import java.io.File
import java.util.Locale
import java.util.concurrent.TimeUnit

class ImageViewBindingAdapter(var context: Context) {
    companion object {
        @JvmStatic
        @BindingAdapter("imageUrl")
        fun setImage(imageView: ImageView, url: String) {
            Log.i("imageUrl",url)

            if (!TextUtils.isEmpty(url)) {
                if (url.lowercase(Locale.getDefault())
                        .endsWith(".jpg") || url.lowercase(Locale.getDefault())
                        .endsWith(".jpeg") || url.lowercase(
                        Locale.getDefault()
                    ).endsWith(".png")
                ) {
                    imageView.setImageURI(Uri.fromFile(File(url)))
                }
                if (url.lowercase().endsWith(".gif")) {
                    Glide.with(imageView.context)
                        .asGif()
                        .load(File(url))
                        .into(imageView)
                }
                if (url.lowercase().endsWith(".mp4")) {
                    Log.i("imageUrl","mp4 " + url)
                    val retriever = MediaMetadataRetriever()
                    retriever.setDataSource(url)
                    val durationString = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                    retriever.release()
                    val duration = durationString?.toLong()
                    if (duration != null) {
                        Glide.with(imageView.context)
                            .asBitmap()
                            .load(Uri.fromFile(File(url)))
                            .frame(TimeUnit.MILLISECONDS.toMicros(duration / 3))
                            .into(imageView)
                    }else{
                        Glide
                            .with(imageView.context)
                            .asBitmap()
                            .load(Uri.fromFile(File(url)))
                            .into(imageView)
                    }

                }
            }
        }

//        @JvmStatic
//        @BindingAdapter("videoUrl")
//        fun setImage(videoView: VideoView, url: String) {
//            if (!TextUtils.isEmpty(url)) {
//                videoView.visibility = View.VISIBLE
//
//                if(url.lowercase(Locale.getDefault()).endsWith(".mp4")){
//
//                    val video = File(url)
//
//
//                    videoView.setVideoURI(Uri.parse(video.absolutePath))
//
//                    // Remove the sound
//                    videoView.setOnPreparedListener { mediaPlayer ->
//                        mediaPlayer.setVolume(0f, 0f)
//                    }
//
//                    // Remove the media controller bar
//                    videoView.setMediaController(null)
//
//                    // Loop the video
//                    videoView.setOnCompletionListener { videoView.start() }
//
//                    videoView.start()
//                }else{
//                    videoView.visibility = View.INVISIBLE
//                }
//
//            }
//        }
    }
}