package com.example.newsreader

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.newsreader.collectionAndHistory.ui.screens.NewsWebViewScreen
import com.example.newsreader.newsreaderlogin.ui.Screen.AgreementScreen
import com.example.newsreader.newsreaderlogin.ui.Screen.AgreementType
import com.example.newsreader.newsreaderlogin.ui.Screen.LoginMainScreen
import com.example.newsreader.newsreaderlogin.ui.viewmodel.MainViewModel
import com.example.newsreader.newsreadershow.ui.components.BottomNavBar
import com.example.newsreader.newsreadershow.ui.screens.MainScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 这会告诉 Activity，界面不需要适应系统窗口的默认样式（例如，状态栏、导航栏等）。这对于全屏应用或需要自定义状态栏的颜色非常有用。
        WindowCompat.setDecorFitsSystemWindows(window, false)
        //使用 WindowInsetsControllerCompat 来控制状态栏的外观，如改变状态栏的图标颜色（浅色或深色）。这里设置
        // isAppearanceLightStatusBars = true，意味着状态栏上的图标将是浅色（如果你使用深色背景的状态栏）。
        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.isAppearanceLightStatusBars = true
        window.statusBarColor = resources.getColor(android.R.color.transparent) // 设置颜色
        setContent {
            MaterialTheme  {
                //底部导航条navController
                val navController = rememberNavController()
                val viewModel: MainViewModel = hiltViewModel()

                Scaffold(modifier = Modifier.windowInsetsPadding(WindowInsets.systemBars),
                    //底部导航条
                    bottomBar = { BottomNavBar(navController) },
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        //导航条包含首页和我的
                        NavHost(navController, startDestination = "home") {
                            //首页
                            composable("home") {
                                MainScreen(navController= navController)
                                //navController.navigate("profile")
                            }
                            //个人资料
                            composable("profile") {
                                LoginMainScreen(navController = navController,viewModel=viewModel)
//                                MineScreen(
//                                    navController = navController,
//                                    isLoggedIn = isLoggedIn,
//                                    onLoginClick = {
//                                        if (!isLoggedIn) {
//                                            navController.navigate("login")
//                                            //navController.navigate("login")
//                                        }
//                                    }
//                                )
                            }
                            //隐私和用户协议
                            composable(
                                route = "agreement/{type}",
                                arguments = listOf(
                                    navArgument("type") {
                                        type = NavType.StringType
                                        defaultValue = "user"
                                    }
                                )
                            ) { backStackEntry ->
                                val agreementType = when(backStackEntry.arguments?.getString("type")) {
                                    "user" -> AgreementType.USER_AGREEMENT
                                    "privacy" -> AgreementType.PRIVACY_POLICY
                                    else -> AgreementType.USER_AGREEMENT
                                }
                                AgreementScreen(
                                    navController = navController,
                                    agreementType = agreementType
                                )
                            }

                            // 在现有的 NavHost 中添加，登录界面
//                            composable("login") {
//                                val viewModel = viewModel<LoginViewModel>()
//                                val uiState by viewModel.uiState.collectAsState()
//
//                                LoginScreen(
//                                    navController = navController,
//                                    onLoginSuccess = {
//                                        isLoggedIn = true
//                                        navController.navigateUp()
//                                    }
//                                )
//                            }

                            // 添加WebView路由
                            composable(
                                route = "news_web_view/{url}",
                                arguments = listOf(
                                    navArgument("url") {
                                        type = NavType.StringType
                                        nullable = true //设置默认路由为空
                                    }
                                )
                            ) { backStackEntry ->
                                val url = backStackEntry.arguments?.getString("url") ?: ""
                                val title = backStackEntry.arguments?.getString("title") ?: "新闻详情"
                                //Log.d("titleTAG", title)
                                NewsWebViewScreen(
                                    url = url,
                                    title = title,
                                    onBackClick = { navController.navigateUp() }
                                )
                            }

                        }
                    }
                }


            }
        }
    }
}

