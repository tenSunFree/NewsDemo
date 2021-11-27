package com.example.newsdemo.main.model

import com.example.newsdemo.common.remote.NetworkState
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val api: MainApi,
    private val dao: MainDao
) {

    suspend fun getNews(countryCode: String, pageNumber: Int): NetworkState<MainNewsResponse> {
        return try {
            val response = api.getMainNewsResponse(countryCode, pageNumber)
            val body = response.body()
            if (response.isSuccessful && body != null) {
                NetworkState.SuccessNetworkState(body)
            } else {
                NetworkState.ErrorNetworkState(null, "An error occurred")
            }
        } catch (e: Exception) {
            NetworkState.ErrorNetworkState(null, "Error occurred ${e.localizedMessage}")
        }
    }

    fun insertMainArticleEntityList(list: List<MainArticleEntity>) =
        dao.insertMainArticleEntityList(list)

    fun getMainArticleEntityList() = dao.getMainArticleEntityList()

    suspend fun deleteAllNews() = dao.deleteMainArticleEntityList()
}