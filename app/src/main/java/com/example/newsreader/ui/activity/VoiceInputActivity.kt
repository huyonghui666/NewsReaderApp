package com.example.newsreader.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.window.OnBackInvokedCallback
import android.window.OnBackInvokedDispatcher
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.material3.MaterialTheme
import com.example.newsreader.ui.screens.VoiceInputScreenMain
import com.example.newsreader.ui.viewmodel.VoiceInputViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class VoiceInputActivity : ComponentActivity() {
/*
    // 获取 ViewModel 实例（依赖 Hilt 或其他依赖注入方式）
    private val voiceInputViewModel: VoiceInputViewModel by viewModels()

    private val onBackCallback = OnBackInvokedCallback {
        // 在此处设置返回数据并结束 Activity
        val resultIntent = Intent().apply {
            putExtra("historicalSearchList", voiceInputViewModel.historicalSearchList)
        }
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }*/

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 注册回调：注意需要指定一个优先级
        /*onBackInvokedDispatcher.registerOnBackInvokedCallback(
            OnBackInvokedDispatcher.PRIORITY_DEFAULT, onBackCallback
        )*/
        setContent {
            MaterialTheme {
                VoiceInputScreenMain()
            }
        }
    }


}

