package com.example.newsreader.collectionAndHistory.ui.components

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.newsreader.collectionAndHistory.data.model.NewsItem
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * 问题一：看能不能与新闻card重构
 * 显示收藏和历史记录新闻的Card
 * @param isEditMode:编辑模式
 * @param onCheckboxClick：选中要删除的新闻
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NewsItemCard(
    newsItem: NewsItem,
    isEditMode: Boolean,
    isSelected: Boolean,
    onItemClick: () -> Unit,
    onCheckboxClick: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    navController: NavController
){
    Card (
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onPrimary),
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            //点击Card跳转到新闻网页
            .clickable {
                onItemClick()
            }
            .padding(all = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
//            verticalAlignment = Alignment.CenterVertically
        ){
            // 编辑模式下显示复选框
            if (isEditMode) {
                Checkbox(
                    checked = isSelected,
                    onCheckedChange = onCheckboxClick,
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.CenterVertically)
                )
            }

            Column(
                modifier = Modifier
                    .weight(2f)
                    .padding(0.dp, 0.dp, 5.dp, 0.dp)
                    .weight(3f)
            ) {
                // 使用 Box 布局实现底部定位
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                ) {

                    Text(
                        style = MaterialTheme.typography.titleSmall,
                        text = newsItem.title,
                        maxLines = 3,
                    )
                    Text(
                        color = MaterialTheme.colorScheme.outlineVariant,
                        style = MaterialTheme.typography.bodySmall,
                        text = formatTimestamp(newsItem.timestamp),
                        modifier = Modifier
                            .align(Alignment.BottomStart)  // 设置底部对齐
                    )
                }
            }
            Log.d("AsyncImageTAG", newsItem.imgSrc)
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(newsItem.imgSrc)
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

//@RequiresApi(Build.VERSION_CODES.O)
//@Composable
//fun NewsItemCard(
//    newsItem: NewsItem,
//    isEditMode: Boolean,
//    isSelected: Boolean,
//    onItemClick: () -> Unit,
//    onCheckboxClick: (Boolean) -> Unit,
//    modifier: Modifier = Modifier
//) {
//    Card(
//        modifier = modifier
//            .fillMaxWidth()
//            .padding(horizontal = 16.dp, vertical = 8.dp)
//            .defaultMinSize(minHeight = 72.dp),
//        //点击
//        onClick = onItemClick
//    ) {
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp),
//            horizontalArrangement = Arrangement.spacedBy(16.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            // 编辑模式下显示复选框
//            if (isEditMode) {
//                Checkbox(
//                    checked = isSelected,
//                    onCheckedChange = onCheckboxClick,
//                    modifier = Modifier.size(24.dp)
//                )
//            }
//
//            // 新闻内容
//            Column(
//                modifier = Modifier.weight(1f)
//            ) {
//                Text(
//                    text = newsItem.title,
//                    style = MaterialTheme.typography.titleMedium,
//                    maxLines = 2,
//                    overflow = TextOverflow.Ellipsis
//                )
//                Spacer(modifier = Modifier.height(4.dp))
//                Text(
//                    text = formatTimestamp(newsItem.timestamp),
//                    style = MaterialTheme.typography.bodySmall,
//                    color = MaterialTheme.colorScheme.onSurfaceVariant
//                )
//            }
//        }
//    }
//}

@RequiresApi(Build.VERSION_CODES.O)
private fun formatTimestamp(timestamp: Long): String {
    val dateTime = LocalDateTime.ofInstant(
        Instant.ofEpochSecond(timestamp),
        ZoneId.systemDefault()
    )
    return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(dateTime)
}
