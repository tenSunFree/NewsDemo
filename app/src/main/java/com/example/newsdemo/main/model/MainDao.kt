package com.example.newsdemo.main.model

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MainDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMainArticleEntityList(list: List<MainArticleEntity>)

    @Query("SELECT * FROM main_article")
    fun getMainArticleEntityList(): LiveData<List<MainArticleEntity>>

    @Query("Delete FROM main_article")
    suspend fun deleteMainArticleEntityList()
}