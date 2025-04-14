package com.example.newsreader.collectionAndHistory.ui.state


import com.example.newsreader.collectionAndHistory.data.model.NewsItem

/**
 * @param items:服务端返回的新闻列表
 * @param isEditMode：选择删除新闻模式
 * @param selectedItems：编辑模式时选择的新闻id，用于计算选中了几个和删除
 */
data class NewsListState(

    val items: List<NewsItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isEditMode: Boolean = false,
    val selectedItems: Set<String> = emptySet()
)

