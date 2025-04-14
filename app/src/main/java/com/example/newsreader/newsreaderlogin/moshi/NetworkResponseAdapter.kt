//package com.example.newsreader.newsreaderlogin.moshi
//
//import com.example.newsreaderloginregistertest.data.model.LoginResult
//import com.squareup.moshi.Moshi
//import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory
//import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
//
//class NetworkResponseAdapter {
//    companion object {
//        fun create(): Moshi {
//            return Moshi.Builder()
//                .add(
//                    PolymorphicJsonAdapterFactory.of(LoginResult::class.java, "type")
//                        .withSubtype(LoginResult.Success::class.java, "success")
//                        .withSubtype(LoginResult.Error::class.java, "error")
//                )
//                .add(KotlinJsonAdapterFactory())
//                .build()
//        }
//    }
//}