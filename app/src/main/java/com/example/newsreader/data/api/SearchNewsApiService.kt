package com.example.newsreader.data.api

import com.example.newsreader.BuildConfig
import com.example.newsreader.data.model.SearchNewsModel
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchNewsApiService {

    /**
     * 查询新闻,key必须放在前面
     * @param word:关键词
     * @param page：页数
     */
    @GET("generalnews")
    suspend fun getSearchNews(
        @Query("key")key:String= BuildConfig.SearchNews_KEY,
        @Query("word")word:String,
        @Query("num")num:Int=10,
        @Query("page")page:Int
    ): SearchNewsModel
}