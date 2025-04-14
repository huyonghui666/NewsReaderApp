package com.example.newsreader.newsreaderlogin.jwt

import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton
import android.util.Log
import com.example.newsreader.newsreaderlogin.data.model.RefreshTokenRequest
import com.example.newsreader.newsreaderlogin.data.repository.AuthRepository

import com.example.newsreader.newsreaderlogin.loginout.AuthEvent
import com.example.newsreader.newsreaderlogin.loginout.EventBus
import okhttp3.Protocol
import okhttp3.ResponseBody

@Singleton
class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager,
    private val authRepositoryProvider: Provider<AuthRepository>
) : Interceptor {

    companion object {
        private const val TAG = "AuthInterceptor"
        private val EXCLUDED_PATHS = listOf(
            "/login",
            "/refresh",
            "/api/sms/send",
            "/lundroid"
        )
        // 定义一个特殊的状态码，表示需要刷新令牌
        private const val TOKEN_REFRESH_REQUIRED = 498
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        //获取请求request
        val originalRequest = chain.request()
        //获取请求url
        val path = originalRequest.url.toString()

        // 如果是排除的路径，chain.proceed(originalRequest)直接放行
        if (isExcludedPath(path)) {
            return chain.proceed(originalRequest)
        }

        // 获取访问令牌
        val accessToken = tokenManager.getAccessToken()
        if (accessToken == null) {
            Log.d(TAG, "No access token found")
            return handleUnauthorized(chain, originalRequest)
        }

        // 执行请求并且检查是否需要刷新令牌
        return proceedWithToken(chain, originalRequest, accessToken)

    }

    /**
     * 重试请求
     */
    private fun handleTokenRefresh(chain: Interceptor.Chain, originalRequest: Request): Response {
        return try {
            // 尝试刷新令牌，并且返回刷新后的访问令牌
            val newToken = refreshTokenWithRetry()
            if (newToken != null) {
                // 使用新令牌重试请求
                proceedWithToken(chain, originalRequest, newToken)
            } else {
                // 刷新失败，返回未授权响应
                handleUnauthorized(chain, originalRequest)
            }
        } catch (e: Exception) {
            handleUnauthorized(chain, originalRequest)
        }
    }

    /**
     * 重新向服务器获取token，并且返回新的访问令牌
     */
    private fun refreshTokenWithRetry(): String? {
        return runBlocking {
            try {
                val refreshToken = tokenManager.getRefreshToken()
                    ?: throw Exception("Refresh token not found")

                val authRepository = authRepositoryProvider.get()
                val newTokens = authRepository.refreshToken(RefreshTokenRequest(refreshToken))
                //保存
                tokenManager.saveTokens(newTokens)
                newTokens.accessToken
            } catch (e: Exception) {
                tokenManager.clearTokens()
                null
            }
        }
    }

    /**
     * 执行请求并且检查是否需要刷新令牌
     */
    private fun proceedWithToken(
        chain: Interceptor.Chain,
        request: Request,
        token: String
    ): Response {
        //添加请求头携带token
        val authenticatedRequest = request.newBuilder()
            .header("Authorization", "Bearer $token")
            .build()

        val response = chain.proceed(authenticatedRequest)

        // 检查是否需要刷新令牌（服务端返回特定状态码498）
        if (response.code == TOKEN_REFRESH_REQUIRED) {
            response.close()  // 关闭不再使用的 Response，释放底层资源
            return handleTokenRefresh(chain, request)
        }

        /**
         * 这个会返回到Response<ApiResponse<ResponseResource>>
         * @GET("auth/resource")
         *     suspend fun getResourceToken(): Response<ApiResponse<ResponseResource>>
         */
        return response
    }

    /**
     * 处理未认证
     */
    private fun handleUnauthorized(chain: Interceptor.Chain, request: Request): Response {
        tokenManager.clearTokens()
        //TODO 发送登出事件
        runBlocking {
            Log.d("登出TAG", "登出")
            EventBus.emit(AuthEvent.Unauthorized)
        }


        //  创建一个简单的401响应
        return Response.Builder()
            .request(request)
            .code(401)  // HTTP 401 Unauthorized
            .message("Unauthorized")
            .protocol(Protocol.HTTP_1_1)
            .body(ResponseBody.create(null, ByteArray(0)))
            .build()
    }

    /**
     * 允许放行的url
     */
    private fun isExcludedPath(path: String): Boolean {
        return EXCLUDED_PATHS.any { path.contains(it) }
    }
}
