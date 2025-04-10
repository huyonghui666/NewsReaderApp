package com.example.newsreader.collectionAndHistory.ui


import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.newsreader.collectionAndHistory.model.NewsItem
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.favoriteNewsDataStore: DataStore<Preferences> by preferencesDataStore(name = "favorite_news")

@Singleton
class FavoriteRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val dataStore = context.favoriteNewsDataStore

    fun getFavoriteNews(): Flow<Set<String>> = dataStore.data.map { preferences ->
        preferences[PreferencesKeys.FAVORITE_NEWS] ?: emptySet()
    }

    suspend fun addFavoriteNews(newsItem: NewsItem) {
        dataStore.edit { preferences ->
            val currentFavorites = preferences[PreferencesKeys.FAVORITE_NEWS] ?: emptySet()
            preferences[PreferencesKeys.FAVORITE_NEWS] = currentFavorites + newsItem.url

            // 存储新闻标题
            preferences[stringPreferencesKey(newsItem.url)] = newsItem.title
        }
    }

    suspend fun removeFavoriteNews(newsItem: NewsItem) {
        dataStore.edit { preferences ->
            val currentFavorites = preferences[PreferencesKeys.FAVORITE_NEWS] ?: emptySet()
            preferences[PreferencesKeys.FAVORITE_NEWS] = currentFavorites - newsItem.url

            // 移除新闻标题
            preferences.remove(stringPreferencesKey(newsItem.url))
        }
    }

    fun isFavorite(url: String): Flow<Boolean> = dataStore.data.map { preferences ->
        val favorites = preferences[PreferencesKeys.FAVORITE_NEWS] ?: emptySet()
        url in favorites
    }

    private object PreferencesKeys {
        val FAVORITE_NEWS = stringSetPreferencesKey("favorite_news")
    }
}