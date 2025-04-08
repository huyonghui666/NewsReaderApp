package com.example.newsreader

import android.os.Bundle
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.newsreader.ui.components.BottomNavBar
import com.example.newsreader.ui.screens.LoginScreen
import com.example.newsreader.ui.screens.MainScreen
import com.example.newsreader.ui.screens.MineScreen
import com.example.newsreader.ui.viewmodel.LoginViewModel
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
                var isLoggedIn by remember { mutableStateOf(false) }
                Scaffold(modifier = Modifier.windowInsetsPadding(WindowInsets.systemBars),
                    //底部导航条
                    bottomBar = { BottomNavBar(navController) },
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        //导航条包含首页和我的
                        NavHost(navController, startDestination = "home") {
                            //首页
                            composable("home") {
                                MainScreen()
                                //navController.navigate("profile")
                            }
                            //我的
                            composable("profile") {
                                MineScreen(
                                    navController = navController,
                                    isLoggedIn = isLoggedIn,
                                    onLoginClick = {
                                        if (!isLoggedIn) {
                                            navController.navigate("login")
                                            //navController.navigate("login")
                                        }
                                    }
                                )
                            }
                            // 在现有的 NavHost 中添加，登录界面
                            composable("login") {
                                val viewModel = viewModel<LoginViewModel>()
                                val uiState by viewModel.uiState.collectAsState()

                                LoginScreen(
                                    navController = navController,
                                    onLoginSuccess = {
                                        isLoggedIn = true
                                        navController.navigateUp()
                                    }
                                )
                            }
                        }
                    }
                }
                /*MainScreen()*/

            }
        }
    }
}

