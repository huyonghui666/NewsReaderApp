package com.example.newsreader.collectionAndHistory.ui.viewmodel


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsreader.collectionAndHistory.data.repository.NewsRepository
import com.example.newsreader.collectionAndHistory.ui.state.NewsListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {

    //收藏新闻列表
    private val _newsListState = MutableStateFlow(NewsListState())
    val newsListState = _newsListState.asStateFlow()


    init {
        loadFavorites()
    }

    /**
     * 获取收藏新闻
     */
    private fun loadFavorites() {
        viewModelScope.launch {
            _newsListState.update { it.copy(isLoading = true) }
            try {
                //TODO 获取收藏新闻
                newsRepository.getFavorites().collect { favorites ->
                    _newsListState.update {
                        it.copy(
                            items = favorites,
                            isLoading = false,
                            error = null
                        )
                    }
                }
            } catch (e: Exception) {
                _newsListState.update {
                    it.copy(
                        isLoading = false,
                        error = "加载失败：${e.message}"
                    )
                }
            }
        }
    }

    /**
     * 删除选中的多个
     */
    fun deleteSelectedItems() {
        viewModelScope.launch {
            try {
                //TODO 删除多个
                newsRepository.removeFavorites(_newsListState.value.selectedItems.toList())
                // 退出编辑模式并刷新列表
                _newsListState.update { it.copy(
                    isEditMode = false,
                    selectedItems = emptySet()
                ) }
                loadFavorites()
            } catch (e: Exception) {
                _newsListState.update { it.copy(error = "批量删除失败：${e.message}") }
            }
        }
    }


    /**
     * 改变编辑模式，退出编辑模式时清空选择
     */
    fun toggleEditMode() {
        _newsListState.update { state ->
            state.copy(
                isEditMode = !state.isEditMode,
                selectedItems = emptySet() // 退出编辑模式时清空选择
            )
        }
    }

    /**
     * 当点击复选框或者在编辑模式下点击Card，如果新闻id在selectedItems中，那么删除，否则添加，这个由于
     * newsListState状态改变所以会触发复选框的选中与否
     */
    fun toggleItemSelection(id: String) {
        _newsListState.update { state ->
            val newSelection = state.selectedItems.toMutableSet()
            if (id in newSelection) {
                newSelection.remove(id)
            } else {
                newSelection.add(id)
            }
            state.copy(selectedItems = newSelection)
        }
    }


    //判断是否收藏
    private val _isFavorite = MutableStateFlow(false)
    val isFavorite = _isFavorite.asStateFlow()

    /**
     * 设置是否点击收藏
     */
    fun setIsFavorite(bool:Boolean){
        _isFavorite.value=bool
    }

    /**
     * 点击新闻详情页的收藏icon
     */
    fun toggleFavorite(url:String,title:String,imgsrc:String) {
        viewModelScope.launch {
            try {
                //是收藏
                if (_isFavorite.value) {
                    // 查找收藏项的ID并删除
                    newsRepository.getFavorites().collect { favorites ->
                        favorites.find { it.url == url }?.let { favorite ->
                            newsRepository.removeFavorites(listOf(favorite.id))

                        }
                    }
                    // 添加收藏
                    newsRepository.addFavorite(title, url,imgsrc)
                } else {
                    // 查找收藏项的ID并删除
                    newsRepository.getFavorites().collect { favorites ->
                        favorites.find { it.url == url }?.let { favorite ->
                            newsRepository.removeFavorites(listOf(favorite.id))
                        }
                    }
                }
            } catch (e: Exception) {
                _newsListState.update {
                    it.copy(error = if (_isFavorite.value) {
                        "取消收藏失败：${e.message}"
                    } else {
                        "添加收藏失败：${e.message}"
                    })
                }
            }
        }
    }


    /**
     * 检查新闻是否已收藏
     */
     fun checkFavoriteStatus(url:String) {
        viewModelScope.launch {
            try {
                _isFavorite.value = newsRepository.isFavorite(url)
            } catch (e: Exception) {
                // 处理错误
                Log.d("FavoritesViewModelTAG", e.message.toString())
            }
        }
    }

}