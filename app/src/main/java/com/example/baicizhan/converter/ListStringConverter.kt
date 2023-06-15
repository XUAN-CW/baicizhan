package com.example.baicizhan.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ListStringConverter {
    private val gson = Gson()

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
}
