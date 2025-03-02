package com.example.newsreader.ui.screens

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.newsreader.ui.components.LoadSearchNews
import com.example.newsreader.ui.components.VoiceInputScreen
import com.example.newsreader.ui.viewmodel.VoiceInputViewModel
import kotlinx.coroutines.launch


@Composable
fun VoiceInputScreenMain(voiceInputViewModel: VoiceInputViewModel = hiltViewModel()){
    val rememberCoroutineScope = rememberCoroutineScope()
    //显示语音输入
    var showVoiceInput by remember { mutableStateOf(true) }
    val searchNewsFlow = voiceInputViewModel.searchNewsFlow.collectAsLazyPagingItems()
    val isLoading by voiceInputViewModel.isLoading.collectAsState()
    val context= LocalContext.current


    if (showVoiceInput){

        Scaffold (
            modifier = Modifier.windowInsetsPadding(WindowInsets.systemBars),
            topBar = {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopEnd){
                    Icon(
                        imageVector = Icons.Filled.Clear,
                        contentDescription = "退出",
                        modifier = Modifier
                            .padding(end = 10.dp)
                            .clickable {
                                (context as? Activity)?.finish()
                            }
                            .size(25.dp),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        ){innerPadding->
            Box(modifier = Modifier
                .fillMaxHeight()
                .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Column (
                    modifier = Modifier
                        .fillMaxWidth() // 让 Row 填充父容器宽度
                        .padding(start = 16.dp, end = 16.dp), // 给布局添加一些内边距
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    //语音输入
                    VoiceInputScreen(
                        onResult = { text ->
                            if (text!=""){
                                //修改word
                                voiceInputViewModel.getSearchWord(text)
                                // 调用搜索新闻
                                rememberCoroutineScope.launch {
                                    voiceInputViewModel.getSearchPress(true)
                                }
                                //保存历史搜索关键词
                                voiceInputViewModel.addSearchKeyword(text)
                                showVoiceInput = false
                            }
                        },
                    )
                }
            }
        }

    }else{
        if (isLoading){
            Box(
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }else {
            //加载搜索新闻
            Box (
                modifier = Modifier
                    .fillMaxWidth() // 让 Row 填充父容器宽度
                    //.padding(start = 16.dp, end = 16.dp) // 给布局添加一些内边距
                    .windowInsetsPadding(WindowInsets.systemBars),
            ){
                Log.d("LoadSearchNewsTAG", searchNewsFlow.toString())
                LoadSearchNews(searchNewsFlow)
            }
        }
    }
}