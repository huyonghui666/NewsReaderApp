package com.example.newsreader.newsreaderlogin.data.model

import com.squareup.moshi.JsonClass


/**
 * SMS短信请求体
 */
data class SmsRequest(val phone: String)

/**
 * SMS返回的数据
 */
data class SmsResponse(val result: String)

sealed class SmsResult<out T> {
    data class Success<T>(val data: T) : SmsResult<T>()
    data class Error(val exception: Throwable) : SmsResult<Nothing>()
}

/**
 * 登录返回的数据
 */

sealed interface LoginResult<out T> {
    data class Success<T>(val data: T) : LoginResult<T>
    data class Error(val exception: String) : LoginResult<Nothing>
}
/**
 * 创建一个网络响应包装类
 */
@JsonClass(generateAdapter = true)
data class ApiResponse<T>(
    val code: Int,
    val message: String?,
    val data: T?
)
//扩展函数处理响应
/*fun <T> ApiResponse<T>.toLoginResult(): LoginResult<T> = when {
    //这个data是后端返回的AuthTokens
    code == 200 && data != null -> LoginResult.Success(data)
    else -> LoginResult.Error(message ?: "Unknown error")
}*/


/**
 * 手机号登录请求数据类
 */
data class LoginRequest(
    val phone: String,
    val code: String
)

data class ResponseResource(val resource:String)
