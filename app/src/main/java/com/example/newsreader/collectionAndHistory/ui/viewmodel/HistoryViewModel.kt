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
class HistoryViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {
    //历史记录新闻列表
    private val _newsListState = MutableStateFlow(NewsListState())
    val newsListState = _newsListState.asStateFlow()


    init {
        loadHistorys()
    }

    /**
     * 获取历史记录新闻
     */
    private fun loadHistorys() {
        viewModelScope.launch {
            _newsListState.update { it.copy(isLoading = true) }
            try {
                //TODO 获取历史记录新闻
                newsRepository.getHistory().collect { historys ->
                    _newsListState.update {
                        it.copy(
                            items = historys,
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
                newsRepository.removeHistories(_newsListState.value.selectedItems.toList())
                // 退出编辑模式并刷新列表
                _newsListState.update { it.copy(
                    isEditMode = false,
                    selectedItems = emptySet()
                ) }
                loadHistorys()
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


    //判断是否记录了
    private val _isHistorys = MutableStateFlow(false)
    val isHistorys = _isHistorys.asStateFlow()

    /**
     * 设置是否点击历史记录
     */
    fun setIsFavorite(bool:Boolean){
        _isHistorys.value=bool
    }

    /**
     * 跳转新闻详情页时添加到历史记录
     */
    fun toggleHistory(url:String,title:String,imgsrc:String) {
        viewModelScope.launch {
            try {
                // 查找记录项的ID并删除
                Log.d("toggleHistoryTAG", imgsrc)
                newsRepository.getHistory().collect { historys ->
                    historys.find { it.url == url }?.let { history ->
                        newsRepository.removeHistories(listOf(history.id))
                    }
                }
                // 添加历史记录
                newsRepository.addHistory(title, url,imgsrc)
            } catch (e: Exception) {
                _newsListState.update {
                    it.copy(error = "添加历史记录失败：${e.message}")
                }
            }
        }
    }

    /**
     * 清空
     */
    fun clearHistory(){
        viewModelScope.launch {
            try {
                newsRepository.clearHistory()
                _newsListState.update { it.copy(
                    isEditMode = false,
                    selectedItems = emptySet()
                ) }
                loadHistorys()
            }catch (e:Exception){
                _newsListState.update { it.copy(error = "清空失败${e.message}") }
            }
        }
    }


    /**
     * 检查新闻是否已收藏
     */
//    fun checkFavoriteStatus(url:String) {
//        viewModelScope.launch {
//            try {
//                _isHistorys.value = newsRepository.isFavorite(url)
//            } catch (e: Exception) {
//                // 处理错误
//                Log.d("FavoritesViewModelTAG", e.message.toString())
//            }
//        }
//    }

}