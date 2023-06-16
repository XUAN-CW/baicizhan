package com.example.baicizhan.dao

import androidx.room.*
import com.example.baicizhan.entity.LearningRecord
import com.example.baicizhan.entity.WordResource

@Dao
interface LearningRecordDao {
    @Insert
    fun insert(learningRecord: LearningRecord)

    @Query("SELECT * FROM learning_record")
    fun getAllLearningRecord(): List<LearningRecord>

    @Query("SELECT * FROM learning_record WHERE word = :id")
    fun getLearningRecordById(id: Int): LearningRecord?

    @Update
    fun update(learningRecord: LearningRecord)

    @Delete
    fun delete(learningRecord: LearningRecord)
}
