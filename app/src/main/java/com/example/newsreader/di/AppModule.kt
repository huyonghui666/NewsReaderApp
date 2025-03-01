package com.example.newsreader.di

import android.content.Context
import com.example.newsreader.data.api.NewsShowApiService
import com.example.newsreader.data.api.SearchNewsApiService
import com.example.newsreader.data.remote.NewsShowRemoteDataSource
import com.example.newsreader.data.remote.SearchNewsDataRemote
import com.example.newsreader.data.repository.NewsShowRepositoryImpl
import com.example.newsreader.domain.repository.NewsShowRepository
import com.example.newsreader.util.PreferencesManager
import com.squareup.moshi.Moshi.*
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
     * 获取搜索的新闻
     */
    @Provides
    @Singleton
    fun provideSearchNewsApiService(okHttpClient: OkHttpClient): SearchNewsApiService {
        return Retrofit.Builder()
            .baseUrl("https://whyta.cn/api/tx/") // Replace with your API base URL
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(okHttpClient)
            .build()
            .create(SearchNewsApiService::class.java)
    }

    /**
     * 提供新闻数据源
     */
    @Provides
    @Singleton
    fun provideNewsShowRemoteDataSource(newsApiService: NewsShowApiService): NewsShowRemoteDataSource {
        return NewsShowRemoteDataSource(newsApiService)
    }

    /**
     * 提供搜索新闻数据源
     */
    @Provides
    @Singleton
    fun provideSearchNewsRemoteDataSource(searchNewsApiService: SearchNewsApiService): SearchNewsDataRemote {
        return SearchNewsDataRemote(searchNewsApiService)
    }

    /**
     *提供新闻仓库对象
     */
    @Provides
    @Singleton
    fun provideNewsShowRepository(
        newsShowRemoteDataSource: NewsShowRemoteDataSource,
        searchNewsRemote: SearchNewsDataRemote
    ): NewsShowRepository {
        return NewsShowRepositoryImpl(newsShowRemoteDataSource,searchNewsRemote)
    }

    /**
     * 提供sharePreferences的依赖
     */
    @Provides
    @Singleton
    fun providePreferencesManager(
        @ApplicationContext context: Context  // 自动注入 Application Context
    ): PreferencesManager {
        return PreferencesManager(context)
    }

}