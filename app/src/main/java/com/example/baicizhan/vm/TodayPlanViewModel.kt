package com.example.baicizhan.vm

import android.app.Application
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.baicizhan.R
import com.example.baicizhan.database.BaicizhanDatabase
import com.example.baicizhan.entity.WordResource
import com.example.baicizhan.player.Mp3Player
import com.google.android.exoplayer2.upstream.RawResourceDataSource
import java.util.Random


class TodayPlanViewModel(application: Application) : AndroidViewModel(application) {
    var choiceWordResourceArray: MutableLiveData<Array<WordResource>> = MutableLiveData()
    var lastWordResource: MutableLiveData<WordResource> = MutableLiveData()
    val wordResourceArray: Array<WordResource>
    var currentWordResource : MutableLiveData<Int> = MutableLiveData(0)
    var continuousCorrect : MutableLiveData<Int> = MutableLiveData(0)

    var isComplete : MutableLiveData<Boolean> = MutableLiveData(false)

    private val mp3Player : Mp3Player


    fun choice(word: String) {
        Log.i("choice", word)
        if (wordResourceArray[currentWordResource.value!!].word == word) {
            mp3Player.forcePlayMp3(RawResourceDataSource.buildRawResourceUri(R.raw.rightanswer))
            if(currentWordResource.value!! > 0){
                lastWordResource.value = wordResourceArray[currentWordResource.value!!]
                currentWordResource.value = currentWordResource.value!! - 1
                continuousCorrect.value = continuousCorrect.value!! + 1
            }else{
                isComplete.value = true
            }
        }else{
            continuousCorrect.value = 0
        }
    }
    init {
        mp3Player = Mp3Player(application)
        this.wordResourceArray = BaicizhanDatabase.getInstance(application).wordResourceDao().getAllWordResource().shuffled().toTypedArray()
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

}