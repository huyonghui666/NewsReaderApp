package com.example.newsreader.data.repository


import android.util.Log
import androidx.paging.PagingData
import androidx.paging.map
import com.example.newsreader.data.remote.NewsShowRemoteDataSource
import com.example.newsreader.data.remote.SearchNewsDataRemote
import com.example.newsreader.domain.models.NewsShowModel
import com.example.newsreader.domain.models.SearchNews
import com.example.newsreader.domain.models.TouTiaoHot
import com.example.newsreader.domain.repository.NewsShowRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NewsShowRepositoryImpl @Inject constructor(
    private val newsShowRemoteDataSource: NewsShowRemoteDataSource,
    private val searchNewsDataRemote: SearchNewsDataRemote
) : NewsShowRepository {


    /**
     * 实现分页加载和将data的modle数据映射到domain的model
     */
    override fun newsShowPaging(channel: String): Flow<PagingData<NewsShowModel>> {
        return newsShowRemoteDataSource.newsShowPaging(channel=channel)!!.map { pagingData ->
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

    /**
     * 实现搜索新闻
     */
    override fun searchNewsPaging(word: String?): Flow<PagingData<SearchNews>>{
        if (word!=null){
            return  searchNewsDataRemote.searchNewsPaging(word = word).map { searchNews ->
                searchNews.map {dataModel->
                    SearchNews(
                        time = dataModel.time,
                        title = dataModel.title,
                        source = dataModel.source,
                        picUrl = dataModel.picUrl,
                        url = dataModel.url
                    )
                }
            }
        }else{
            return flowOf(PagingData.empty())
        }
    }


    /**
     * 获取头条热点
     */
    override suspend fun getTouTiaohotHot(): List<TouTiaoHot> {
        val touTiaoHotResponse = searchNewsDataRemote.getTouTiaohotHot()
        Log.d("TouTiaohotHotTAG", touTiaoHotResponse.result.hotList.toString())
        return touTiaoHotResponse.result.hotList
            .take(10)
            .map {
            TouTiaoHot(
                title = it.title
            )
        }
    }

}