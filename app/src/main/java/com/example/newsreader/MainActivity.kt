package com.example.newsreader

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.newsreader.ui.screens.MainScreen
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
                MainScreen()

            }
        }
    }
}

