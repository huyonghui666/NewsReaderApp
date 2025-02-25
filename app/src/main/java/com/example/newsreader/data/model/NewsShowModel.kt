package com.example.newsreader.data.model

import com.squareup.moshi.Json

data class NewsShowResponse(
    @Json(name = "data")val newsShowData:List<NewsShowModel>?
)

data class NewsShowModel(
    @Json(name = "ptime")val time:String?,
    @Json(name = "title")val title:String?,
    @Json(name = "imgsrc")val imgsrc:String?,
    @Json(name = "url")val url:String?
)
