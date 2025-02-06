package us.smt.mylearningcentre.data.database.local.room.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class StringListConverter {
    // Convert List<String> to a String (JSON format)
    @TypeConverter
    fun fromList(list: List<String>?): String? {
        return Gson().toJson(list)
    }

    // Convert a String (JSON format) back to List<String>
    @TypeConverter
    fun toList(data: String?): List<String>? {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(data, listType)
    }

}