package com.example.baicizhan.player

import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import java.util.*
import java.util.concurrent.ArrayBlockingQueue

class Mp3Player(context: Context) {


    private var exoPlayer: SimpleExoPlayer
    private var mediaItemQueue : Queue<Uri> = ArrayBlockingQueue(100)

    init {
        // exoPlayer
        exoPlayer = SimpleExoPlayer.Builder(context).build()
        exoPlayer.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(@Player.State state: Int) {
                if (state == Player.STATE_ENDED) {
                    Log.i("playMp3","Player.STATE_ENDED " + mediaItemQueue.size)
                    if(mediaItemQueue.size > 0){
                        playMp3(null)
                    }
                }
            }
        })
    }

    fun playMp3(uri: Uri?) {
        Log.i("playMp3", "uri = $uri")
        if(uri != null){
            mediaItemQueue.add(uri)
        }
        Log.i("playMp3", mediaItemQueue.size.toString()
                + "|" + exoPlayer.playbackState
                + "|" + Player.STATE_IDLE
                + " " + Player.STATE_READY
                + " " + Player.STATE_ENDED)

        if(exoPlayer.playbackState == Player.STATE_ENDED ||  exoPlayer.playbackState == Player.STATE_IDLE){
            val  target = mediaItemQueue.poll()
            if(target != null){
                Log.i("playMp3","play " +target.toString())
                exoPlayer.setMediaItem(MediaItem.fromUri(target))
                exoPlayer.prepare()
                //当Player处于STATE_READY状态时，进行播放
                exoPlayer.playWhenReady = true

            }
        }else{
            Log.i("playMp3","mediaItemQueue  " + mediaItemQueue.size.toString())
        }
    }

    fun forcePlayMp3(uri: Uri?) {
        Log.i("playMp3", "uri = $uri")
        if(uri == null){
            return
        }
        mediaItemQueue.clear()
        exoPlayer.setMediaItem(MediaItem.fromUri(uri))
        exoPlayer.prepare()
        //当Player处于STATE_READY状态时，进行播放
        exoPlayer.playWhenReady = true
    }
}