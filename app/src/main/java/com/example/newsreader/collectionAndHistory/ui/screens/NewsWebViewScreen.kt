package com.example.newsreader.collectionAndHistory.ui.screens

import android.util.Log
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.newsreader.collectionAndHistory.ui.CustomWebViewClient
import com.example.newsreader.collectionAndHistory.ui.viewmodel.WebViewModel


/**
 * 设置WebView来显示新闻详情
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsWebViewScreen(
    url: String,
    title: String,
    onBackClick: () -> Unit,
    viewModel: WebViewModel = hiltViewModel()
) {
    val webViewState by viewModel.webViewState.collectAsState()

    // 保持WebView实例
    val webView = remember { mutableStateOf<WebView?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {  },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.toggleFavorite() }) {
                        Icon(
                            imageVector = if (webViewState.isFavorite){
                                Log.d("urlTAG", "$url  $title")
                                Icons.Default.Favorite
                            }
                            else
                                Icons.Default.FavoriteBorder,
                            contentDescription = if (webViewState.isFavorite)
                                "取消收藏"
                            else
                                "收藏"
                        )
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
            AndroidView(
                factory = { context ->
                    WebView(context).apply {
                        webView.value = this

                        // 设置WebViewClient
                        webViewClient = CustomWebViewClient()

                        // 配置WebView设置
                        settings.apply {
                            javaScriptEnabled = true
                            domStorageEnabled = true
                            setSupportZoom(true)
                            loadWithOverviewMode = true
                            useWideViewPort = true

                            // 启用JavaScript接口
                            javaScriptCanOpenWindowsAutomatically = true

                            // 设置缓存模式
                            cacheMode = WebSettings.LOAD_NO_CACHE

                            // 启用DOM储存API
                            domStorageEnabled = true

                            // 设置默认编码
                            defaultTextEncodingName = "UTF-8"

                            // 禁用缩放
                            builtInZoomControls = false
                            displayZoomControls = false
                        }

                        // 加载URL
                        loadUrl(url)
                    }
                },
                update = { webView ->
                    // 更新时不需要重新加载URL，除非URL发生变化
                    if (webView.url != url) {
                        webView.loadUrl(url)
                    }
                }
            )
        }
    }

    // 生命周期处理
    DisposableEffect(Unit) {
        onDispose {
            // 清理WebView
            webView.value?.apply {
                clearCache(true)
                clearHistory()
                destroy()
            }
        }
    }
}
