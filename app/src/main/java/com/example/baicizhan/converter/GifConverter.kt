package com.example.baicizhan.converter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import pl.droidsonroids.gif.GifOptions
import java.io.FileOutputStream
import java.io.InputStream

class GifConverter(private val context: Context) {

    fun convertMp4ToGif(inputStream: InputStream, outputPath: String) {
        val gifEncoder = GifEncoder()
        val gifOptions = GifOptions()
        val bitmapOptions = BitmapFactory.Options()
        val bitmap = BitmapFactory.decodeStream(inputStream, null, bitmapOptions)

        gifEncoder.start(outputPath)
        gifEncoder.setOptions(gifOptions)

        gifEncoder.addFrame(bitmap)

        gifEncoder.finish()
    }
}
