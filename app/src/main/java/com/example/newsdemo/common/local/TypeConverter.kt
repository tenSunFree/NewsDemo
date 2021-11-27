package com.example.newsdemo.common.local

import androidx.room.TypeConverter
import com.example.newsdemo.main.model.MainArticleSourceEntity

class TypeConverter {

    @TypeConverter
    fun fromMainArticleSourceEntity(entity: MainArticleSourceEntity): String {
        return entity.name
    }

    @TypeConverter
    fun toMainArticleSourceEntity(name: String): MainArticleSourceEntity {
        return MainArticleSourceEntity(name, name)
    }
}