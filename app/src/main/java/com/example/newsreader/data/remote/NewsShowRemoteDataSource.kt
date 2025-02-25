package com.example.newsreader.data.remote

import com.example.newsreader.data.api.NewsShowApiService
import javax.inject.Inject

class NewsShowRemoteDataSource @Inject constructor(private val newsShowApiService: NewsShowApiService){

    /**
     * 获取指定频道的新闻
     * @param channel 频道（新闻、娱乐、体育、财经、军事、科技、手机、数码、时尚、游戏、教育、健康、旅游）
     * @param page 页数
     */
    suspend fun getNewsShow(channel:String,page:String)=newsShowApiService.getNewsShow(channel=channel, page = page)

}