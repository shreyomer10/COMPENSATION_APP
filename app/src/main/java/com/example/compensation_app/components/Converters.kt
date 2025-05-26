package com.example.compensation_app.components

import androidx.room.TypeConverter
import com.example.compensation_app.Backend.StatusUpdate
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    @TypeConverter
    fun fromStatusUpdateList(value: List<StatusUpdate>?): String? {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toStatusUpdateList(value: String?): List<StatusUpdate>? {
        val type = object : TypeToken<List<StatusUpdate>>() {}.type
        return Gson().fromJson(value, type)
    }
}
