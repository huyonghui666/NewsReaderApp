package com.example.newsreader.collectionAndHistory.ui.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsreader.collectionAndHistory.model.NewsItem
import com.example.newsreader.collectionAndHistory.ui.FavoriteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WebViewModel @Inject constructor(
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {

    private val _webViewState = MutableStateFlow(WebViewState())
    val webViewState: StateFlow<WebViewState> = _webViewState.asStateFlow()


    fun loadUrl(url: String, title: String) {
        val newsItem = NewsItem(
            id = url,
            title = title,
            url = url
        )
        _webViewState.update { it.copy(currentNews = newsItem) }

        viewModelScope.launch {
            favoriteRepository.isFavorite(url)
                .collect { isFavorite ->
                    _webViewState.update { it.copy(isFavorite = isFavorite) }
                }
        }
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            _webViewState.value.currentNews?.let { newsItem ->
                if (_webViewState.value.isFavorite) {
                    favoriteRepository.removeFavoriteNews(newsItem)
                } else {
                    favoriteRepository.addFavoriteNews(newsItem)
                }
            }
        }
    }
}
data class WebViewState(
    val currentNews: NewsItem? = null,
    val isFavorite: Boolean = false,
)