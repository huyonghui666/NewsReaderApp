package com.example.newsreader.ui.screens

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.newsreader.ui.activity.SearchNewsActivity
import com.example.newsreader.ui.components.BottomNavBar
import com.example.newsreader.ui.components.IntentSearchNewsBar
import com.example.newsreader.ui.components.NewsCategoryTabs
import com.example.newsreader.ui.components.NewsShowCard
import com.example.newsreader.ui.viewmodel.NewsShowViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


// 主界面

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(newsShowViewModel: NewsShowViewModel = hiltViewModel()) {
    //底部导航条navController
    val navController = rememberNavController()
    val isLoading by newsShowViewModel.isLoading .collectAsState()
    val errorMessage by newsShowViewModel.errorMessage.collectAsState()
    val currentChannel by newsShowViewModel.currentChannel.collectAsState()
    val newsItems = newsShowViewModel.newsFlow.collectAsLazyPagingItems()
    val isRefreshing by newsShowViewModel.isRefreshing.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    //设置组件在系统栏下面
    Scaffold(modifier = Modifier.windowInsetsPadding(WindowInsets.systemBars),
        topBar = {
            Column{
                //点击跳转到搜索栏
                IntentSearchNewsBar(
                    modifier = Modifier
                        .padding(30.dp,20.dp,0.dp,0.dp)
                        //点击跳转搜索页面
                        .clickable {
                            val intent=Intent(context,SearchNewsActivity::class.java)
                            context.startActivity(intent)
                        }
                )
                //新闻导航条
                NewsCategoryTabs(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onCategorySelected = { channel ->
                        // 调用Api去搜索相关频道的新闻
                        coroutineScope.launch {
                            newsShowViewModel.getNewsShow(channel)
                        }
                    }
                )
            }
        },
        bottomBar = { BottomNavBar(navController) },
    ) { innerPadding ->
        if (isLoading){
            Box(
                modifier = Modifier.padding(innerPadding),
                contentAlignment = Alignment.Center
            ){
                CircularProgressIndicator()
            }
        }else if (errorMessage!=null){
            Box(
                modifier = Modifier.padding(innerPadding),
                contentAlignment = Alignment.Center
            ){
                Text(text = "Error: $errorMessage")
            }
        }else{
            Box(modifier = Modifier.padding(innerPadding)) {
                NavHost(navController, startDestination = "home") {
                    composable("home") { }
                    composable("profile") { }
                }
                //刷新组件
                val state = rememberPullToRefreshState()
                PullToRefreshBox(
                    isRefreshing =isRefreshing,
                    state = state,
                    onRefresh = {
                        newsShowViewModel.getNewsShow(currentChannel)
                        newsShowViewModel.getIsRefreshing(true) },
                    indicator = {
                        Indicator(
                            modifier = Modifier.align(Alignment.TopCenter),
                            isRefreshing = isRefreshing,
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            state = state
                        )
                    }
                ) {
                    // 使用 LaunchedEffect 来延迟结束刷新
                    LaunchedEffect(isRefreshing) {
                        if (isRefreshing) {
                            // 在这里模拟延迟，延迟 2 秒后更新 isRefreshing 为 false
                            delay(1000) // 延迟 1 秒
                            newsShowViewModel.getIsRefreshing(false) // 延迟后结束刷新
                            Toast.makeText(context,"更新成功",Toast.LENGTH_SHORT).show()
                        }
                    }
                    LazyColumn  {
                        //加载新闻
                        items(
                            count = newsItems.itemCount,
                        ) { index ->
                            val item = newsItems[index]
                            if (item != null) {
                                if (item.url!="" && item.imgsrc!=""){
                                    //Log.d("newsShowTAG", item.title.toString())
                                    NewsShowCard(item)
                                }
                            }
                        }

                        // 加载状态指示器
                        newsItems.apply {
                            when {
                                loadState.refresh is LoadState.Loading -> {
                                    item { CircularProgressIndicator() }// 刷新加载中
                                }
                                loadState.append is LoadState.Loading -> {
                                    item { CircularProgressIndicator()}// 加载更多时的 loading
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}



