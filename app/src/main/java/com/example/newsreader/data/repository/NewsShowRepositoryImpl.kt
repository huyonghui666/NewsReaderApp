package com.example.newsreader.data.repository


import androidx.paging.PagingData
import androidx.paging.map
import com.example.newsreader.data.remote.NewsShowRemoteDataSource
import com.example.newsreader.domain.models.NewsShowModel
import com.example.newsreader.domain.repository.NewsShowRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NewsShowRepositoryImpl @Inject constructor(
    private val newsShowRemoteDataSource: NewsShowRemoteDataSource,
) : NewsShowRepository {

    /*override suspend fun getNewsShow(channel: String,page:String): List<NewsShowModel> {
        val newsShow = newsShowRemoteDataSource.getNewsShow(channel,page)
        return newsShow.newsShowData!!.map {
            NewsShowModel(
                time = it.time,
                title = it.title,
                imgsrc = it.imgsrc,
                url = it.url
            )
        }
    }*/

    override fun newsShowPaging(channel: String): Flow<PagingData<NewsShowModel>> {
        return newsShowRemoteDataSource.newsShowPaging(channel)!!.map { pagingData ->
            pagingData.map { dataModel->
                NewsShowModel(
                    time = dataModel.time,
                    title = dataModel.title,
                    imgsrc = dataModel.imgsrc,
                    url = dataModel.url
                )
            }
        }
    }

}