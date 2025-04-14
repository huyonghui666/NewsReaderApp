package com.example.newsreader.newsreaderlogin.jwt

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.newsreader.newsreaderlogin.data.model.AuthTokens
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 令牌管理器 - 负责令牌的安全存储和管理
 * 使用EncryptedSharedPreferences确保令牌安全性
 */
@Singleton
class TokenManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private const val PREF_NAME = "auth_tokens"     // SharedPreferences的文件名
        private const val KEY_ACCESS_TOKEN = "access_token" //访问令牌
        private const val KEY_REFRESH_TOKEN = "refresh_token"   //刷新令牌
        private const val KEY_ACCESS_TOKEN_EXPIRY = "access_token_expiry" //访问令牌到期
        private const val KEY_REFRESH_TOKEN_EXPIRY="refresh_token-expiry" //刷新令牌到期时间

        // 令牌提前刷新时间（5分钟）
        //private const val TOKEN_REFRESH_THRESHOLD = 5 * 60 * 1000L
    }

    // 加密的SharedPreferences实例
    private val prefs by lazy {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)//AES256_GCM 作为加密方案
            .build()

        //用于创建加密版的 SharedPreferences，能够保护存储在其中的数据不被未经授权的访问或篡改。
        EncryptedSharedPreferences.create(
            context,
            PREF_NAME,  //文件名称
            masterKey,
            //指定键（Key）的加密方案，这里使用 AES256_SIV。这种模式能防止重放攻击，并确保键名的机密性。
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            //指定值（Value）的加密方案，这里使用 AES256_GCM。这种模式不仅加密数据，还确保数据完整性。
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    /**
     * 保存认证令牌
     */
    fun saveTokens(tokens: AuthTokens) {
        prefs.edit().apply {
            putString(KEY_ACCESS_TOKEN, tokens.accessToken)
            putString(KEY_REFRESH_TOKEN, tokens.refreshToken)
            //putLong(KEY_ACCESS_TOKEN_EXPIRY, System.currentTimeMillis() + tokens.accessExpiresIn)//保存访问令牌到期时间
            putLong(KEY_ACCESS_TOKEN_EXPIRY, tokens.accessExpiresIn)//保存访问令牌到期时间
            putLong(KEY_REFRESH_TOKEN_EXPIRY,tokens.refreshExpiresIn) //保存刷新令牌到期时间
            apply()
        }
    }

    /**
     * 获取访问令牌
     */
    fun getAccessToken(): String? {
        return prefs.getString(KEY_ACCESS_TOKEN, null)
    }

    /**
     * 获取（新）刷新令牌
     */
    fun getRefreshToken(): String? {
        return prefs.getString(KEY_REFRESH_TOKEN, null)
    }

    /**
     * 检查是否需要刷新令牌
     * 只有在有访问令牌的情况下才会返回true
     * 当距离过期时间小于阈值时返回true（提前五分钟刷新）
     */
//    fun shouldRefreshToken(): Boolean {
//        //val accessToken = getAccessToken() ?: return false
//        val expiry = prefs.getLong(KEY_ACCESS_TOKEN_EXPIRY, 0)
//        //Log.d("shouldRefreshTokenTAG", expiry.toString())
//        //Log.d("TAG", System.currentTimeMillis().toString())
//        //return System.currentTimeMillis() > expiry - TOKEN_REFRESH_THRESHOLD//提前五分钟
//        return System.currentTimeMillis() > expiry
//    }

    /**
     * 清除所有令牌
     */
    fun clearTokens() {
        prefs.edit().clear().apply()
    }
}