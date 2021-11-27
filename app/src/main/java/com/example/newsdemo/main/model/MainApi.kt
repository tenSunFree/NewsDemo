package com.example.newsdemo.main.model

import com.example.newsdemo.common.local.Constants.Companion.API_KEY
import com.example.newsdemo.common.local.Constants.Companion.CountryCode
import com.example.newsdemo.common.local.Constants.Companion.QUERY_PER_PAGE
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MainApi {

    @GET("v2/top-headlines")
    suspend fun getMainNewsResponse(
        @Query("country")
        countryCode: String = CountryCode,
        @Query("page")
        pageNumber: Int = 1,
        @Query("pageSize")
        pageSize: Int = QUERY_PER_PAGE,
        @Query("apiKey")
        apiKey: String = API_KEY
    ): Response<MainNewsResponse>
}