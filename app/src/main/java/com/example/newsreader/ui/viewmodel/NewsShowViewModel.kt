package com.example.newsreader.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.newsreader.domain.usecase.GetNewsShowUseCase
import com.example.newsreader.domain.usecase.GetSearchNewsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class NewsShowViewModel @Inject constructor(
    private val getNewsShowUseCase: GetNewsShowUseCase,
):ViewModel(){
    //刷新
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing
    
    //判断是否正在载入新闻
    private val _isLoading=MutableStateFlow<Boolean>(false)
    val isLoading=_isLoading.asStateFlow()

    // 频道分类状态
    private val _currentChannel  = MutableStateFlow<String>("")
    val currentChannel: StateFlow<String> = _currentChannel.asStateFlow()
    // 分页数据流
    //当 currentChannel 发出一个新的频道（比如 "Sports" 或 "Technology"）时，
    //flatMapLatest 会调用 repository.searchNews(channel) 来获取与该频道相关
    //的新闻数据。
    //cachedIn(viewModelScope) 会确保当用户切换频道时，加载的数据会被缓存，
    //避免重复请求。
    val newsFlow = _currentChannel.flatMapLatest { channel ->
        getNewsShowUseCase.invoke(channel).cachedIn(viewModelScope)
    }

    //错误消息
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    //初始化新闻频道新闻
    init {
        getNewsShow("新闻")
    }

    //改变channel来触发搜索
    fun getNewsShow(channel:String){
        _currentChannel.value=channel
    }

    fun getIsRefreshing(isRefreshing:Boolean){
        _isRefreshing.value=isRefreshing
    }
}