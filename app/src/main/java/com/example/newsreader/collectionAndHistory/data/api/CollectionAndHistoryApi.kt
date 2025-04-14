package com.example.newsreader.collectionAndHistory.data.api


import com.example.newsreader.collectionAndHistory.data.model.DeleteNewsRequest
import com.example.newsreader.collectionAndHistory.data.model.FavoriteNewsDto
import com.example.newsreader.collectionAndHistory.data.model.HistoryNewsDto
import com.example.newsreader.collectionAndHistory.data.model.NewsApiResponse
import com.example.newsreader.collectionAndHistory.data.model.NewsRequest
import retrofit2.http.*

/**
 * 新闻API接口
 * 创建时间: 2025-04-10 13:45:13
 * @author huyonghui666
 */
interface CollectionAndHistoryApi {
    /**
     * 获取收藏列表
     */
    @GET("/api/news/favorites")
    suspend fun getFavorites(): NewsApiResponse<List<FavoriteNewsDto>>

    /**
     * 添加收藏
     */
    @POST("/api/news/favorites")
    suspend fun addFavorite(@Body request: NewsRequest): NewsApiResponse<FavoriteNewsDto>

    /**
     * 批量删除收藏
     */
    @HTTP(method = "DELETE", path = "/api/news/favorites/batch", hasBody = true)
    suspend fun deleteFavorites(@Body request: DeleteNewsRequest): NewsApiResponse<Any?>

    /**
     * 检查新闻是否已收藏
     */
    @GET("/api/news/favorites/check")
    suspend fun checkFavorite(@Query("url") url: String): NewsApiResponse<Boolean>

    /**
     * 获取历史记录
     */
    @GET("/api/news/history")
    suspend fun getHistory(): NewsApiResponse<List<HistoryNewsDto>>

    /**
     * 添加历史记录
     */
    @POST("/api/news/history")
    suspend fun addHistory(@Body request: NewsRequest): NewsApiResponse<HistoryNewsDto>

    /**
     * 批量删除历史记录
     */
    @HTTP(method = "DELETE", path = "/api/news/history/batch", hasBody = true)
    suspend fun removeHistories(@Body request: DeleteNewsRequest): NewsApiResponse<Any?>

    /**
     * 清空历史记录
     */
    @DELETE("/api/news/history")
    suspend fun clearHistory(): NewsApiResponse<Any?>
}