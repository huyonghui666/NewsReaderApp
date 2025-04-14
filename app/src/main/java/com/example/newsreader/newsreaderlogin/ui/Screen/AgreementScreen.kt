package com.example.newsreader.newsreaderlogin.ui.Screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

/**
 * 用户和隐私协议
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgreementScreen(
    navController: NavController,
    agreementType: AgreementType
) {
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = when (agreementType) {
                            AgreementType.USER_AGREEMENT -> "用户协议"
                            AgreementType.PRIVACY_POLICY -> "隐私政策"
                        }
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(scrollState)
        ) {
            when (agreementType) {
                AgreementType.USER_AGREEMENT -> UserAgreementContent()
                AgreementType.PRIVACY_POLICY -> PrivacyPolicyContent()
            }
        }
    }
}

@Composable
private fun UserAgreementContent() {
    AgreementSection(
        title = "一、总则",
        content = """
            1.1 用户协议（以下简称"本协议"）是您与我们之间关于使用本新闻阅读器应用（以下简称"本应用"）所订立的协议。

            1.2 在使用本应用之前，请您仔细阅读本协议。您点击"同意"或使用本应用，即表示您同意接受本协议的所有条款。
        """.trimIndent()
    )

    AgreementSection(
        title = "二、服务内容",
        content = """
            2.1 本应用向您提供新闻阅读、收藏、评论等服务。

            2.2 您可以通过本应用浏览新闻内容，发表评论，收藏感兴趣的内容。

            2.3 我们保留随时修改或中断服务的权利，而无需事先通知您。
        """.trimIndent()
    )

    AgreementSection(
        title = "三、用户行为规范",
        content = """
            3.1 您承诺遵守中华人民共和国相关法律法规。

            3.2 您不得利用本应用发布、传播违法信息。

            3.3 您不得干扰本应用的正常运行。
        """.trimIndent()
    )

    AgreementSection(
        title = "四、知识产权",
        content = """
            4.1 本应用的所有权和运营权归我们所有。

            4.2 本应用中的内容受著作权法和其他知识产权法律法规的保护。
        """.trimIndent()
    )
}

@Composable
private fun PrivacyPolicyContent() {
    AgreementSection(
        title = "一、信息收集",
        content = """
            1.1 我们收集的信息包括：
            - 您提供的个人信息（如手机号码）
            - 设备信息
            - 使用记录
            - 位置信息（如果您授权）

            1.2 我们承诺对您的个人信息进行严格保密。
        """.trimIndent()
    )

    AgreementSection(
        title = "二、信息使用",
        content = """
            2.1 我们使用收集的信息用于：
            - 提供、维护和改进服务
            - 向您推送个性化内容
            - 处理您的反馈
            - 预防欺诈和非法使用

            2.2 未经您的同意，我们不会向第三方分享您的个人信息。
        """.trimIndent()
    )

    AgreementSection(
        title = "三、信息保护",
        content = """
            3.1 我们采用行业标准的安全措施保护您的信息。

            3.2 我们使用加密技术确保数据传输的安全性。

            3.3 我们定期检查信息安全系统。
        """.trimIndent()
    )

    AgreementSection(
        title = "四、您的权利",
        content = """
            4.1 您有权：
            - 访问您的个人信息
            - 更正不准确的信息
            - 删除您的账号
            - 撤回同意

            4.2 如需行使上述权利，请联系我们的客服。
        """.trimIndent()
    )
}

@Composable
private fun AgreementSection(
    title: String,
    content: String
) {
    Column(
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = content,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
    }
}

enum class AgreementType {
    USER_AGREEMENT,
    PRIVACY_POLICY
}