package com.example.newsreader.ui.components

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.newsreader.domain.models.NewsShowModel


/**
 * 点击跳转到搜索栏
 */

@Composable
fun IntentSearchNewsBar(
    modifier: Modifier=Modifier
){
    Row(
        modifier=modifier
    ) {
        Icon(
            Icons.Default.Search,
            contentDescription = "Search Icon",

            )
        Text(text = "搜索",
            color = MaterialTheme.colorScheme.surfaceDim,
            modifier=Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        )
    }

}






//打开网页
fun openWebPage(context: Context, url: String) {
    try {
        // 确保 URL 格式正确
        val validUrl = if (!url.startsWith("http://") && !url.startsWith("https://")) {
            "http://$url" // 自动补全协议头
        } else {
            url
        }

        // 使用 Custom Tabs 优化体验
        val customTabsIntent = CustomTabsIntent.Builder().build()
        customTabsIntent.launchUrl(context, Uri.parse(validUrl))

    } catch (e: Exception) {
        Log.e("WebIntent", "打开网页失败: ${e.message}")
        Toast.makeText(context, "链接无效", Toast.LENGTH_SHORT).show()
    }
}

/**
 * Card用于显示新闻
 */

@Composable
fun NewsShowCard(
    newsShowModel: NewsShowModel,
){
    val context = LocalContext.current
    Card (
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onPrimary),
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            //点击Card跳转到新闻网页
            .clickable {
                newsShowModel.url?.let {
                    openWebPage(context, it)
                    Log.d("clickableTAG", it)
                }
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
                    newsShowModel.title?.let {
                        //Log.d("NewsShowCardTAG", newsShowModel.title)
                        Text(
                            style = MaterialTheme.typography.titleSmall,
                            text = it,
                            maxLines = 3,
                        )
                    }
                    newsShowModel.time?.let {
                        Text(
                            color = MaterialTheme.colorScheme.outlineVariant,
                            style = MaterialTheme.typography.bodySmall,
                            text = it,
                            modifier = Modifier
                                .align(Alignment.BottomStart)  // 设置底部对齐
                        )
                    }
                }
            }
            newsShowModel.imgsrc?.let {
                //Log.d("imgsrcTAG", it)
                //异步加载图片
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(it)
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
}


/**
 * 顶部导航条
 */
// 1. 定义分类数据模型
private data class NewsCategory(
    val id: Int,
    val title: String
)

// 2. 创建分类列表
private val newsCategories = listOf(
    NewsCategory(0, "新闻"),
    NewsCategory(1, "娱乐"),
    NewsCategory(2, "体育"),
    NewsCategory(3, "财经"),
    NewsCategory(4, "军事"),
    NewsCategory(5, "科技"),
    NewsCategory(6, "手机"),
    NewsCategory(7, "数码"),
    NewsCategory(8, "时尚"),
    NewsCategory(9, "游戏"),
    NewsCategory(10, "教育"),
    NewsCategory(11, "健康"),
    NewsCategory(12, "旅游")
)

// 3. 创建可组合函数
@Composable
fun NewsCategoryTabs(
    modifier: Modifier = Modifier,
    onCategorySelected: (String) -> Unit
) {
    var selectedCategory by remember { mutableStateOf(0) }

    ScrollableTabRow(
        selectedTabIndex = selectedCategory,
        modifier = modifier,
        edgePadding = 0.dp,
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.primary
    ) {
        newsCategories.forEachIndexed { index, category ->
            Tab(
                selected = selectedCategory == index,
                onClick = {
                    selectedCategory = index
                    //回调函数传出点击的title
                    onCategorySelected(category.title)
                },
                text = {
                    Text(
                        text = category.title,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                selectedContentColor = MaterialTheme.colorScheme.primary,
                unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * 底部导航条
 */

// 底部导航栏
@Composable
fun BottomNavBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        NavigationBarItem(
            selected = currentRoute == "home",
            onClick = { navController.navigate("home") },
            icon = { Icon(Icons.Default.Home, contentDescription = null) },
            label = { Text("首页") }
        )

        NavigationBarItem(
            selected = currentRoute == "profile",
            onClick = { navController.navigate("profile") },
            icon = { Icon(Icons.Default.Person, contentDescription = null) },
            label = { Text("我的") }
        )
    }
}