package com.example.newsdemo.common.di

import android.content.Context
import com.example.newsdemo.BuildConfig
import com.example.newsdemo.common.local.NDRoomDatabase
import com.google.gson.GsonBuilder
import com.example.newsdemo.common.local.Constants.Companion.BASE_URL
import com.example.newsdemo.main.model.MainApi
import com.example.newsdemo.main.model.MainDao
import com.example.newsdemo.main.model.MainRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Provides
    @Singleton
    fun provideOkHttpClient() = if (BuildConfig.DEBUG) {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    } else OkHttpClient
        .Builder()
        .build()


    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .build()

    @Provides
    @Singleton
    fun provideMainApi(retrofit: Retrofit): MainApi = retrofit.create(MainApi::class.java)

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context) =
        NDRoomDatabase.getDatabase(appContext)

    @Singleton
    @Provides
    fun provideMainDao(db: NDRoomDatabase) = db.getMainDao()

    @Singleton
    @Provides
    fun provideMainRepository(
        remoteDataSource: MainApi,
        localDataSource: MainDao
    ) = MainRepository(remoteDataSource, localDataSource)
}