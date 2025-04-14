package com.example.newsreader.newsreaderlogin.ui.Screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.newsreader.R
import kotlinx.coroutines.delay

//在页面底部通过 BottomSheet 的形式展示登录界面
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginBottomSheetContent(
    //viewModel:MainViewModel,
    //传入导航条，实现跳转到协议
    navController: NavController,
    //onDismiss: () -> Unit,
    onClickVerificationCode: (String)->Unit,
    onLoginSuccess:   (String, String) -> Unit
) {
    var phoneNumber by remember { mutableStateOf("") }
    var verificationCode by remember { mutableStateOf("") }
    //记住隐私的checkbox
    var isPrivacyAccepted by remember { mutableStateOf(false) }
    var isCodeSent by remember { mutableStateOf(false) }
    var countdown by remember { mutableStateOf(60) }

    // 实现验证码从60每一秒减少1
    LaunchedEffect(isCodeSent) {
        if (isCodeSent) {
            while (countdown > 0) {
                delay(1000)
                countdown--
            }
            isCodeSent = false
            countdown = 60
        }
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 顶部标题
        Text(
            text = "登录新闻",
            style = MaterialTheme.typography.titleLarge,
        )

        Row (
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ){
            //+86文本框
            Text(
                text = "+86",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    //.padding(vertical = 16.dp)
            )
            // 手机号输入框
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { if (it.length <= 11) phoneNumber = it },  //如果长度小于11就显示，当手机号大于11就不显示了
                label = { Text("手机号") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),//点击后直接是123等数字的输入法
                modifier = Modifier
                    .fillMaxWidth()
            )
        }

        // 验证码输入框和获取验证码按钮
        Row(
            modifier = Modifier
                .fillMaxWidth(),
                //.padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = verificationCode,
                onValueChange = { if (it.length <= 6) verificationCode = it },
                label = { Text("验证码") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),//
                modifier = Modifier.weight(1f)
            )

            Button(
                onClick = {
                    // TODO: 发送验证码的逻辑
                    onClickVerificationCode(phoneNumber)
                    //viewModel.sendVerificationCode(SmsRequest(phoneNumber))
                    isCodeSent = true
                },
                enabled = phoneNumber.length == 11 && !isCodeSent,
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Text(if (isCodeSent) "${countdown}s" else "获取验证码")
            }
        }

        // 隐私政策同意选项
        Row(
            modifier = Modifier
                .fillMaxWidth(),
                //.padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isPrivacyAccepted,
                onCheckedChange = { isPrivacyAccepted = it }
            )

            PrivacyAgreementText(
                onUserAgreementClick = {
                // TODO(): 打开用户协议
                    navController.navigate("agreement/user")
                },
                onPrivacyPolicyClick = {
                // TODO(): 打开隐私协议
                    navController.navigate("agreement/privacy")

                }
            )
        }

        // 登录按钮
        Button(
            onClick = {
                // TODO: 实现登录逻辑
                onLoginSuccess(phoneNumber,verificationCode)
            },
            enabled = phoneNumber.length == 11 &&
                    verificationCode.length == 6 &&
                    isPrivacyAccepted,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text("登录")
        }

        // 第三方登录图标
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconButton(
                onClick = {
                    // TODO: 实现微信登录
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_wechat),
                    contentDescription = "微信登录",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(30.dp)
                )
            }

            IconButton(
                onClick = {
                    // TODO: 实现QQ登录
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_qq),
                    contentDescription = "QQ登录",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(30.dp)
                )
            }
        }
    }
}

//隐私政策、用户协议富文本
@Composable
private fun PrivacyAgreementText(modifier: Modifier=Modifier,onUserAgreementClick: () -> Unit, onPrivacyPolicyClick: () -> Unit) {
    val annotatedText = buildAnnotatedString {
        withStyle(SpanStyle(color = MaterialTheme.colorScheme.outlineVariant)) {
            append("我已阅读并同意")
        }

        // 添加《用户协议》并设置点击标识
        pushStringAnnotation(tag = "USER_AGREEMENT", annotation = "user_agreement")
        withStyle(SpanStyle(color = MaterialTheme.colorScheme.onSurface)) {
            append("《用户协议》")
        }
        pop() // 结束标记

        withStyle(SpanStyle(color = MaterialTheme.colorScheme.outlineVariant)) {
            append("和")
        }

        // 添加《隐私政策》并设置点击标识
        pushStringAnnotation(tag = "PRIVACY_POLICY", annotation = "privacy_policy")
        withStyle(SpanStyle(color = MaterialTheme.colorScheme.onSurface)) {
            append("《隐私政策》")
        }
        pop() // 结束标记
    }

    ClickableText(
        text = annotatedText,
        style = TextStyle(fontSize = 14.sp),
        onClick = { offset ->
            // 获取用户点击的部分
            annotatedText.getStringAnnotations(
                tag = "USER_AGREEMENT", start = offset, end = offset
            ).firstOrNull()?.let {
                onUserAgreementClick() // 触发用户协议点击事件
            }

            annotatedText.getStringAnnotations(
                tag = "PRIVACY_POLICY", start = offset, end = offset
            ).firstOrNull()?.let {
                onPrivacyPolicyClick() // 触发隐私政策点击事件
            }
        }
    )
}