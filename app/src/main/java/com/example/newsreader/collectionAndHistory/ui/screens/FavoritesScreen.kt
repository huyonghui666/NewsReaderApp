package com.example.newsreader.collectionAndHistory.ui.screens


import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.newsreader.collectionAndHistory.ui.components.NewsItemCard

import com.example.newsreader.collectionAndHistory.ui.viewmodel.FavoritesViewModel
import java.net.URLEncoder


/**
 * 收藏视图
 */
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    navController: NavController,
    //onNewsClick: (String, String) -> Unit,
    viewModel: FavoritesViewModel= hiltViewModel()  //这里是否可以优化，重复初始化FavoritesViewModel的init
) {
    val newsListState by viewModel.newsListState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("我的收藏") },
                navigationIcon = {
                    //返回
                    IconButton(onClick = {navController.navigateUp()}) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                },
                actions = {
                    if (newsListState.isEditMode) {
                        // 编辑模式下的操作
                        if (newsListState.selectedItems.isNotEmpty()) {
                            Text(
                                text = "已选择 ${newsListState.selectedItems.size} 项",
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                            IconButton(
                                onClick = { viewModel.deleteSelectedItems() }
                            ) {
                                Icon(Icons.Default.Delete, contentDescription = "删除")
                            }
                        }
                        TextButton(
                            onClick = { viewModel.toggleEditMode() }
                        ) {
                            Text("取消")
                        }
                    } else {
                        // 非编辑模式下显示编辑按钮
                        TextButton(
                            onClick = { viewModel.toggleEditMode() }
                        ) {
                            Text("编辑")
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (newsListState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else if (newsListState.items.isEmpty()) {
                Text(
                    text = "暂无收藏",
                    modifier = Modifier.align(Alignment.Center),
                    style = MaterialTheme.typography.bodyLarge
                )
            } else {
                LazyColumn {
                    items(
                        items = newsListState.items,
                        key = { it.id }
                    ) { newsItem ->
                        NewsItemCard(
                            newsItem = newsItem,
                            isEditMode = newsListState.isEditMode,
                            //这个随着newsListState的状态改变，重组而变成true
                            isSelected = newsItem.id in newsListState.selectedItems,
                            //非编辑模式下可以点击跳转新闻，编辑模式下点击Card触发点击复选框效果
                            onItemClick = {
                                if (newsListState.isEditMode) {
                                    viewModel.toggleItemSelection(newsItem.id)
                                } else {
                                    newsItem.url.let {
                                        // 在点击新闻项时，跳转到相应的新闻路由导航
                                        navController.navigate("news_web_view/${URLEncoder.encode(it, "UTF-8")}?title=${URLEncoder.encode(newsItem.title, "UTF-8")}")
                                    }
                                    //onNewsClick(newsItem.url, newsItem.title)
                                }
                            },
                            //选中要删除的新闻
                            onCheckboxClick = { isChecked ->
                                viewModel.toggleItemSelection(newsItem.id)
                            },
                            navController = navController
                        )
                    }
                }
            }

            // 错误提示
            newsListState.error?.let { error ->
                Snackbar(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                ) {
                    Text(text = error)
                }
            }
        }
    }
}



