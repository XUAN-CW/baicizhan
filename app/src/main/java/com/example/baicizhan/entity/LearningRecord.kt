package com.example.baicizhan.entity

import android.text.TextUtils
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.File
@Entity(tableName = "learning_record")
data class LearningRecord (
    @PrimaryKey
    var id: Long,
    var word: String,
    var createTime : String,
    var updateTime : String,
    var event: String,
    /**
     * 是否正确
     */
    var right: Int
)