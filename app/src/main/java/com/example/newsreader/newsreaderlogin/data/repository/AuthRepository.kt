package com.example.newsreader.newsreaderlogin.data.repository

import android.util.Log
import com.example.newsreader.newsreaderlogin.data.api.AuthApiService
import com.example.newsreader.newsreaderlogin.data.model.ApiResponse
import com.example.newsreader.newsreaderlogin.data.model.AuthTokens
import com.example.newsreader.newsreaderlogin.data.model.LoginRequest
import com.example.newsreader.newsreaderlogin.data.model.LoginResult
import com.example.newsreader.newsreaderlogin.data.model.RefreshTokenRequest
import com.example.newsreader.newsreaderlogin.data.model.ResponseResource
import com.example.newsreader.newsreaderlogin.jwt.TokenManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 认证仓库 - 处理所有与认证相关的网络请求
 */
@Singleton
class AuthRepository @Inject constructor(
    private val api: AuthApiService,
    private val tokenManager: TokenManager
) {
    /**
     * 手机号验证码登录
     * @param phone 手机号
     * @param code 验证码
     * @return 认证令牌
     */
    suspend fun loginWithPhone(request: LoginRequest): LoginResult<AuthTokens> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.loginWithPhone(request)
                Log.d("loginWithPhoneTAG", response.body().toString())
                when(response.body()?.code){
                    200->{
                        response.body()!!.data?.let {
                            /**
                             * 要求服务端response返回
                             * data class AuthTokens(
                             *     val accessToken: String,    // JWT访问令牌，短期有效
                             *     val refreshToken: String,   // （新）刷新令牌，长期有效
                             *     val expiresIn: Long        // 访问令牌过期时间(秒)
                             * )
                             */
                            //保存Token
                            tokenManager.saveTokens(it)
                            LoginResult.Success(it)
                        } ?: LoginResult.Error(response.body()?.message ?: "Unknown error")
                    }
                    else->{
                        LoginResult.Error(response.body()?.message ?: "Unknown error")
                    }
                }

            }catch (e:Exception){
                LoginResult.Error(e.message ?: "Unknown error")
            }
        }
    }

    /**
     * 刷新令牌
     * @param refreshToken 刷新令牌
     * @return 新的认证令牌
     */
    suspend fun refreshToken(refreshToken: RefreshTokenRequest): AuthTokens {
         try {
            val response = api.refreshToken(refreshToken)
             return response.body()!!
        } catch (e: Exception) {
            throw Exception("Token refresh failed!${e.message}")
        }
    }

    /**
     * 访问需要Token的资源
     */
    suspend fun getResourceToken(): ApiResponse<ResponseResource> {
        try {
            val response = api.getResourceToken()
            //Log.d("getResourceTokenTAG", response.body().toString())
            if (response.isSuccessful && response.body() != null) {
                return response.body()!!
            } else {
                return ApiResponse(
                    code = 401,
                    message = "未授权",
                    data = null
                )
            }
        } catch (e: Exception) {
            throw Exception("Token refresh failed!")
        }
    }
}
