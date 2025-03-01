package com.example.newsreader.domain.models

import com.squareup.moshi.Json

//时间，标题，来源，图片，新闻URL
data class SearchNews(
    @Json(name = "ctime")val time:String,
    @Json(name = "title")val title:String,
    @Json(name = "source")val source:String,
    @Json(name = "picUrl")val picUrl:String?,
    @Json(name = "url")val url:String?)