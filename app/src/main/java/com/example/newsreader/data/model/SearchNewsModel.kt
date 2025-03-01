package com.example.newsreader.data.model

import com.squareup.moshi.Json


data class SearchNewsModel(
    @Json(name = "result")val result:Result)
//页数从1开始
data class Result(
    @Json(name = "curpage")val curpage:Int,
    @Json(name = "newslist")val newslist:List<SearchNews>)
//时间，标题，来源，图片，新闻URL
data class SearchNews(
    @Json(name = "ctime")val time:String,
    @Json(name = "title")val title:String,
    @Json(name = "source")val source:String,
    @Json(name = "picUrl")val picUrl:String,
    @Json(name = "url")val url:String)