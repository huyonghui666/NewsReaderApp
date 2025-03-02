package com.example.newsreader.ui.components

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.speech.RecognizerIntent
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation.Companion.keyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.newsreader.R
import com.example.newsreader.domain.models.SearchNews
import com.example.newsreader.domain.models.TouTiaoHot
import com.example.newsreader.util.VoiceRecognizer
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


/**
 * 加载搜索新闻
 */
@Composable
fun LoadSearchNews(searchNewsFlow: LazyPagingItems<SearchNews>){
    val context = LocalContext.current
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
            }
        }
        // 加载状态指示器
        searchNewsFlow.apply {
            when {
                loadState.refresh is LoadState.Loading -> {
                    item { CircularProgressIndicator() }// 刷新加载中
                }
                loadState.append is LoadState.Loading -> {
                    item { CircularProgressIndicator() }// 加载更多时的 loading
                }
                loadState.refresh is LoadState.Error -> {
                    val error = loadState.refresh as LoadState.Error
                    Toast.makeText(context,"加载失败!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}


/**
 * 热搜榜
 */

@Composable
fun TouTiaoHotView(
    touTiaoHotList: List<TouTiaoHot>
){
    val myCustomFontFamily = FontFamily(
        Font(R.font.toutiaohotfont, FontWeight.Normal)
    )
    val theFirstThree = remember(touTiaoHotList) { mutableStateOf(touTiaoHotList.take(3)) }
    val theLast= remember { mutableStateOf(touTiaoHotList.drop(3)) }


    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)) {
        Text(
            text = "热搜榜",
            style = TextStyle(
                fontFamily = myCustomFontFamily,
                fontSize = 20.sp
            ),
            color = MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.height(5.dp))
        //绘制前三个
        for (i in 0..2){

            Row (verticalAlignment = Alignment.CenterVertically){
                Text(text = (i+1).toString(),
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = theFirstThree.value[i].title)
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
        //绘制后七个
        for (i in 0..6){
            Row (verticalAlignment = Alignment.CenterVertically){
                Text(text = (i+4).toString(),
                    color = MaterialTheme.colorScheme.outlineVariant,
                    textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = theLast.value[i].title)
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

/**
 * 搜索新闻card
 */
@Composable
fun SearchNewsShowCard(
    searchNews: SearchNews,
){
    val context = LocalContext.current
    Card (
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onPrimary),
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            //点击Card跳转到新闻网页
            .clickable {
                searchNews.url?.let { openWebPage(context, it) }
            }
            .padding(all = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ){
        Row{
            Column(
                modifier = Modifier
                    .weight(2f)
                    .padding(0.dp, 0.dp, 5.dp, 0.dp)
                    .weight(3f)
            ) {
                // 最佳实践：使用 Box 布局实现底部定位
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                ) {
                    Text(
                        style = MaterialTheme.typography.titleSmall,
                        text = searchNews.title,
                        maxLines = 3,
                    )
                    Text(
                        color = MaterialTheme.colorScheme.outlineVariant,
                        style = MaterialTheme.typography.bodySmall,
                        text = searchNews.time,
                        modifier = Modifier
                            .align(Alignment.BottomStart)  // 设置底部对齐
                    )
                }
            }
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(searchNews.picUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "News Image",
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp)), // 圆角裁剪
                contentScale = ContentScale.Crop // 关键：裁剪填充
            )
        }
    }
}

/**
 * 清空历史搜索时弹出一个对话框
 */
@Composable
fun ClearHistoryDialog(
    onClear: () -> Unit) {
    var showDialog by remember{ mutableStateOf(true) }

    if (showDialog) {
        AlertDialog(
            //onDismissRequest：用户点击对话框以外的区域或使用系统返回键时调用，设置 showDialog 为 false 隐藏对话框
            onDismissRequest = { showDialog = false },
            //title = { Text("清空历史搜索") },
            text = { Text("确定删除全部历史记录?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onClear()
                        showDialog = false
                    }
                ) {
                    Text("确认")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDialog = false }
                ) {
                    Text("取消")
                }
            }
        )
    }
}



/**
 * 历史搜索
 */

@Composable
fun SearchHistoryView(
    historicalSearchList: MutableList<String>,
    onClear: ()->Unit,
    onSearchHistoryTextClicked:(word:String)->Unit
) {
    //将historicalSearchList以一行两个历史搜索word数据进行垂直排列
    val historyList= historicalSearchList.chunked(2)

    // 展开/收起状态
    var isExpanded by remember { mutableStateOf(false) }
    // 最大显示条目数（收起时）
    val maxCollapsedItems = 2

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, 0.dp, 0.dp, 0.dp)
    ) {
        // 标题栏
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "历史搜索",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f)
            )
            Text(
                text =if (isExpanded)"收起" else "展开",
                modifier = Modifier
                    .padding(start = 100.dp),
            )
            // 展开/收起按钮
            IconToggleButton(
                checked = isExpanded,
                onCheckedChange = { isExpanded = !isExpanded },
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp
                    else Icons.Default.KeyboardArrowDown,
                    contentDescription = "展开收起")
            }
            //清空icon
            Icon(
                Icons.Default.Delete,
                contentDescription = "Delete Icon",
                //tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .weight(1f)
                    .clickable(onClick = onClear)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        if (historyList.isNotEmpty()){
            Log.d("historyListSizeTAG", historyList.toString())
            // 历史记录列表
            LazyColumn {
                items(
                    items = if (isExpanded) historyList
                    //打开行数
                    else historyList.take(maxCollapsedItems),
                    key = {it}
                ){
                    item ->
                    HistoryItem(
                        textList = item,
                        onSearchHistoryTextClicked = {word->
                            onSearchHistoryTextClicked(word)
                        })
                }
            }
        }
    }
}

