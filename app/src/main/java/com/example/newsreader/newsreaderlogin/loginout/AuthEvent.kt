package com.example.newsreader.newsreaderlogin.loginout


sealed class AuthEvent {
    data class TokenRefreshed(val newToken: String) : AuthEvent()
    data class Error(val message: String) : AuthEvent()
    object Unauthorized : AuthEvent() // 未授权/需要登出
}