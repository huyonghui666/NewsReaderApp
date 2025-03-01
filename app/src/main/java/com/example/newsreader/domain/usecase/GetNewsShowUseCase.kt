package com.example.newsreader.domain.usecase


import com.example.newsreader.domain.repository.NewsShowRepository
import javax.inject.Inject

class GetNewsShowUseCase @Inject constructor(private val newsShowRepository: NewsShowRepository) {

    operator fun invoke(channel: String)=newsShowRepository.newsShowPaging(channel)

}