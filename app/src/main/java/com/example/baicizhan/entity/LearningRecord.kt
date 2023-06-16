package com.example.baicizhan.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

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
    var isRight: Int
){

    enum class Event(private val event: String) {
        LOOK_AT_THE_PICTURE_AND_CHOOSE_THE_WORD("看图选词");
        override fun toString(): String {
            return event
        }
    }
}