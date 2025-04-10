package com.example.newsreader.collectionAndHistory.model

/**
 * 创建WebView相关的数据模型
 */
data class NewsItem(
    val id: String,
    val title: String,
    val url: String,
    val timestamp: Long = System.currentTimeMillis()
)