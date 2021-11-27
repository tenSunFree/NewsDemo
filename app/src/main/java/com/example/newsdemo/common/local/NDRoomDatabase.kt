package com.example.newsdemo.common.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.newsdemo.main.model.MainDao
import com.example.newsdemo.main.model.MainArticleEntity

@Database(
    entities = [MainArticleEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(TypeConverter::class)
abstract class NDRoomDatabase : RoomDatabase() {

    abstract fun getMainDao(): MainDao

    companion object {
        @Volatile
        private var instance: NDRoomDatabase? = null

        fun getDatabase(context: Context): NDRoomDatabase =
            instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also {
                    instance = it
                }
            }

        private fun buildDatabase(appContext: Context) =
            Room.databaseBuilder(appContext, NDRoomDatabase::class.java, "nd_db")
                .fallbackToDestructiveMigration()
                .build()
    }
}