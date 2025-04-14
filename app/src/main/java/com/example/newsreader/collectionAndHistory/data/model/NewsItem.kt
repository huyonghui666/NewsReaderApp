package com.example.newsreader.collectionAndHistory.data.model

/**
 * @param id:新闻的id
 * 创建WebView相关的数据模型
 */
data class NewsItem(
    val id: String,
    val title: String,
    val url: String,
    val timestamp: Long,
    val imgSrc:String
)
