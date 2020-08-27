package com.hbs.burnout.model.dao

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hbs.burnout.model.ShareResult
import java.lang.reflect.Type


public class Converters {
    @TypeConverter
    public fun fromString(value: String?): List<ShareResult.Result> {
        val listType: Type = object : TypeToken<ArrayList<ShareResult.Result>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    public fun fromArrayList(list: List<ShareResult.Result>): String {
        return Gson().toJson(list)
    }
}
