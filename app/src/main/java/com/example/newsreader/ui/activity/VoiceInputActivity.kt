package com.example.newsreader.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.window.OnBackInvokedCallback
import android.window.OnBackInvokedDispatcher
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.material3.MaterialTheme
import androidx.core.view.WindowCompat
import com.example.newsreader.ui.screens.VoiceInputScreenMain
import com.example.newsreader.ui.viewmodel.VoiceInputViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class VoiceInputActivity : ComponentActivity() {
    // 手势返回回调
    private val onBackCallback = OnBackInvokedCallback {
        setActivityResultAndFinish() // 统一设置结果
    }

    // 处理物理返回键
    @Suppress("DEPRECATION")
    override fun onBackPressed() {
        setActivityResultAndFinish() // 统一设置结果
        super.onBackPressed() // 调用父类方法确保关闭
    }

    // 统一设置结果并关闭 Activity
    private fun setActivityResultAndFinish() {
        val resultIntent = Intent().apply {
            putExtra("loadSearchHistory", true)
        }
        setResult(RESULT_OK, resultIntent)
        finish()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 使内容扩展到系统栏下方（如果需要自定义状态栏）
        WindowCompat.setDecorFitsSystemWindows(window, false)
        // 注册回调：注意需要指定一个优先级
        onBackInvokedDispatcher.registerOnBackInvokedCallback(
            OnBackInvokedDispatcher.PRIORITY_DEFAULT, onBackCallback
        )
        setContent {
            MaterialTheme {
                VoiceInputScreenMain()
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onDestroy() {
        super.onDestroy()
        onBackInvokedDispatcher.unregisterOnBackInvokedCallback(onBackCallback)
    }
}

