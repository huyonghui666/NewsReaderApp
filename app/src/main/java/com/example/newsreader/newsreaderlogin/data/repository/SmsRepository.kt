package com.example.newsreader.newsreaderlogin.data.repository

import com.example.newsreader.newsreaderlogin.data.api.SMSApiService
import com.example.newsreader.newsreaderlogin.data.model.SmsRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class SmsRepository @Inject constructor(
    private val smsApiService: SMSApiService,
) {

    /**
     * 阿里云发送短信
     */
    suspend fun sendVerificationCode(request: SmsRequest){
        withContext(Dispatchers.IO){
            try {
                smsApiService.sendSms(request)
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }

}