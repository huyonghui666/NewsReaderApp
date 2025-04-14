package com.example.newsreader.collectionAndHistory.data.model


import com.squareup.moshi.JsonClass
import java.time.Instant

/**
 * 收藏新闻DTO返回体
 */
@JsonClass(generateAdapter = true)
data class FavoriteNewsDto(
    val id: String,
    val title: String,
    val url: String,
    val timestamp: Long,
    val createdAt: String,
    val imgSrc: String
)

/**
 * 新闻收藏/历史记录请求DTO
 */
@JsonClass(generateAdapter = true)
data class NewsRequest(
    val title: String,
    val url: String,
    val imgSrc: String
)

/**
 * 响应体
 */
// 响应包装类
@JsonClass(generateAdapter = true)
data class NewsApiResponse<T>(
    val code: Int,
    val message: String,
    val data: T?
)

/**
 * 删除新闻请求
 */
@JsonClass(generateAdapter = true)
data class DeleteNewsRequest(
    val ids: List<String>
)

/**
 * 历史记录dto
 */
@JsonClass(generateAdapter = true)
data class HistoryNewsDto(
    val id: String,
    val title: String,
    val url: String,
    val timestamp: Long,
    val createdAt: String,
    val imgSrc: String
)



//data class NewsApiResponse<T>(
//    val code: Int,
//    val message: String,
//    val data: T?
//)