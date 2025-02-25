package com.example.newsreader.data.repository


import android.util.Log
import com.example.newsreader.data.remote.NewsShowRemoteDataSource
import com.example.newsreader.domain.models.NewsShowModel
import com.example.newsreader.domain.repository.NewsShowRepository
import javax.inject.Inject

class NewsShowRepositoryImpl @Inject constructor(
    private val newsShowRemoteDataSource: NewsShowRemoteDataSource
) : NewsShowRepository {

    override suspend fun getNewsShow(channel: String,page:String): List<NewsShowModel> {
        val newsShow = newsShowRemoteDataSource.getNewsShow(channel,page)
        return newsShow.newsShowData!!.map {
            NewsShowModel(
                time = it.time,
                title = it.title,
                imgsrc = it.imgsrc,
                url = it.url
            )
        }
    }
}