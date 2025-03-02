package com.example.newsreader.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.paging.cachedIn
import com.example.newsreader.domain.usecase.GetSearchNewsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import com.example.newsreader.domain.models.TouTiaoHot
import com.example.newsreader.util.PreferencesManager
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

@HiltViewModel
class SearchNewsViewModel @Inject constructor(
    private val getSearchNewsUseCase: GetSearchNewsUseCase,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    // 搜索关键字
    private val _searchWord = MutableStateFlow<String?>(null)
    val searchWord: StateFlow<String?> = _searchWord.asStateFlow()

    //控制显示搜索的新闻
    private val _searchNewsShow=MutableStateFlow<Boolean>(false)
    val searchNewsShow=_searchNewsShow.asStateFlow()

    //判断是否正在载入新闻
    private val _isLoading=MutableStateFlow<Boolean>(false)
    val isLoading=_isLoading.asStateFlow()

    //当点击搜索进行数据获取
    private val _searchPress=MutableStateFlow<Boolean>(false)
    //val searchPress=_searchPress.asStateFlow()
    val searchNewsFlow=_searchPress
        .filter { it }    //过滤true的可以继续进行flatMapLatest
        .flatMapLatest {searchPress->
            getSearchNewsUseCase.invoke(_searchWord.value)
                .onStart {
                    // 在开始请求之前设置 isLoading 为 true
                    _isLoading.value = true
                    _searchNewsShow.value=false
                }
                .onEach {
                    // 数据获取过程中可能没有操作，但需要确保
                    _isLoading.value = false
                    _searchNewsShow.value=true
                    _searchPress.value=false
                }
                .catch { exception ->
                    // 如果有错误发生，确保 loading 状态为 false
                    _isLoading.value = false
                    // 错误处理，例如记录日志或显示错误信息
                    Log.e("SearchNewsFlow", "Error fetching data: ${exception.message}")
                }
                .cachedIn(viewModelScope)
    }

    //历史搜索列表
    private val _historicalSearchList = MutableStateFlow(mutableListOf<String>())
    val historicalSearchList: StateFlow<MutableList<String>> = _historicalSearchList.asStateFlow()

    //新闻列表数据
    private val _TouTiaoHotList = MutableStateFlow<List<TouTiaoHot>>(emptyList())
    val TouTiaoHotList= _TouTiaoHotList.asStateFlow()
    //获取头条热点
    fun loadTouTiaohotHot() {
        viewModelScope.launch {
            getSearchNewsUseCase()
                .catch { e->
                    Log.e("SearchNewsFlow", "Error fetching data: ${e.message}")
                }
                .collect { newsShowList ->
                    _TouTiaoHotList.value = newsShowList
                }
        }
    }



    // 最大缓存数量
    private val maxSearchHistory = 10

    init {
        // 初始化时从 SharedPreferences 加载历史搜索记录
        loadSearchHistory()
        //获取头条热点
        loadTouTiaohotHot()
    }

    // 从 SharedPreferences 中加载历史搜索记录
    fun loadSearchHistory() {
        val savedHistory = preferencesManager.getString("search_history")
        if (savedHistory!="") _historicalSearchList.value = savedHistory.split(",").toMutableList()

    }

    // 将新的搜索关键词保存到 SharedPreferences 中
    private fun saveSearchHistory() {
        val searchHistory = _historicalSearchList.value.joinToString(",")
        preferencesManager.saveString("search_history", searchHistory)
    }

    // 添加新的搜索关键词
    fun addSearchKeyword(searchWord: String) {
        val currentList = _historicalSearchList.value.toMutableList()
        // 如果关键词已经在历史记录中，先移除它
        currentList.remove(searchWord)
        // 将新搜索的关键词添加到列表的最前面
        currentList.add(0, searchWord)
        // 如果列表超过最大缓存数，移除最后一项
        if (currentList.size > maxSearchHistory) {
            currentList.removeAt(currentList.size - 1)
        }
        // 更新历史搜索列表
        _historicalSearchList.value = currentList
        // 保存历史记录到 SharedPreferences
        saveSearchHistory()
    }

    //清空历史搜索
    fun clearSearchHistory(){
        _historicalSearchList.value= mutableListOf()
        preferencesManager.clear()
    }


    //改变word来触发搜索
    fun getSearchWord(word:String){
        _searchWord.value=word
    }

    //当按下搜索获取新闻
    fun getSearchPress(searchPress:Boolean){
        _searchPress.value=searchPress
    }

    //控制显示搜索的新闻
    fun getSearchNewsShow(searchNewsShow:Boolean){
        _searchNewsShow.value=searchNewsShow
    }


}