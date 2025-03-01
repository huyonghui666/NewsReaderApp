package com.example.newsreader.domain.usecase

import com.example.newsreader.domain.repository.NewsShowRepository
import javax.inject.Inject

class GetSearchNewsUseCase @Inject constructor(private val newsShowRepository: NewsShowRepository) {

    operator fun invoke(word: String?)=newsShowRepository.searchNewsPaging(word = word)
}