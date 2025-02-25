package com.example.newsreader.data.api

import com.example.newsreader.BuildConfig
import com.example.newsreader.data.model.SearchNewsModel
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchNewsApiService {

    @GET("guonei?key=${BuildConfig.SearchNews_KEY}&num=50&page=1")
    suspend fun getSearchNews(@Query("word")word:String):SearchNewsModel

}