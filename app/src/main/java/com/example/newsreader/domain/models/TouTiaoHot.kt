package com.example.newsreader.domain.models

import com.squareup.moshi.Json

//头条热点词
data class TouTiaoHot(
    @Json(name = "title")val title:String
)
