package com.example.baicizhan

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.VideoView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import com.example.baicizhan.constaint.Constaints
import com.example.baicizhan.dao.LearningRecordDao
import com.example.baicizhan.dao.WordResourceDao
import com.example.baicizhan.database.BaicizhanDatabase
import com.example.baicizhan.databinding.ActivityZhanBinding
import com.example.baicizhan.entity.LearningRecord
import com.example.baicizhan.entity.WordResource
import com.example.baicizhan.player.Mp3Player
import com.example.baicizhan.util.IdUtil
import com.google.android.exoplayer2.upstream.RawResourceDataSource
import com.google.gson.Gson
import java.io.File
import java.time.LocalDateTime
import java.util.*


class TodayPlanActivity : AppCompatActivity() {

    var choiceWordResourceArray: MutableLiveData<Array<WordResource>> = MutableLiveData()
    var lastWordResource: MutableLiveData<WordResource> = MutableLiveData()
    lateinit var wordResourceArray: Array<WordResource>
    var currentWordResource : MutableLiveData<Int> = MutableLiveData(0)
    var continuousCorrect : MutableLiveData<Int> = MutableLiveData(0)

    private lateinit var mp3Player : Mp3Player

    private lateinit var wordResourceDao: WordResourceDao
    private lateinit var learningRecordDao: LearningRecordDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zhan)

        initData()
        // 绑定
        val activityZhanBinding: ActivityZhanBinding = DataBindingUtil.setContentView(this, R.layout.activity_zhan)
        activityZhanBinding.todayPlanViewModel = this
        activityZhanBinding.lifecycleOwner = this


        gestureDetector = GestureDetector(this, MyGestureListener())



        val videoView = findViewById<VideoView>(R.id.videoView)

        val video = File("/storage/emulated/0/Android/data/com.example.baicizhan/files/wordResourceRoot/final-1.mp4")

//        val mediaController = MediaController(this)
//        mediaController.setAnchorView(videoView)
//        videoView.setMediaController(mediaController)
//        videoView.setVideoURI(Uri.parse(video.absolutePath))
//        videoView.requestFocus()


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
    }

    private fun initData(){
        wordResourceDao = BaicizhanDatabase.getInstance(application).wordResourceDao()
        learningRecordDao = BaicizhanDatabase.getInstance(application).learningRecordDao()

        mp3Player = Mp3Player(application)

        wordResourceArray = wordResourceDao.getAllWordResource().shuffled().toTypedArray()
        currentWordResource = MutableLiveData(wordResourceArray.size - 1)
        if(wordResourceArray.isNotEmpty()){
            currentWordResource.observeForever {value ->
                mp3Player.playMp3(Uri.parse(wordResourceArray[value].usSpeechFile))
                setChoiceWordResourceListCurrentWordResource()
            }
        }
    }



    fun forcePlayWord(usSpeechFile: String){
        mp3Player.forcePlayMp3(Uri.parse(usSpeechFile))
    }

    private fun setChoiceWordResourceListCurrentWordResource() {
        val fourWordResource = wordResourceArray.copyOf().toList().shuffled().take(4).toTypedArray()

        for (wordResource in fourWordResource) {
            if(wordResource.word == wordResourceArray[currentWordResource.value!!].word){
                choiceWordResourceArray.value = fourWordResource
                return
            }
        }
        fourWordResource[Random().nextInt(3)] = wordResourceArray[currentWordResource.value!!]
        choiceWordResourceArray.value = fourWordResource
    }

    fun choice(word: String) {
        Log.i("choice", word)
        if (wordResourceArray[currentWordResource.value!!].word == word) {
            learningRecordDao.insert(LearningRecord(IdUtil.generateLongId(),word, LocalDateTime.now(),
                LearningRecord.Event.LOOK_AT_THE_PICTURE_AND_CHOOSE_THE_WORD.toString(),1))
            mp3Player.forcePlayMp3(RawResourceDataSource.buildRawResourceUri(R.raw.rightanswer))
            if(currentWordResource.value!! > 0){
                lastWordResource.value = wordResourceArray[currentWordResource.value!!]
                currentWordResource.value = currentWordResource.value!! - 1
                continuousCorrect.value = continuousCorrect.value!! + 1
            }else{
                val dialogBuilder = AlertDialog.Builder(this)
                val dialog = dialogBuilder.setMessage("全部完成！"+ (continuousCorrect.value?.plus(1)) +"连击！")
                    .setCancelable(false)
                    .setPositiveButton("Yes") { dialog, id ->
                        // User clicked Yes button
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }.show()
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.GREEN)
            }
        }else{

            learningRecordDao.insert(LearningRecord(IdUtil.generateLongId(),word, LocalDateTime.now(),
                LearningRecord.Event.LOOK_AT_THE_PICTURE_AND_CHOOSE_THE_WORD.toString(),0))
            continuousCorrect.value = 0
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event)
    }
    private lateinit var gestureDetector: GestureDetector

    private inner class MyGestureListener : GestureDetector.SimpleOnGestureListener() {

        override fun onFling(
            event1: MotionEvent,
            event2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            if (event1.rawY - event2.rawY > 100) {
                Log.i("GestureListener","手势向上滑动")
            }
            if (event2.rawY - event1.rawY > 100) {
                Log.i("GestureListener","手势向下滑动")
                val subArray: Array<WordResource>? = currentWordResource.value?.let {
                    wordResourceArray.copyOfRange(it, wordResourceArray.size)
                }
                val gson = Gson()
                val json = gson.toJson(subArray)
                val intent = Intent(this@TodayPlanActivity, WordDetailActivity::class.java)
                intent.putExtra(Constaints.learnedWordList, json)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_bottom)

            }
            if ((event1.rawX - event2.rawX) > 100) {
                Log.i("GestureListener","手势向右滑动")
                if(currentWordResource.value!! < wordResourceArray.size - 1){
                    val subArray: Array<WordResource>? = currentWordResource.value?.let {
                        wordResourceArray.copyOfRange(it + 1, wordResourceArray.size)
                    }
                    val gson = Gson()
                    val json = gson.toJson(subArray)
                    val intent = Intent(this@TodayPlanActivity, WordDetailActivity::class.java)
                    intent.putExtra(Constaints.learnedWordList, json)
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left)
                }
            }
            if ((event2.rawX - event1.rawX) > 100) {
                Log.i("GestureListener","手势向左滑动")
            }
            // 消费掉当前事件  不让当前事件继续向下传递
            return true
        }

    }
}