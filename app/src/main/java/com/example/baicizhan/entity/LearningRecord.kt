package com.example.baicizhan.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "learning_record")
data class LearningRecord (
    @PrimaryKey
    var id: Long,
    var word: String,
    var createTime : LocalDateTime,
    var event: String,
    /**
     * 是否回答正确
     */
    var isCorrect: Int?
){

    enum class Event(private val event: String) {
        LOOK_AT_THE_PICTURE_AND_CHOOSE_THE_WORD("看图选词");
        override fun toString(): String {
            return event
        }
    }
}