/**
 * 显示历史搜索词条
 */
@Composable
fun HistoryItem(textList: List<String?>,onSearchHistoryTextClicked:(word:String)->Unit) {
    if (textList.size==2){
        Row  {
            textList[0]?.let {
                Text(
                    text = it,
                    modifier = Modifier
                        .padding(12.dp)
                        .weight(1f)
                        .clickable { onSearchHistoryTextClicked(it) }
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            textList[1]?.let {
                Text(
                    text = it,
                    modifier = Modifier
                        .padding(12.dp)
                        .weight(1f)
                        .clickable { onSearchHistoryTextClicked(it) }
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }else{
        Row  {
            textList[0]?.let {
                Text(
                    text = it,
                    modifier = Modifier
                        .padding(12.dp)
                        .weight(1f)
                        .clickable { onSearchHistoryTextClicked(it) }
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}


/**
 * 搜索栏
 */

@SuppressLint("RememberReturnType")
@Composable
fun SearchNewsBar(
    modifier: Modifier =Modifier,
    word:String?,
    onWordChange: (String) -> Unit,
    onSearchPress:(Boolean)-> Unit,
    onMicClicked:()->Unit
){
    // 唯一的状态：管理输入框的文本和光标位置
    val textFieldState = remember { mutableStateOf(TextFieldValue()) }

    // 关键点：监听外部 word 变化，同步到输入框并移动光标到末尾
    LaunchedEffect(word) {
        val newText = word ?: ""
        textFieldState.value = TextFieldValue(
            text = newText,
            selection = TextRange(newText.length) // 光标在末尾
        )
    }

    val coroutineScope = rememberCoroutineScope()
    // 用于在用户停止输入一段时间后再进行搜索请求
    var job by remember { mutableStateOf<Job?>(null) }
    // 创建 FocusRequester 来请求焦点
    val focusRequester = remember { FocusRequester() }
    // 获取键盘控制器
    val keyboardController = LocalSoftwareKeyboardController.current

    // 自动请求焦点并显示软键盘
    LaunchedEffect(Unit) {
        focusRequester.requestFocus() // 请求焦点
        keyboardController?.show()  // 显示软键盘
    }

    OutlinedTextField(
        //类似hint文本提示
        placeholder={ Text(text = "搜索", color = MaterialTheme.colorScheme.surfaceDim) },
        //左边的图标
        leadingIcon = {
            Icon(
                Icons.Filled.Search,
                contentDescription = "Search Icon")
        },
        //最右边的图标，语音
        trailingIcon ={
            /*VoiceInputIcon { recognizedText ->
                if (recognizedText != null) {
                    // 将识别到的文本更新到搜索框中
                    onWordChange(recognizedText)
                }
            }*/
            Icon(
                Icons.Filled.Mic,
                modifier = Modifier
                    .clickable {
                        onMicClicked()
                    }
                    .size(15.dp),
                contentDescription = "Mic Icon")
        },
        value = textFieldState.value,
        onValueChange = {newState->
            textFieldState.value=newState
            // 如果当前协程任务存在，则取消它
            job?.cancel()
            // 启动一个新的协程，并延迟触发 onWordChange
            job = coroutineScope.launch {
                delay(250) // 延迟500ms后再执行
                onWordChange(newState.text)
            }
        },
        keyboardOptions= KeyboardOptions.Default.copy(
            imeAction= ImeAction.Search
        ) ,
        keyboardActions = KeyboardActions(
            onSearch = {
                // 当搜索键被点击时，执行的操作，这里通过回调函数传出
                onSearchPress(true)
            }
        ),
        modifier = modifier.focusRequester(focusRequester)
    )
}
/*
*//**
 * 语音输入
 *//*
@Composable
fun VoiceInputScreen(
    onResult: (String) -> Unit,// 外部传入的回调
    onClose: () -> Unit
) {
    val context = LocalContext.current
    var statusText by remember { mutableStateOf("点击麦克风开始录音") }
    var recognizedText by remember { mutableStateOf("") }
    var isRecording by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }
    // 使用 rememberUpdatedState 保持最新的 onResult 引用


    // 初始化语音识别器（通过 remember 管理生命周期）
    val voiceRecognizer = remember {
        VoiceRecognizer(context).apply {
            onVoiceRecognizerResult = { text ->
                recognizedText = text
                onResult(text)
            }
            onError = { error ->
                statusText = error
                showError = true
                isRecording = false
            }
            onStatusUpdate = { status ->
                statusText = status
            }
        }
    }

    // 处理权限请求
    val recordAudioLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            //开始监听
            voiceRecognizer.startListening()
            isRecording = true
        } else {
            statusText = "需要麦克风权限"
        }
    }

    // 生命周期控制：界面销毁时释放资源
    DisposableEffect(Unit) {
        onDispose {
            voiceRecognizer.destroy()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // 录音动画（示例：动态麦克风图标）
            AnimatedMicIcon(isRecording = isRecording)

            Spacer(modifier = Modifier.height(24.dp))

            // 状态提示
            Text(
                text = statusText,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 识别结果，当语音输入完成时显示一个语音输入的文本，可删
            Text(
                text = recognizedText,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(32.dp))

            // 控制按钮
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Button(
                    onClick = {
                        if (isRecording) {
                            voiceRecognizer.stopListening()
                            isRecording = false
                        } else {
                            recordAudioLauncher.launch(Manifest.permission.RECORD_AUDIO)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isRecording) Color.Red else MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(
                        imageVector = if (isRecording) Icons.Filled.Stop else Icons.Filled.Mic,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (isRecording) "停止" else "开始")
                }

                if (isRecording) {
                    Button(onClick = onClose) {
                        Text("取消")
                    }
                }
            }
        }

        // 错误提示
        if (showError) {
            Snackbar(
                modifier = Modifier.align(Alignment.BottomCenter),
                action = {
                    TextButton(onClick = { showError = false }) {
                        Text("确定")
                    }
                }
            ) {
                Text(statusText)
            }
        }
    }
}

// 自定义录音动画组件
@Composable
private fun AnimatedMicIcon(isRecording: Boolean) {
    val animatedSize by animateDpAsState(
        targetValue = if (isRecording) 100.dp else 80.dp,
        animationSpec = tween(durationMillis = 500)
    )

    Box(
        modifier = Modifier
            .size(animatedSize)//动态改变大小
            .background(
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Filled.Mic,
            contentDescription = "麦克风",
            modifier = Modifier.size(40.dp),
            tint = MaterialTheme.colorScheme.primary,

        )
    }
}*/

