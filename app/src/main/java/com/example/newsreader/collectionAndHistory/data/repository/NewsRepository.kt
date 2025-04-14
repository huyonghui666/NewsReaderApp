package com.example.newsreader.collectionAndHistory.data.repository


import android.util.Log
import com.example.newsreader.collectionAndHistory.data.api.CollectionAndHistoryApi
import com.example.newsreader.collectionAndHistory.data.model.DeleteNewsRequest
import com.example.newsreader.collectionAndHistory.data.model.NewsItem
import com.example.newsreader.collectionAndHistory.data.model.NewsRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * 新闻仓库实现类
 */
class NewsRepository @Inject constructor(
    private val collectionAndHistoryApi: CollectionAndHistoryApi
)  {

    /**
     * 获取收藏的新闻
     */
     suspend fun getFavorites(): Flow<List<NewsItem>> = flow {
        val response = collectionAndHistoryApi.getFavorites()
        if (response.code == 200) {
            emit(response.data?.map { dto ->
                NewsItem(
                    id = dto.id,
                    title = dto.title,
                    url = dto.url,
                    timestamp = dto.timestamp,
                    imgSrc = dto.imgSrc
                )
            } ?: emptyList())
        } else {
            throw Exception(response.message)
        }
    }

    /**
     * 添加收藏
     */
     suspend fun addFavorite(title: String, url: String,imgSrc:String): NewsItem {
        val request = NewsRequest(title = title, url = url, imgSrc = imgSrc)
        val response = collectionAndHistoryApi.addFavorite(request)
        if (response.code == 200) {
            return response.data?.let { dto ->
                NewsItem(
                    id = dto.id,
                    title = dto.title,
                    url = dto.url,
                    timestamp = dto.timestamp,
                    imgSrc = dto.imgSrc
                )
            } ?: throw Exception("添加收藏失败")
        } else {
            throw Exception(response.message)
        }
    }

    /**
     * 批量删除收藏新闻
     */
     suspend fun removeFavorites(ids: List<String>) {
        val response = collectionAndHistoryApi.deleteFavorites(DeleteNewsRequest(ids))
        if (response.code != 200) {
            throw Exception(response.message)
        }
    }

    /**
     * 检查新闻是否已收藏
     */
    suspend fun isFavorite(url: String): Boolean {
        val response = collectionAndHistoryApi.checkFavorite(url)
        Log.d("isFavoriteTAG", response.data.toString())
        return if (response.code == 200) {
            response.data ?: false
        } else {
            throw Exception(response.message)
        }
    }

    /**
     * 获取历史记录
     */
     suspend fun getHistory(): Flow<List<NewsItem>> = flow {
        val response = collectionAndHistoryApi.getHistory()
        Log.d("getHistoryTAG", response.data.toString())
        if (response.code == 200) {
            emit(response.data?.map { dto ->
                NewsItem(
                    id = dto.id,
                    title = dto.title,
                    url = dto.url,
                    timestamp = dto.timestamp,
                    imgSrc = dto.imgSrc
                )
            } ?: emptyList())
        } else {
            throw Exception(response.message)
        }
    }



    /**
     * 添加历史记录
     */
    suspend fun addHistory(title: String, url: String,imgSrc:String): NewsItem {
        val request = NewsRequest(title = title, url = url,imgSrc=imgSrc)
        val response = collectionAndHistoryApi.addHistory(request)
        if (response.code == 200) {
            return response.data?.let { dto ->
                NewsItem(
                    id = dto.id,
                    title = dto.title,
                    url = dto.url,
                    timestamp = dto.timestamp,
                    imgSrc = dto.imgSrc
                )
            } ?: throw Exception("添加历史记录失败")
        } else {
            throw Exception(response.message)
        }
    }

    /**
     * 批量删除历史记录新闻
     */
    suspend fun removeHistories(ids: List<String>) {
        val response = collectionAndHistoryApi.removeHistories(DeleteNewsRequest(ids))
        if (response.code != 200) {
            throw Exception(response.message)
        }
    }

    /**
     * 清空
     */
    suspend fun clearHistory() {
        val response = collectionAndHistoryApi.clearHistory()
        if (response.code != 200) {
            throw Exception(response.message)
        }
    }
}
