package com.example.newsreader.newsreaderlogin.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

//为 Context 扩展一个 dataStore 属性，使用 preferencesDataStore 委托创建或获取一个 DataStore 实例，指定存储文件名user_preferences
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")
//单例
@Singleton
class UserPreferences @Inject constructor(
    //注入全局Context
    @ApplicationContext private val context: Context
) {
    //通过context获取dataStore属性
    private val dataStore = context.dataStore

    //静态块，可以通过PreferencesKeys.IS_LOGGED_IN获取key
    private  object PreferencesKeys {
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        val IS_FIRST_TIME = booleanPreferencesKey("is_first_time")
    }

    //可以实时监听数据变化
    val isLoggedIn: Flow<Boolean> = dataStore.data
        .map {preferences ->
            //preferences是Preferences的对象，获取键的布尔值，返回布尔流
            preferences[PreferencesKeys.IS_LOGGED_IN] ?: false
        }

    //更新登录状态，edit：事务性写入操作，保证原子性（要么全部成功，要么全部失败）。
    suspend fun updateLoginState(isLoggedIn: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_LOGGED_IN] = isLoggedIn
        }
    }

    // 清空存储数据
    suspend fun clearUserData() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}