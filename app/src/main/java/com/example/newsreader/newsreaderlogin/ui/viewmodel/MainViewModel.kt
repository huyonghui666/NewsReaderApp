package com.example.newsreader.newsreaderlogin.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsreader.newsreaderlogin.data.datastore.UserPreferences
import com.example.newsreader.newsreaderlogin.data.model.AuthTokens
import com.example.newsreader.newsreaderlogin.data.model.LoginRequest
import com.example.newsreader.newsreaderlogin.data.model.LoginResult
import com.example.newsreader.newsreaderlogin.data.model.SmsRequest
import com.example.newsreader.newsreaderlogin.data.repository.AuthRepository
import com.example.newsreader.newsreaderlogin.data.repository.SmsRepository
import com.example.newsreader.newsreaderlogin.loginout.AuthEvent
import com.example.newsreader.newsreaderlogin.loginout.EventBus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val smsRepository: SmsRepository,
    private val userPreferences: UserPreferences,
    private val authRepository: AuthRepository,
) : ViewModel() {

    //登录状态
    private val _loginState = MutableStateFlow<LoginResult<AuthTokens>?>(null)
    val loginState = _loginState.asStateFlow()

    //是否是登录状态
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()


    init {
        // 初始化时检查登录状态，开启两个协程避免阻塞
        viewModelScope.launch {
            userPreferences.isLoggedIn.collect { loginState ->
                _isLoggedIn.value = loginState
            }
        }
        // 在 ViewModel 中监听认证事件
        viewModelScope.launch {
            EventBus.events.collect { event ->
                when (event) {
                    is AuthEvent.Unauthorized -> {
                        // 处理登出事件，修改登录状态
                        updateLoginState(false)
                    }
                    else -> {} // 处理其他事件
                }
            }
        }

    }

    //更新登录状态，使用DataStore更新
     private fun updateLoginState(isLoggedIn: Boolean) {
        viewModelScope.launch {
            userPreferences.updateLoginState(isLoggedIn)
            _isLoggedIn.value = isLoggedIn
        }
    }


    /**
     * 阿里云发送短信
     */
    fun sendVerificationCode(request: SmsRequest) {
        viewModelScope.launch {
            //_smsState.value = SmsState.Sending
            smsRepository.sendVerificationCode(request)
        }
    }

    /**
     * 执行登录流程，传入手机号和短信验证码
     */
    fun login(request: LoginRequest){
        viewModelScope.launch {
            //使用手机号加验证码登录
            try {
                val result = authRepository.loginWithPhone(request)
                _loginState.value = result
                when(loginState.value){
                    is LoginResult.Success->{
                        updateLoginState(true)
                    }
                    is LoginResult.Error->{
                        updateLoginState(false)
                    }
                    else -> {updateLoginState(false)}
                }
            } catch (e: Exception) {
                _loginState.value = LoginResult.Error(e.message ?: "Unknown error")
            }
        }
    }

    /**
     *  测试服务器访问资源
     */
    fun getResourceToken(){
        viewModelScope.launch {
            val authTokensSmsResult = authRepository.getResourceToken()
            Log.d("LoginViewModelTAG", authTokensSmsResult.toString())
        }
    }
}