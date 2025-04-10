package com.example.newsreader.newsreaderlogin.di

import android.content.Context
import com.example.newsreader.newsreaderlogin.data.api.AuthApiService
import com.example.newsreader.newsreaderlogin.data.api.SMSApiService
import com.example.newsreader.newsreaderlogin.data.datastore.UserPreferences
import com.example.newsreader.newsreaderlogin.data.repository.SmsRepository

import com.example.newsreader.newsreaderlogin.jwt.AuthInterceptor
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton
import com.squareup.moshi.Moshi.*
import com.squareup.moshi.*


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /**
     * 提供datastore配置
     */
    @Provides
    @Singleton
    fun provideUserPreferences(
        @ApplicationContext context: Context
    ): UserPreferences {
        return UserPreferences(context)
    }

    /**
     * 提供OkHttpClient实例
     * 配置拦截器、超时等，
     * AuthInterceptor用于自动添加认证令牌到请求头并处理令牌刷新逻辑
     */
    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()
    }

    // 创建 Moshi 实例
    /*@Provides
    @Singleton*/
    /*fun provideMoshi(): Moshi = NetworkResponseAdapter.create()*/
    val moshi = Builder()
        .add(KotlinJsonAdapterFactory()) // 关键：启用 Kotlin 数据类支持
        .build()
    /**
     * 提供Retrofit实例
     */
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://test.newsreader.xin:443/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(okHttpClient)
            .build()
    }

    /**
     * 提供验证接口实例
     */
    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApiService {
        return retrofit.create(AuthApiService::class.java)
    }


    /**
     * 提供短信接口的实例
     */
    @Provides
    @Singleton
    fun provideSMSApiService(retrofit: Retrofit): SMSApiService {
        return retrofit.create(SMSApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideSmsRepository(
        smsApiService: SMSApiService,
    ): SmsRepository {
        return SmsRepository(smsApiService)
    }
}