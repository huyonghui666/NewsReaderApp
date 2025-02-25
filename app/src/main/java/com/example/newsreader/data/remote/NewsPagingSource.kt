package com.example.newsreader.data.remote


import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.newsreader.data.api.NewsShowApiService
import com.example.newsreader.data.model.NewsShowModel


class NewsPagingSource(
    private val newsShowApiService: NewsShowApiService, // Retrofit 接口
    private val channel: String     // 频道

) : PagingSource<Int, NewsShowModel>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NewsShowModel> {
        return try {
            // 1. 获取当前页码
            val page = params.key ?: 1

            // 2. 发起网络请求
            val response = newsShowApiService.getNewsShow(
                channel = channel,
                page = page.toString()
            )

            // 3. 构造分页结果
            LoadResult.Page(
                data = response.newsShowData,
                prevKey = if (page == 1) null else page - 1,
                nextKey = page + 1  //下一页
            )
        } catch (e: Exception) {
            // 4. 错误处理
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, NewsShowModel>): Int? {
        TODO("Not yet implemented")
    }
}
