package com.example.newsreader.util

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log

class VoiceRecognizer(context: Context) : RecognitionListener {

    private val speechRecognizer: SpeechRecognizer =
        SpeechRecognizer.createSpeechRecognizer(context).apply {
            setRecognitionListener(this@VoiceRecognizer)
        }

    // 识别结果回调
    var onVoiceRecognizerResult: (String) -> Unit = {}
    var onError: (String) -> Unit = {}
    var onStatusUpdate: (String) -> Unit = {}

    // 开始录音
    fun startListening() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true) // 启用实时结果
        }
        speechRecognizer.startListening(intent)
    }

    // 停止录音
    fun stopListening() {
        speechRecognizer.stopListening()
    }

    // 释放资源
    fun destroy() {
        speechRecognizer.destroy()
    }

    // 实现 RecognitionListener 回调
    override fun onReadyForSpeech(params: Bundle?) {
        onStatusUpdate("准备就绪")
    }

    override fun onBeginningOfSpeech() {
        onStatusUpdate("正在录音...")
    }

    override fun onResults(results: Bundle?) {
        val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
        matches?.firstOrNull()?.let { onVoiceRecognizerResult(it) }
    }

    override fun onPartialResults(partialResults: Bundle?) {
        val partial = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
        partial?.firstOrNull()?.let { onVoiceRecognizerResult(it) }
    }

    override fun onEvent(p0: Int, p1: Bundle?) {
        TODO("Not yet implemented")
    }

    override fun onError(error: Int) {
        val errorMsg = when (error) {
            SpeechRecognizer.ERROR_NO_MATCH -> "未识别到内容"
            SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "录音超时"
            else -> "错误码: $error"
        }
        onError(errorMsg)
    }

    // 其他回调根据需要实现（如 onRmsChanged 可用于绘制波形）
    override fun onRmsChanged(rmsdB: Float) {
        // 可在此处更新音量波动动画
    }

    override fun onBufferReceived(p0: ByteArray?) {
        // 可记录日志或忽略
        Log.d("VoiceRecognizer", "onBufferReceived: ${p0?.size} bytes")
    }

    override fun onEndOfSpeech() {
        Log.d("VoiceRecognizer", "onEndOfSpeech")
    }

    // 省略其他空实现回调...
}