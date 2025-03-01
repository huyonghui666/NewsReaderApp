package com.example.newsreader.data.remote

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.newsreader.data.api.SearchNewsApiService
import javax.inject.Inject

class SearchNewsDataRemote @Inject constructor(
    private val searchNewsService: SearchNewsApiService
) {

    /**
     * 搜索新闻
     */
    fun searchNewsPaging(word: String)= Pager(
        config = PagingConfig(
            pageSize = 10,          // 每页数量
            prefetchDistance = 5,   // 提前加载下一页的阈值（距离列表底部 5 项时加载）
            enablePlaceholders = false// 不显示占位符
        ),
        pagingSourceFactory = { SearchNewsPagingSource(searchNewsService, word) }
    ).flow

    /**
     * 获取头条热点
     */
    suspend fun getTouTiaohotHot()=searchNewsService.getTouTiaohotHot()

}