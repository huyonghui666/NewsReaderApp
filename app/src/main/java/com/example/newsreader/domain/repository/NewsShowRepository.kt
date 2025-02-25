package com.example.newsreader.domain.repository

import com.example.newsreader.domain.models.NewsShowModel


interface NewsShowRepository {
    /**
     * @param channel 频道（新闻、娱乐、体育、财经、军事、科技、手机、数码、时尚、游戏、教育、健康、旅游）
     */
    //domain中的模块
    suspend fun getNewsShow(channel:String,page:String):List<NewsShowModel>

}