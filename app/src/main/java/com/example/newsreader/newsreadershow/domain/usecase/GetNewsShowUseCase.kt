package com.example.newsreader.newsreadershow.domain.usecase


import com.example.newsreader.newsreadershow.domain.repository.NewsShowRepository
import javax.inject.Inject

class GetNewsShowUseCase @Inject constructor(private val newsShowRepository: NewsShowRepository) {

    operator fun invoke(channel: String)=newsShowRepository.newsShowPaging(channel)

}