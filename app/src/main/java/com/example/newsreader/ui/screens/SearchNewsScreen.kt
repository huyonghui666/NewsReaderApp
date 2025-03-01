package com.example.newsreader.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.log
import com.example.newsreader.ui.components.ClearHistoryDialog
import com.example.newsreader.ui.components.NewsShowCard
import com.example.newsreader.ui.components.SearchHistoryView
import com.example.newsreader.ui.components.SearchNewsBar
import com.example.newsreader.ui.components.SearchNewsShowCard
import com.example.newsreader.ui.viewmodel.SearchNewsViewModel
import kotlinx.coroutines.launch



@Composable
fun SearchNewsScreen(searchNewsViewModel: SearchNewsViewModel= hiltViewModel()){
    val searchWord by searchNewsViewModel.searchWord.collectAsState()
    val searchNewsFlow = searchNewsViewModel.searchNewsFlow.collectAsLazyPagingItems()
    val searchPress by searchNewsViewModel.searchPress.collectAsState()
    val rememberCoroutineScope= rememberCoroutineScope()
    val historicalSearchList by searchNewsViewModel.historicalSearchList.collectAsState()
    // 控制是否显示清空对话框的状态
    var showClearDialog by remember { mutableStateOf(false) }
    //控制显示搜索的新闻
    val searchNewsShow by searchNewsViewModel.searchNewsShow.collectAsState()
    val context = LocalContext.current
    val isLoading by searchNewsViewModel.isLoading.collectAsState()


    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth() // 让 Row 填充父容器宽度
                .padding(16.dp) // 给布局添加一些内边距
                .windowInsetsPadding(WindowInsets.systemBars),
            verticalAlignment = Alignment.CenterVertically
        ) {
            //搜索栏
            SearchNewsBar(
                modifier = Modifier,
                word = searchWord,
                onWordChange = {word->
                    //修改word
                    searchNewsViewModel.getSearchWord(word)
                    //当word为空字符串时显示热搜榜view
                    if (word.isEmpty()){
                        searchNewsViewModel.getSearchNewsShow(false)
                    }
                },
                onSearchPress = {searchPress->
                    //搜索相关的新闻
                    rememberCoroutineScope.launch {
                        searchNewsViewModel.getSearchPress(searchPress)
                    }
                    //保存历史搜索关键词
                    searchNewsViewModel.addSearchKeyword(searchWord!!)
                }
            )
            Spacer(modifier = Modifier.width(20.dp))

            Text(
                text = "取消",
                color = MaterialTheme.colorScheme.surfaceDim,
            )
        }
        if (searchNewsShow){
            if (isLoading){
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }else{
                //加载搜索新闻
                LazyColumn {
                    items(
                        count = searchNewsFlow.itemCount,
                        //key = { index -> searchNewsFlow[index]?.id ?: index }
                    ) { index ->
                        //item是数据模块
                        val item = searchNewsFlow[index]
                        if (item != null) {
                            if (item.picUrl!="" && item.url!="") {
                                SearchNewsShowCard(searchNews = item)
                            }
                            //SearchNewsShowCard(searchNews = item)
                        }
                    }
                    // 加载状态指示器
                    searchNewsFlow.apply {
                        when {
                            loadState.refresh is LoadState.Loading -> {
                                item { CircularProgressIndicator() }// 刷新加载中
                            }
                            loadState.append is LoadState.Loading -> {
                                item { CircularProgressIndicator()}// 加载更多时的 loading
                            }
                            loadState.refresh is LoadState.Error -> {
                                val error = loadState.refresh as LoadState.Error
                                Toast.makeText(context,"加载失败!",Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }else{
            //历史搜索view
            SearchHistoryView(
                historicalSearchList,
                onClear = {
                    showClearDialog=true
                },
                //历史搜索word被点击
                onSearchHistoryTextClicked = {word: String ->
                    //修改word
                    searchNewsViewModel.getSearchWord(word)
                    //搜索相关新闻

                }
            )
            // 根据状态显示清空对话框
            if (showClearDialog){
                ClearHistoryDialog(onClear = {
                    //清空历史搜索
                    searchNewsViewModel.clearSearchHistory()
                    showClearDialog=false
                })
            }
        }
    }
}