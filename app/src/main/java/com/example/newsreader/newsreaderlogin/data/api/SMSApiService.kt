package com.example.newsreader.newsreaderlogin.data.api

import com.example.newsreader.newsreaderlogin.data.model.SmsRequest
import com.example.newsreader.newsreaderlogin.data.model.SmsResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * 发送阿里云SMS短信
 */
interface SMSApiService {
    @POST("api/sms/send")
    suspend fun sendSms(@Body request: SmsRequest): Response<SmsResponse>

}