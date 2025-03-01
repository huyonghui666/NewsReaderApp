package com.example.newsreader.data.remote


import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.newsreader.data.api.SearchNewsApiService
import com.example.newsreader.data.model.SearchNews


class SearchNewsPagingSource(
    private val searchNewsService: SearchNewsApiService, // Retrofit 接口
    private val word: String     // 关键词

) : PagingSource<Int, SearchNews>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SearchNews> {
        return try {
            // 1. 获取当前页码
            val page = params.key ?: 1

            // 2. 发起网络请求
            val response = searchNewsService.getSearchNews(
                word = word,
                page = page
            )

            // 3. 构造分页结果
            LoadResult.Page(
                data = response.result.newslist,
                prevKey = if (page == 1) null else page - 1,
                nextKey = page + 1  //下一页
            )
        } catch (e: Exception) {
            // 4. 错误处理
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, SearchNews>): Int? {
        TODO("Not yet implemented")
    }
}
