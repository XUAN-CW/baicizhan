package com.example.baicizhan.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class BaiciZhanTypeConverter {
    private val gson = Gson()
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    @TypeConverter
    fun fromListString(value: List<String>?): String? {
        if(value == null){
            return null
        }
        return  gson.toJson(value)
    }

    @TypeConverter
    fun toListString(value: String?): List<String>? {
        if(value == null){
            return null
        }
        val listType = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun fromString(value: String?): LocalDateTime? {
        return if (value == null) {
            null
        } else LocalDateTime.parse(value, formatter)
    }

    @TypeConverter
    fun toString(date: LocalDateTime?): String? {
        return date?.format(formatter)
    }
}
