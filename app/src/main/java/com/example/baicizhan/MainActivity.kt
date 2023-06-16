package com.example.baicizhan

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.baicizhan.database.BaicizhanDatabase
import com.example.baicizhan.entity.LearningRecord
import com.example.baicizhan.util.BaicizhanPathUtil
import com.example.baicizhan.util.IdUtil
import com.example.baicizhan.util.WordResourceDirPathUtil
import java.time.LocalDateTime


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