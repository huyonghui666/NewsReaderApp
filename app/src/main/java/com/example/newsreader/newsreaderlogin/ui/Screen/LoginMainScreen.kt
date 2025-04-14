package com.example.newsreader.newsreaderlogin.ui.Screen

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.newsreader.newsreaderlogin.data.model.LoginRequest
import com.example.newsreader.newsreaderlogin.data.model.LoginResult
import com.example.newsreader.newsreaderlogin.data.model.SmsRequest
import com.example.newsreader.newsreaderlogin.ui.viewmodel.MainViewModel


@Composable
fun LoginMainScreen(
    viewModel: MainViewModel,
    navController: NavController,

    ) {
    //控制是否显示底部弹出框
    var showLoginSheet by remember { mutableStateOf(false) }

    //登录状态
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()
    //登录状态
    val loginState by viewModel.loginState.collectAsState()
    //底部提醒框状态
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(loginState) {
        when (loginState) {
            is LoginResult.Error -> {
                snackbarHostState.showSnackbar(
                    message =  (loginState as LoginResult.Error).exception,
                    duration = SnackbarDuration.Short
                )
            }
            is LoginResult.Success->{
                snackbarHostState.showSnackbar(
                    message = "登录成功",
                    duration = SnackbarDuration.Short
                )
            }
            // ...其他状态处理
            else -> {}
        }
    }

    Scaffold(
        snackbarHost = {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            ) {
                SnackbarHost(hostState = snackbarHostState) { data ->
                    // 你可以自定义 Snackbar 样式，也可以直接使用默认样式
                    Snackbar(snackbarData = data)
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.systemBars) // 避免与状态栏、导航栏重叠
                .fillMaxSize()
                .padding(paddingValues) //设置间距
                .background(MaterialTheme.colorScheme.background)
        ) {
            // 顶部登录区域
            TopLoginSection(isLoggedIn, onLoginClick={showLoginSheet = true})

            // 功能按钮区域
            FunctionButtonsRow( onClick = {text->
                if (isLoggedIn){
                    when(text){
                        //TODO 执行评论被点击相关操作
                        "评论" ->{}
                        //TODO 执行收藏被点击相关操作
                        "收藏" -> {navController.navigate("collection")}
                        //TODO 执行关注被点击相关操作
                        "关注" ->{}
                        //TODO 执行历史被点击相关操作
                        "历史" ->{
                            navController.navigate("history")}
                    }

                    //viewModel.getResourceToken()
                }else{
                    //显示底部弹出框
                    showLoginSheet=true
                }
            })

            // 工具列表
            ToolsList(onClick = {
                if (isLoggedIn){
                    //TODO 执行相应的操作

                }else{
                    //显示底部弹出框
                    showLoginSheet=true
                }
            })

        }

        //登录底部弹出框
        LoginBottomSheet(showLoginSheet,viewModel,navController,onDismiss = { showLoginSheet = false })

    }
}

/**
 * 登录底部弹出框
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LoginBottomSheet(
    showLoginSheet:Boolean,
    viewModel: MainViewModel,
    navController: NavController,
    onDismiss: () -> Unit){
    // 底部弹出框BottomSheet状态
    val bottomSheetState = rememberModalBottomSheetState()

    //登录状态
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()
    // 当点击登录时，进行底部弹出框
    if (showLoginSheet && !isLoggedIn) {
        ModalBottomSheet(
            //当用户点击弹窗外部或手势下拉关闭时，执行 onDismissRequest。
            onDismissRequest = onDismiss,
            sheetState = bottomSheetState,//根据状态改变底部弹框
        ) {
            //弹出框的内容
            LoginBottomSheetContent(
                //viewModel = viewModel,
                navController=navController,
                //onDismiss = { showLoginSheet = false },
                onClickVerificationCode = {phoneNumber->
                    viewModel.sendVerificationCode(SmsRequest(phoneNumber))
                },
                onLoginSuccess = {phoneNumber,verificationCode->
                    onDismiss()
                    //传入用户输入的手机号和验证码
                    viewModel.login(LoginRequest(phoneNumber,verificationCode))

                }
            )
        }
    }else{
        //处理登录状态触发顶部登录区域onLoginClick，导航到个人资料

    }
}

@Composable
private fun TopLoginSection(
    isLoggedIn: Boolean,
    onLoginClick: () -> Unit,
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable(onClick = onLoginClick), //触发点击回调
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)  //设置下面组件的水平间距
        ) {
            Image(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "User Avatar",
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
            )
            Text(
                text = if (isLoggedIn) "已登录用户" else "登录/注册",
                style = MaterialTheme.typography.titleMedium
            )
        }
        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = "Arrow"
        )

    }
}

//布局一个评论、关注、收藏、历史的组件
@Composable
private fun FunctionButtonsRow(
    onClick:  (String)->Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        FunctionButton("评论", Icons.Default.Comment, onClick = {onClick("评论")})
        FunctionButton("收藏", Icons.Default.Favorite,onClick = {onClick("收藏")})
        FunctionButton("关注", Icons.Default.BookmarkBorder,onClick = {onClick("关注")})
        FunctionButton("历史", Icons.Default.History,onClick = {onClick("历史")})
    }
}

@Composable
private fun FunctionButton(
    text: String,
    icon: ImageVector,
    onClick: (String) -> Unit

) {

    Column(modifier = Modifier.clickable {
        //执行点击操作
            onClick(text)
        }
    ){
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
private fun ToolsList(
    onClick: () -> Unit
) {
    val tools = listOf(
        //Tool("更多工具", Icons.Default.Build),
        Tool("我的消息", Icons.Default.Email),
        Tool("我的奖品", Icons.Default.Star),
        Tool("直通焦点访谈", Icons.Default.LiveTv),
        Tool("摇一摇", Icons.Default.Refresh),
        Tool("扫一扫", Icons.Default.QrCode),
        Tool("夜间模式", Icons.Default.DarkMode),
        Tool("意见反馈", Icons.Default.Feedback),
        Tool("设置", Icons.Default.Settings),
    )
    Text(
        text = "更多工具",
        modifier = Modifier.padding(16.dp))
    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
        items(tools) { tool ->
            ToolItem(tool,onClick)
        }
    }
}

@Composable
private fun ToolItem(tool: Tool, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
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




