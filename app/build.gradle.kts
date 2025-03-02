plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-kapt")  // 这行是启用 kapt 插件
    id("com.google.dagger.hilt.android") // 应用 Hilt 插件
}

android {
    namespace = "com.example.newsreader"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.newsreader"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }


    // 确保启用 buildConfig
    buildFeatures {
        buildConfig = true
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            // 使用正确的 Kotlin DSL 语法
            buildConfigField("String", "SearchNews_KEY", "\"96f163cda80b\"")
            buildConfigField("String", "NEWS_SHOW_API_KEY", "\"8180965e08914519b67aff44fb5fd9f3\"")
        }
        getByName("debug") {
            buildConfigField("String", "SearchNews_KEY", "\"96f163cda80b\"")
            buildConfigField("String", "NEWS_SHOW_API_KEY", "\"8180965e08914519b67aff44fb5fd9f3\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.paging.common.android)
    implementation(libs.androidx.material3.android)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // Hilt
    implementation("com.google.dagger:hilt-android:2.50")
    kapt("com.google.dagger:hilt-compiler:2.50")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0") // If using Hilt with Compose navigation

    // OkHttp Logging Interceptor
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0") // 确保版本号是最新的

    // Moshi
    implementation("com.squareup.moshi:moshi:1.15.0") // 核心库
    implementation("com.squareup.moshi:moshi-kotlin:1.15.0") // Kotlin 支持
    kapt("com.squareup.moshi:moshi-kotlin-codegen:1.15.0") // 注解处理（可选）

    // Coil
    implementation("io.coil-kt:coil-compose:2.6.0")

    //浏览器
    implementation("androidx.browser:browser:1.7.0")

    //Paging 3
    implementation("androidx.paging:paging-runtime:3.2.1")        // 核心库
    implementation("androidx.paging:paging-compose:3.2.1")       // Compose 支持
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2") // ViewModel

    //刷新组件
    implementation ("androidx.compose.material3:material3:1.3.1")

    //icons
    implementation ("androidx.compose.material:material-icons-extended:1.7.8")

}