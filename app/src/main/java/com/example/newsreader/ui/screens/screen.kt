package com.example.newsreader.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.newsreader.ui.components.BottomNavBar
import com.example.newsreader.ui.components.NewsCategoryTabs
import com.example.newsreader.ui.components.NewsShowCard
import com.example.newsreader.ui.viewmodel.NewsShowViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


// 主界面
@Composable
fun MainScreen(newsShowViewModel: NewsShowViewModel = hiltViewModel()) {
    //底部导航条navController
    val navController = rememberNavController()
    //根据频道获取到的新闻列表
    val newsShowList by newsShowViewModel.newsShowList.collectAsState()
    val isLoading by newsShowViewModel.isLoading .collectAsState()
    val errorMessage by newsShowViewModel.errorMessage.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {NewsCategoryTabs(
            modifier = Modifier.fillMaxWidth(),
            onCategorySelected = { channel ->
                // 调用Api去搜索相关频道的新闻
                coroutineScope.launch {
                    newsShowViewModel.loadNewsShow(channel)
                }
                //newsShowViewModel.loadNewsShow(channel)
                //Log.d("NewsShowScreenTAG", newsShowList.toString())
            }
        )},
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
                if (newsShowList.isNotEmpty()){
                    LazyColumn  {
                        //加载新闻
                        items(newsShowList){newsShow->
                            if (newsShow.url!="" && newsShow.imgsrc!=""){
                                //Log.d("newsShowTAG", newsShow.title.toString())
                                NewsShowCard(newsShow)
                            }
                        }
                    }
                }
            }
        }
    }
}




