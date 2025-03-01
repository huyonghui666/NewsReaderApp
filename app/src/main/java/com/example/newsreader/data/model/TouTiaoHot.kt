package com.example.newsreader.data.model

import com.squareup.moshi.Json

 data class TouTiaoHotResponse(
    @Json(name = "code")val code:Int,
    @Json(name = "result")val result:HotResult
)
data class HotResult(
    @Json(name = "list")val hotList:List<TouTiaoHot>
)

//头条热点词
data class TouTiaoHot(
    @Json(name = "word")val word:String
)
