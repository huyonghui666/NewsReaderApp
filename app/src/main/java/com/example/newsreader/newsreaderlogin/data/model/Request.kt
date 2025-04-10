package com.example.newsreader.newsreaderlogin.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * 认证令牌数据类
 * 包含访问令牌和刷新令牌
 */
@JsonClass(generateAdapter = true)
data class AuthTokens(
    val accessToken: String,    // JWT访问令牌，短期有效
    val refreshToken: String,   // （新）刷新令牌，长期有效
    val accessExpiresIn: Long,        // 访问令牌过期时间(毫秒)
    val refreshExpiresIn: Long  //刷新令牌到期时间
)

/**
 * 刷新令牌请求数据类
 */
data class RefreshTokenRequest(
    val refreshToken: String  // 刷新令牌
)