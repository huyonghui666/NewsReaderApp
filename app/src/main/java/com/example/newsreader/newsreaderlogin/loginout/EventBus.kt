package com.example.newsreader.newsreaderlogin.loginout

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/**
 * 事件总线，通知登出等
 */
object EventBus {
    private val _events = MutableSharedFlow<AuthEvent>()
    val events = _events.asSharedFlow()

    /**
     * 发送登出事件，订阅者接受
     */
    suspend fun emit(event: AuthEvent) {
        _events.emit(event)
    }
}