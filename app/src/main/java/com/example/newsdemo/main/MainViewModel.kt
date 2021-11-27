/*
 * *
 *  * Created by Rafsan Ahmad on 9/27/21, 5:30 PM
 *  * Copyright (c) 2021 . All rights reserved.
 *
 */

package com.example.newsdemo.main

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsdemo.common.local.Constants
import com.example.newsdemo.common.remote.NetworkState
import com.example.newsdemo.common.util.CoroutinesDispatcherProvider
import com.example.newsdemo.common.util.NetworkHelper
import com.example.newsdemo.main.model.MainArticleEntity
import com.example.newsdemo.main.model.MainRepository
import com.example.newsdemo.main.model.MainNewsResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MainRepository,
    private val networkHelper: NetworkHelper,
    private val coroutinesDispatcherProvider: CoroutinesDispatcherProvider
) : ViewModel() {

    companion object {
        private const val TAG: String = "MainViewModel"
    }

    private val _responseNetworkState = MutableLiveData<NetworkState<MainNewsResponse>>()
    val responseNetworkState: LiveData<NetworkState<MainNewsResponse>>
        get() = _responseNetworkState
    private val _errorToast = MutableLiveData<String>()
    val errorToast: LiveData<String>
        get() = _errorToast
    private var mainNewsResponse: MainNewsResponse? = null
    var feedNewsPage = 1

    init {
        fetchNews(Constants.CountryCode)
    }

    fun fetchNews(countryCode: String) {
        if (networkHelper.isNetworkConnected()) {
            _responseNetworkState.postValue(NetworkState.LoadingNetworkState())
            val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
                onError(exception)
            }
            viewModelScope.launch(coroutinesDispatcherProvider.io + coroutineExceptionHandler) {
                when (val response = repository.getNews(countryCode, feedNewsPage)) {
                    is NetworkState.SuccessNetworkState -> {
                        _responseNetworkState.postValue(handleFeedNewsResponse(response))
                    }
                    is NetworkState.ErrorNetworkState -> {
                        _responseNetworkState.postValue(
                            NetworkState.ErrorNetworkState(
                                null,
                                response.message ?: "Error"
                            )
                        )
                    }
                }
            }
        } else {
            _errorToast.value = "No internet available"
        }
    }

    private fun handleFeedNewsResponse(responseMain: NetworkState<MainNewsResponse>): NetworkState<MainNewsResponse> {
        responseMain.data?.let { resultResponse ->
            if (mainNewsResponse == null) {
                feedNewsPage = 2
                mainNewsResponse = resultResponse
            } else {
                feedNewsPage++
                val oldArticles = mainNewsResponse?.articles
                val newArticles = resultResponse.articles
                oldArticles?.addAll(newArticles)
            }
            mainNewsResponse?.let {
                mainNewsResponse = convertPublishedDate(it)
            }
            return NetworkState.SuccessNetworkState(mainNewsResponse ?: resultResponse)
        }
        return NetworkState.ErrorNetworkState(null, "No data found")
    }

    private fun convertPublishedDate(currentResponseMain: MainNewsResponse): MainNewsResponse {
        currentResponseMain.let { response ->
            for (i in 0 until response.articles.size) {
                val publishedAt = response.articles[i].publishedAt
                publishedAt?.let {
                    val converted = formatDate(it)
                    response.articles[i].publishedAt = converted
                }
            }
        }
        return currentResponseMain
    }

    @SuppressLint("SimpleDateFormat")
    private fun formatDate(strCurrentDate: String): String {
        var convertedDate = ""
        try {
            if (strCurrentDate.isNotEmpty() && strCurrentDate.contains("T")) {
                var format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                val newDate: Date? = format.parse(strCurrentDate)
                format = SimpleDateFormat("MMM dd, yyyy hh:mm a")
                newDate?.let {
                    convertedDate = format.format(it)
                }
            } else {
                convertedDate = strCurrentDate
            }
        } catch (e: Exception) {
            e.message?.let {
                Log.e(TAG, it)
            }
            convertedDate = strCurrentDate
        }
        return convertedDate
    }

    fun hideErrorToast() {
        _errorToast.value = ""
    }

    fun insertMainArticleEntityList(mainEntities: List<MainArticleEntity>) {
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
            onError(exception)
        }
        viewModelScope.launch(coroutinesDispatcherProvider.io + coroutineExceptionHandler) {
            repository.deleteAllNews()
            repository.insertMainArticleEntityList(mainEntities)
        }
    }

    fun getMainArticleEntityList() = repository.getMainArticleEntityList()

    private fun onError(throwable: Throwable) {
        _errorToast.value = throwable.message
    }
}