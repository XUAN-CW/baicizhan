package com.example.baicizhan

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.baicizhan.database.BaicizhanDatabase
import com.example.baicizhan.entity.LearningRecord
import com.example.baicizhan.entity.WordResource
import com.example.baicizhan.util.BaicizhanPathUtil
import com.example.baicizhan.util.IdUtil
import com.example.baicizhan.util.WordResourceDirPathUtil
import com.google.gson.Gson
import org.apache.commons.io.FileUtils
import org.apache.commons.lang3.ArrayUtils
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.PullCommand
import org.eclipse.jgit.api.ResetCommand
import org.eclipse.jgit.lib.ObjectId
import org.eclipse.jgit.lib.TextProgressMonitor
import org.eclipse.jgit.storage.file.FileRepositoryBuilder
import java.io.File
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.ArrayList
import java.util.Random


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        BaicizhanPathUtil.init(applicationContext)


        findViewById<Button>(R.id.scanWordResource).setOnClickListener{ scanWordResourceDir() }

        val doTodayPlanButton = findViewById<Button>(R.id.doTodayPlan)
        doTodayPlanButton.setOnClickListener {
            val intent = Intent(this@MainActivity, ZhanActivity::class.java)
            startActivity(intent)
        }

        var a =LearningRecord(IdUtil.generateLongId(),"KK", LocalDateTime.now(),
            LearningRecord.Event.LOOK_AT_THE_PICTURE_AND_CHOOSE_THE_WORD.toString(),0);

        val learningRecordDao = BaicizhanDatabase.getInstance(this).learningRecordDao()

        learningRecordDao.insert(a)

        for (learningRecord in learningRecordDao.getAllLearningRecord()) {
            Log.i("learningRecord",learningRecord.createTime.toString())
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