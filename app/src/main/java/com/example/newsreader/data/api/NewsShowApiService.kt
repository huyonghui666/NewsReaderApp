package com.example.newsreader.data.api

import com.example.newsreader.BuildConfig
import com.example.newsreader.data.model.NewsShowResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface NewsShowApiService {

    /**
     * 获取指定频道的新闻
     * @param channel 频道（新闻、娱乐、体育、财经、军事、科技、手机、数码、时尚、游戏、教育、健康、旅游）
     * @param page 页数
     */
    @Headers("Authorization:APPCODE ${BuildConfig.NEWS_SHOW_API_KEY}")
    @GET("news")
    suspend fun getNewsShow(
        @Query("channel")channel:String,
        @Query("page")page:String
    ): NewsShowResponse
}