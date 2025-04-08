package com.example.newsreader.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun MineScreen(
    navController: NavController,
    isLoggedIn: Boolean = false,
    onLoginClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // 顶部登录区域
        TopLoginSection(isLoggedIn, onLoginClick)

        // 功能按钮区域
        FunctionButtonsRow()

        // 工具列表
        ToolsList()
    }
}

@Composable
private fun TopLoginSection(
    isLoggedIn: Boolean,
    onLoginClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable(onClick = onLoginClick),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Image(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "User Avatar",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
            )
            Text(
                text = if (isLoggedIn) "已登录用户" else "登录/注册",
                style = MaterialTheme.typography.titleMedium
            )
        }
        //>这个Icon
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = "Arrow"
        )
    }
}

@Composable
private fun FunctionButtonsRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        FunctionButton("评论", Icons.Default.Comment)
        FunctionButton("关注", Icons.Default.Favorite)
        FunctionButton("收藏", Icons.Default.BookmarkBorder)
        FunctionButton("历史", Icons.Default.History)
    }
}

@Composable
private fun FunctionButton(
    text: String,
    icon: ImageVector
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun ToolsList() {
    val tools = listOf(
        Tool("我的消息", Icons.Default.Email),
        Tool("我的奖品", Icons.Default.Star),
        Tool("直通焦点访谈", Icons.Default.LiveTv),
        Tool("摇一摇", Icons.Default.Refresh),
        Tool("扫一扫", Icons.Default.QrCode),
        Tool("夜间模式", Icons.Default.DarkMode),
        Tool("意见反馈", Icons.Default.Feedback),
        Tool("设置", Icons.Default.Settings)
    )

    Text(
        text = "更多工具",
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    )
    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
        items(tools) { tool ->
            ToolItem(tool)
        }
    }
}

@Composable
private fun ToolItem(tool: Tool) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = tool.icon,
                contentDescription = tool.name
            )
            Text(
                text = tool.name,
                style = MaterialTheme.typography.bodyLarge
            )
        }
        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = "Arrow"
        )
    }
}

data class Tool(
    val name: String,
    val icon: ImageVector
)