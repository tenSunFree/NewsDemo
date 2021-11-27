package com.example.newsdemo.main.model

data class MainNewsResponse(
    val articles: MutableList<MainArticleEntity>,
    val status: String,
    val totalResults: Int
)