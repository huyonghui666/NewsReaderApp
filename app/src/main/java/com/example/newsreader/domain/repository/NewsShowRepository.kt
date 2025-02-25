package com.example.newsreader.domain.repository

import androidx.paging.PagingData
import com.example.newsreader.domain.models.NewsShowModel
import kotlinx.coroutines.flow.Flow


interface NewsShowRepository {
    /**
     * 请求新闻
     * @param channel 频道（新闻、娱乐、体育、财经、军事、科技、手机、数码、时尚、游戏、教育、健康、旅游）
     * @param page 页数
     */
    /*suspend fun getNewsShow(channel:String,page:String):List<NewsShowModel>*/

    /**
     *分页加载
     * @param channel 频道
     */
    fun newsShowPaging(channel: String):Flow<PagingData<NewsShowModel>>

}