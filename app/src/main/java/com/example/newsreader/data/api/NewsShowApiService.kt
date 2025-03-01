package com.example.newsreader.data.api

import com.example.newsreader.BuildConfig
import com.example.newsreader.data.model.NewsShowResponse
import com.example.newsreader.data.model.SearchNewsModel
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

    /**
     * 查询新闻
     * @param word:关键词
     * @param page：页数
     */
    //https://whyta.cn/api/tx/guonei?key=96f163cda80b&num=10&word=%E9%95%BF%E6%B2%99&page=3
    /*@GET("guonei")
    suspend fun getSearchNews(
        @Query("word")word:String,
        @Query("num")num:Int=10,
        @Query("page")page:Int,
        @Query("key")key:String=BuildConfig.SearchNews_KEY
    ): SearchNewsModel*/
}