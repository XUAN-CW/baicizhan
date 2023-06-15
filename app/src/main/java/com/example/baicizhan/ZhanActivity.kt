package com.example.baicizhan

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.baicizhan.constaint.Constaints
import com.example.baicizhan.database.BaicizhanDatabase
import com.example.baicizhan.databinding.ActivityZhanBinding
import com.example.baicizhan.entity.WordResource
import com.example.baicizhan.vm.TodayPlanViewModel
import com.google.gson.Gson
import org.apache.commons.lang3.ArrayUtils
import java.io.Serializable
import java.util.*


class ZhanActivity : AppCompatActivity() {
    private lateinit var todayPlanViewModel: TodayPlanViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zhan)

        // 构造 todayPlanViewModel
        todayPlanViewModel = ViewModelProvider(this).get(TodayPlanViewModel::class.java)
        todayPlanViewModel.isComplete.observe(this) { isComplete ->
            if (isComplete) {
                val dialogBuilder = AlertDialog.Builder(this)
                val dialog = dialogBuilder.setMessage("全部完成！"+ (todayPlanViewModel.continuousCorrect.value?.plus(1)) +"连击！")
                    .setCancelable(false)
                    .setPositiveButton("Yes") { dialog, id ->
                        // User clicked Yes button
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }.show()
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.GREEN)
            }
        }

        // 绑定
        val activityZhanBinding: ActivityZhanBinding = DataBindingUtil.setContentView(this, R.layout.activity_zhan)
        activityZhanBinding.todayPlanViewModel = todayPlanViewModel
        activityZhanBinding.lifecycleOwner = this
        gestureDetector = GestureDetector(this, MyGestureListener())
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
                val subArray: Array<WordResource>? = todayPlanViewModel.currentWordResource.value?.let {
                    todayPlanViewModel.wordResourceArray.copyOfRange(it, todayPlanViewModel.wordResourceArray.size)
                }
                val gson = Gson()
                val json = gson.toJson(subArray)
                val intent = Intent(this@ZhanActivity, WordDetailActivity::class.java)
                intent.putExtra(Constaints.learnedWordList, json)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_bottom)

            }
            if ((event1.rawX - event2.rawX) > 100) {
                Log.i("GestureListener","手势向右滑动")
                if(todayPlanViewModel.currentWordResource.value!! < todayPlanViewModel.wordResourceArray.size - 1){
                    val subArray: Array<WordResource>? = todayPlanViewModel.currentWordResource.value?.let {
                        todayPlanViewModel.wordResourceArray.copyOfRange(it + 1, todayPlanViewModel.wordResourceArray.size)
                    }
                    val gson = Gson()
                    val json = gson.toJson(subArray)
                    val intent = Intent(this@ZhanActivity, WordDetailActivity::class.java)
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