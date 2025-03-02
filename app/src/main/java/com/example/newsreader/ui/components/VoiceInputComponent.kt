package com.example.newsreader.ui.components

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.newsreader.util.VoiceRecognizer

/**
 * 语音输入
 */
@Composable
fun VoiceInputScreen(
    onResult: (String) -> Unit,// 外部传入的回调
    //onClose: () -> Unit
) {
    val context = LocalContext.current
    var statusText by remember { mutableStateOf("点击麦克风开始录音") }
    var recognizedText by remember { mutableStateOf("") }
    var isRecording by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }
    // 使用 rememberUpdatedState 保持最新的 onResult 引用


    // 初始化语音识别器（通过 remember 管理生命周期）
    val voiceRecognizer = remember {
        VoiceRecognizer(context).apply {
            onVoiceRecognizerResult = { text ->
                recognizedText = text
                onResult(text)
            }
            onError = { error ->
                statusText = error
                showError = true
                isRecording = false
            }
            onStatusUpdate = { status ->
                statusText = status
            }
        }
    }

    // 处理权限请求
    val recordAudioLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            //开始监听
            voiceRecognizer.startListening()
            isRecording = true
        } else {
            statusText = "需要麦克风权限"
        }
    }

    // 生命周期控制：界面销毁时释放资源
    DisposableEffect(Unit) {
        onDispose {
            voiceRecognizer.destroy()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // 录音动画（示例：动态麦克风图标）
            AnimatedMicIcon(isRecording = isRecording)

            Spacer(modifier = Modifier.height(24.dp))

            // 状态提示
            Text(
                text = statusText,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 识别结果，当语音输入完成时显示一个语音输入的文本，可删
            /*Text(
                text = recognizedText,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(32.dp))*/

            // 控制按钮
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Button(
                    onClick = {
                        if (isRecording) {
                            voiceRecognizer.stopListening()
                            isRecording = false
                        } else {
                            recordAudioLauncher.launch(Manifest.permission.RECORD_AUDIO)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isRecording) Color.Red else MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(
                        imageVector = if (isRecording) Icons.Filled.Stop else Icons.Filled.Mic,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (isRecording) "停止" else "开始")
                }

                /*if (isRecording) {
                    Button(onClick = onClose) {
                        Text("取消")
                    }
                }*/
            }
        }

        // 错误提示
        if (showError) {
            Snackbar(
                modifier = Modifier.align(Alignment.BottomCenter),
                action = {
                    TextButton(onClick = { showError = false }) {
                        Text("确定")
                    }
                }
            ) {
                Text(statusText)
            }
        }
    }
}

// 自定义录音动画组件
@Composable
private fun AnimatedMicIcon(isRecording: Boolean) {
    val animatedSize by animateDpAsState(
        targetValue = if (isRecording) 100.dp else 80.dp,
        animationSpec = tween(durationMillis = 500)
    )

    Box(
        modifier = Modifier
            .size(animatedSize)//动态改变大小
            .background(
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Filled.Mic,
            contentDescription = "麦克风",
            modifier = Modifier.size(40.dp),
            tint = MaterialTheme.colorScheme.primary,

            )
    }
}