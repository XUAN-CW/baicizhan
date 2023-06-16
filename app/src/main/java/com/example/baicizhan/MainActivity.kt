package com.example.baicizhan

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.baicizhan.database.BaicizhanDatabase
import com.example.baicizhan.entity.LearningRecord
import com.example.baicizhan.util.BaicizhanPathUtil
import com.example.baicizhan.util.IdUtil
import com.example.baicizhan.util.WordResourceDirPathUtil
import com.google.common.io.Files
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        BaicizhanPathUtil.init(applicationContext)


        findViewById<Button>(R.id.scanWordResource).setOnClickListener{ scanWordResourceDir() }

        val doTodayPlanButton = findViewById<Button>(R.id.doTodayPlan)
        doTodayPlanButton.setOnClickListener {
            val intent = Intent(this@MainActivity, TodayPlanActivity::class.java)
            startActivity(intent)
        }


        findViewById<Button>(R.id.shareDatabaseFile).setOnClickListener {
            shareFile(BaicizhanPathUtil.getDatabaseFile())

        }

    }



    private fun shareFile(file: File) {
        val backup = File(file.parent,LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss_")) + file.name)
        Files.copy(file, backup)
        val uri = FileProvider.getUriForFile(this, "com.example.baicizhan.fileprovider", backup)
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "application/octet-stream"
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivity(Intent.createChooser(shareIntent, "Share File"))
    }




    private fun scanWordResourceDir(){
        Log.i("scanWordResourceDir","scanWordResourceDir")

        val wordResourceList = WordResourceDirPathUtil.getWordResourceList(BaicizhanPathUtil.getWordResourceRootDir())
        val baicizhanDatabase = BaicizhanDatabase.getInstance(this)
        for (wordResource in baicizhanDatabase.wordResourceDao().getAllWordResource()) {
            Log.i("check wordResource",wordResource.word)
            baicizhanDatabase.wordResourceDao().delete(wordResource)
        }
        for (wordResource in wordResourceList) {
            try {
                Log.i("scanWordResourceDir",wordResource.wordResourceDir.toString())
                baicizhanDatabase.wordResourceDao().insert(wordResource)
            }catch (e : android.database.sqlite.SQLiteConstraintException){

            }
        }
    }


}