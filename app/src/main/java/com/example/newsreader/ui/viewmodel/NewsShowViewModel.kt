package com.example.newsreader.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsreader.domain.models.NewsShowModel
import com.example.newsreader.domain.usecase.GetNewsShowUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsShowViewModel @Inject constructor(
    private val getNewsShowUseCase: GetNewsShowUseCase
):ViewModel(){

    //新闻列表数据
    private val _newsShowList = MutableStateFlow<List<NewsShowModel>>(emptyList())
    val newsShowList= _newsShowList.asStateFlow()

    //判断是否正在载入新闻
    private val _isLoading=MutableStateFlow<Boolean>(false)
    val isLoading=_isLoading.asStateFlow()

    // 分类状态
    private val _selectedCategory = MutableStateFlow<String>("新闻")
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()

    // 分页状态
    private val _currentPage = MutableStateFlow("1")
    val currentPage: StateFlow<String> = _currentPage.asStateFlow()

    //错误消息
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()



    /*// 修改为挂起函数，返回新数据
    private suspend fun loadNewsShow(channel: String, page: String): List<NewsShowModel> {
        return try {
            getNewsShowUseCase(channel, page) // 直接返回新数据
        } catch (e: Exception) {
            // 处理错误
            emptyList()
        }
    }*/
    /*init {
        viewModelScope.launch {
            _isLoading.value = true
            // 创建异步任务列表
            val deferredPages = listOf("1", "2", "3", "4").map { page ->
                async {
                    loadNewsShow("新闻", page) // 返回 List<NewsShowModel>
                }
            }

            // 按顺序等待并合并数据
            deferredPages.forEach { deferred ->
                try {
                    val newData = deferred.await()
                    _newsShowList.update { oldList -> oldList + newData }
                } catch (e: Exception) {
                    _errorMessage.value = "加载第${deferred}页失败: ${e.message}"
                }
            }
            _isLoading.value = false
        }
    }

    // 返回新数据的挂起函数
    private suspend fun loadNewsShow(channel: String, page: String): List<NewsShowModel> {
        return try {
            getNewsShowUseCase(channel, page)
        } catch (e: Exception) {
            throw e // 将异常抛给上层处理
        }
    }*/

    init {
        viewModelScope.launch {
            loadNewsShow("新闻", "1")
            loadNewsShow("新闻", "2")
            loadNewsShow("新闻", "3")
            loadNewsShow("新闻", "4")
            loadNewsShow("新闻", "5")
            /*val deferred1 =async {
                loadNewsShow("新闻","1")
            }
            deferred1.await()
            val deferred2 =async {
                loadNewsShow("新闻","2")
            }
            deferred2.await()
            val deferred3 =async {
                loadNewsShow("新闻","3")
            }
            deferred3.await()
            val deferred4 =async {
                loadNewsShow("新闻","4")
            }
            deferred4.await()
            val deferred5 =async {
                loadNewsShow("新闻","5")
            }
            deferred5.await()*/

        }
        /*loadNewsShow("新闻","1")
        loadNewsShow("新闻","2")
        loadNewsShow("新闻","3")
        loadNewsShow("新闻","4")
        loadNewsShow("新闻","5")
        loadNewsShow("新闻","6")*/
    }

    suspend fun loadNewsShow(channel:String=_selectedCategory.value,page:String=_currentPage.value) {
        viewModelScope.launch {
            //每次调用这个函数就设置
            _isLoading.value=true
            _errorMessage.value=null

            getNewsShowUseCase(channel,page)
                .catch { e->
                    _errorMessage.value=e.message
                }
                .collect { newsShowList ->
                    _newsShowList.update { oldList->
                        Log.d("updateTAG", (oldList+newsShowList).toString())
                        (oldList+newsShowList)
                        /*.sortedBy { it.time
                            }*/
                    }
                    //取消
                    _isLoading.value=false
                }
        }
    }



}