package com.example.newsreader.collectionAndHistory.di

import com.example.newsreader.collectionAndHistory.data.api.CollectionAndHistoryApi
import com.example.newsreader.collectionAndHistory.data.repository.NewsRepository
import com.squareup.moshi.Moshi.Builder
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


//    @Provides
//    @Singleton
//    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
//        return OkHttpClient.Builder()
//            .addInterceptor(authInterceptor)
//            .build()
//    }

    val moshi = Builder()
        .add(KotlinJsonAdapterFactory()) // 关键：启用 Kotlin 数据类支持
        .build()

    @Provides
    @Singleton
    fun provideNewsApi(client: OkHttpClient): CollectionAndHistoryApi {
        return Retrofit.Builder()
            .baseUrl("https://test.newsreader.xin:443/") // 替换为实际的API地址
            .client(client)
            //.addConverterFactory(UnitConverterFactory())  // 添加Unit转换工厂
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(CollectionAndHistoryApi::class.java)
    }

    @Provides
    @Singleton
    fun provideNewsRepository(collectionAndHistoryApi: CollectionAndHistoryApi): NewsRepository {
        return NewsRepository(collectionAndHistoryApi)
    }
}