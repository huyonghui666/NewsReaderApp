package com.example.newsreader.domain.repository

import androidx.paging.PagingData
import com.example.newsreader.domain.models.NewsShowModel
import com.example.newsreader.domain.models.SearchNews
import com.example.newsreader.domain.models.TouTiaoHot
import kotlinx.coroutines.flow.Flow


interface NewsShowRepository {

    /**
     *分页加载
     * @param channel 频道
     */
    fun newsShowPaging(channel: String):Flow<PagingData<NewsShowModel>>

    /**
     * 根据关键字搜索新闻
     * @param word 搜索关键字
     */
    fun searchNewsPaging(word: String?): Flow<PagingData<SearchNews>>

    /**
     * 获取头条热点
     */
    //suspend fun getTouTiaohotHot():List<TouTiaoHot>
}