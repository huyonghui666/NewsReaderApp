package com.example.newsreader.data.remote

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.newsreader.data.api.NewsShowApiService
import javax.inject.Inject

class NewsShowRemoteDataSource @Inject constructor(
    private val newsShowApiService: NewsShowApiService,
){

    /**
     * 分页加载，返回 Flow<PagingData<NewsShowModel>>,并且NewsPagingSource()内部进行新闻的网络请求
     * @param channel 频道
     */
    fun newsShowPaging(channel: String) = Pager(
        config = PagingConfig(
            pageSize = 10,          // 每页数量
            prefetchDistance = 5,   // 提前加载下一页的阈值（距离列表底部 5 项时加载）
            enablePlaceholders = false// 不显示占位符
        ),
        pagingSourceFactory = { NewsPagingSource(newsShowApiService, channel) }
    ).flow // 保持数据在配置变化后存活

    /**
     * 搜索新闻
     *//*
    fun searchNewsPaging(word: String)=Pager(
        config = PagingConfig(
            pageSize = 10,          // 每页数量
            prefetchDistance = 5,   // 提前加载下一页的阈值（距离列表底部 5 项时加载）
            enablePlaceholders = false// 不显示占位符
        ),
        pagingSourceFactory = { SearchNewsPagingSource(newsShowApiService, word) }
    ).flow*/

}