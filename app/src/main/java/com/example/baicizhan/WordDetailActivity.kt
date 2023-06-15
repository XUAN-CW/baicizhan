package com.example.baicizhan

import WordDetailAdapter
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.example.baicizhan.constaint.Constaints
import com.example.baicizhan.databinding.ActivityWordDetailBinding
import com.example.baicizhan.entity.WordResource
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class WordDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_word_detail)

        val json = intent.getStringExtra(Constaints.learnedWordList)

        val gson = Gson()
        val wordResourceList : List<WordResource> = gson.fromJson(json, object : TypeToken<List<WordResource>>() {}.type)


        val binding = ActivityWordDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val viewPager: ViewPager2 = binding.viewPager
        val adapter = WordDetailAdapter(wordResourceList)
        viewPager.adapter = adapter
        viewPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            var lastState = 0
            var currentPosition : Int? = null
            override fun onPageSelected(position: Int) {
                Log.i("registerOnPageChangeCallback ", "onPageSelected $position")
                currentPosition = position
            }

            override fun onPageScrollStateChanged(state: Int) {
                Log.i("registerOnPageChangeCallback", "onPageScrollStateChanged $state")
                super.onPageScrollStateChanged(state)
                if(state == ViewPager2.SCROLL_STATE_IDLE
                    && lastState == ViewPager2.SCROLL_STATE_DRAGGING
                    && currentPosition == 0){
                    finish()
                }
                lastState = state
            }
        })

    }
}