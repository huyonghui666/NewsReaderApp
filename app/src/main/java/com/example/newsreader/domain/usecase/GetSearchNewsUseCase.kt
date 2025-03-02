package com.example.newsreader.domain.usecase

import com.example.newsreader.domain.models.TouTiaoHot
import com.example.newsreader.domain.repository.NewsShowRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetSearchNewsUseCase @Inject constructor(private val newsShowRepository: NewsShowRepository) {

    //获取搜索新闻
    operator fun invoke(word: String?)=newsShowRepository.searchNewsPaging(word = word)

    //获取头条热点
    operator fun invoke():Flow<List<TouTiaoHot>> = flow {
        try {
            emit(newsShowRepository.getTouTiaohotHot())
        }catch (e:Exception){
            e.printStackTrace()
        }
    }
}