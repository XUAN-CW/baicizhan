package com.example.baicizhan.dao

import androidx.room.*
import com.example.baicizhan.entity.WordResource

@Dao
interface WordResourceDao {
    @Insert
    fun insert(wordResource: WordResource)

    @Query("SELECT * FROM word_resource")
    fun getAllWordResource(): List<WordResource>

    @Query("SELECT * FROM word_resource WHERE word = :studentId")
    fun getWordResourceById(studentId: Int): WordResource?

    @Update
    fun update(wordResource: WordResource)

    @Delete
    fun delete(wordResource: WordResource)
}
