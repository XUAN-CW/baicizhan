package com.example.baicizhan.entity

import android.text.TextUtils
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.baicizhan.util.PathUtil
import java.io.File
@Entity(tableName = "word_resource")
data class WordResource (
    @PrimaryKey
    var word: String,
    val shortMean: String,
    val usphone: String?,
    @ColumnInfo(name = "mean_list")  val meanList: List<String>?,
    var image: String?,
    val prototype: String?,
    var createTime : String?,
    var updateTime : String?

){
    fun getUsSpeechFile() : File {
        return PathUtil.getUsSpeechFile(word)
    }
    fun getWordDir() : File {
        return File(PathUtil.getWordResourceRootDir(),word)
    }

    fun getMeanListString(): String?{
        return meanList?.let { TextUtils.join("\n", it) }
    }

    fun getWordOnSpace() : String{
        return word.replace("_".toRegex(), " ")
    }
}