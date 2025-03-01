package com.example.newsreader.ui.components

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation.Companion.keyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.newsreader.R
import com.example.newsreader.domain.models.SearchNews
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


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
            .padding(16.dp)
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
    onSearchPress:(Boolean)-> Unit
){
    //wrod变化就重新执行初始化逻辑
    val textFieldValue= remember(word) { mutableStateOf(word?:"") }

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
            Icon(
                painter = painterResource(R.drawable.mic),
                modifier = Modifier
                    .size(15.dp),
                contentDescription = "Mic Icon")
        },
        value = textFieldValue.value,
        onValueChange = {
            textFieldValue.value=it
            // 如果当前协程任务存在，则取消它
            job?.cancel()
            // 启动一个新的协程，并延迟触发 onWordChange
            job = coroutineScope.launch {
                delay(500) // 延迟200ms后再执行
                onWordChange(it)
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
