package com.example.newsreader.util

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager(context:Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)

    // 保存字符串数据
    fun saveString(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    // 获取字符串数据
    fun getString(key: String, defaultValue: String = ""): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }

    // 清除数据
    fun clear() {
        sharedPreferences.edit().clear().apply()
    }
}