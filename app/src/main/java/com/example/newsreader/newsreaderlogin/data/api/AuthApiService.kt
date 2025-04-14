package com.example.newsreader.newsreaderlogin.data.api

import com.example.newsreader.newsreaderlogin.data.model.ApiResponse
import com.example.newsreader.newsreaderlogin.data.model.AuthTokens
import com.example.newsreader.newsreaderlogin.data.model.LoginRequest
import com.example.newsreader.newsreaderlogin.data.model.RefreshTokenRequest
import com.example.newsreader.newsreaderlogin.data.model.ResponseResource
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * 认证相关的API接口定义
 */
interface AuthApiService {
    @POST("auth/login/phone")
    suspend fun loginWithPhone(@Body request: LoginRequest): Response<ApiResponse<AuthTokens>>
    @POST("auth/refresh")
    suspend fun refreshToken(@Body request: RefreshTokenRequest): Response<AuthTokens>

    @GET("auth/resource")
    suspend fun getResourceToken(): Response<ApiResponse<ResponseResource>>
}
