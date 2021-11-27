package com.example.newsdemo.main.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    tableName = "main_article"
)
data class MainArticleEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    val author: String?,
    val content: String?,
    val description: String?,
    var publishedAt: String?,
    val source: MainArticleSourceEntity?,
    val title: String?,
    val url: String?,
    val urlToImage: String?
) : Serializable