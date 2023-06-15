package com.example.baicizhan.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.baicizhan.converter.ListStringConverter
import com.example.baicizhan.dao.WordResourceDao
import com.example.baicizhan.entity.WordResource
import com.example.baicizhan.util.BaicizhanPathUtil

@Database(entities = [WordResource::class], version = 1,exportSchema = false)
@TypeConverters(ListStringConverter::class)
abstract class BaicizhanDatabase : RoomDatabase() {
    abstract fun wordResourceDao(): WordResourceDao

    companion object {
        @Volatile
        private var INSTANCE: BaicizhanDatabase? = null

        fun getInstance(context: Context): BaicizhanDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    BaicizhanDatabase::class.java, BaicizhanPathUtil.getDatabaseFile().absolutePath
                )
                    .allowMainThreadQueries()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
