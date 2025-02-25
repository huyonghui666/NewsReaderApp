package com.example.newsreader.di

import com.example.newsreader.data.api.NewsShowApiService
import com.example.newsreader.data.remote.NewsShowRemoteDataSource
import com.example.newsreader.data.repository.NewsShowRepositoryImpl
import com.example.newsreader.domain.repository.NewsShowRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.Moshi.*
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    /**
     *提供 OkHttpClient（可添加拦截器）
     */
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            //HttpLoggingInterceptor 是 OkHttp 提供的一个拦截器，用于记录 HTTP 请求和响应的信息。
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    // 创建 Moshi 实例
    val moshi = Builder()
        .add(KotlinJsonAdapterFactory()) // 关键：启用 Kotlin 数据类支持
        .build()

    /**
     * 获取指定频道的新闻
     */
    @Provides
    @Singleton
    fun provideNewsApiService(okHttpClient: OkHttpClient): NewsShowApiService {
        return Retrofit.Builder()
            .baseUrl("https://lznews.market.alicloudapi.com/lundroid/") // Replace with your API base URL
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(okHttpClient)
            .build()
            .create(NewsShowApiService::class.java)
    }

    /**
     * 提供数据源
     */
    @Provides
    @Singleton
    fun provideNewsShowRemoteDataSource(newsApiService: NewsShowApiService): NewsShowRemoteDataSource {
        return NewsShowRemoteDataSource(newsApiService)
    }

    /**
     *提供新闻仓库对象
     */
    @Provides
    @Singleton
    fun provideNewsShowRepository(
        remoteDataSource: NewsShowRemoteDataSource,
    ): NewsShowRepository {
        return NewsShowRepositoryImpl(remoteDataSource)
    }

}