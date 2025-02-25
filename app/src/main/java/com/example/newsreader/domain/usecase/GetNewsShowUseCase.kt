package com.example.newsreader.domain.usecase

import com.example.newsreader.domain.models.NewsShowModel
import com.example.newsreader.domain.repository.NewsShowRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetNewsShowUseCase @Inject constructor(private val newsShowRepository: NewsShowRepository) {

    /*operator fun invoke(channel:String,page:String): Flow<List<NewsShowModel>> = flow {
        try {
            //获取新闻数据
            val newsShowList = newsShowRepository.getNewsShow(channel,page)
            emit(newsShowList)

        }catch (e:Exception){
            e.printStackTrace()
        }
    }*/

    operator fun invoke(channel: String)=newsShowRepository.newsShowPaging(channel)
}