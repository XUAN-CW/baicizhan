package com.example.baicizhan.adapter

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.MediaSourceFactory
import java.io.File
import java.util.*
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
                            .frame(TimeUnit.MILLISECONDS.toMicros(duration / 4))
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

        @JvmStatic
        @BindingAdapter("videoUrl")
        fun setImage(playerView: com.google.android.exoplayer2.ui.PlayerView, url: String) {
            if (!TextUtils.isEmpty(url)) {
                if(url.lowercase(Locale.getDefault()).endsWith(".mp4")){
                    val video = File(url)
                    Uri.parse(video.absolutePath)
                    val player = SimpleExoPlayer.Builder(playerView.context).build()
                    playerView.setPlayer(player)
                    val mediaSourceFactory: MediaSourceFactory = DefaultMediaSourceFactory(playerView.context)
                    val mediaItem = MediaItem.fromUri(Uri.parse(video.absolutePath))
                    val mediaSource: MediaSource = mediaSourceFactory.createMediaSource(mediaItem)
                    player.setMediaSource(mediaSource)
                    player.prepare()
                    player.play()
                }

            }
        }
    }
}