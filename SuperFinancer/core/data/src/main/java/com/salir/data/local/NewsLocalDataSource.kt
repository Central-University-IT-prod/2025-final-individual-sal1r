package com.salir.data.local

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.salir.data.model.NewsCache
import com.salir.util.AppDispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class NewsLocalDataSource(
    private val json: Json,
    private val context: Application,
    private val dispatchers: AppDispatchers
) {
    suspend fun saveCache(cache: NewsCache) = withContext(dispatchers.io) {
        context.newsCacheDataStore.edit { preferences ->
            preferences[newsCacheKey] = json.encodeToString(cache)
        }
    }

    suspend fun getCache(): NewsCache? = withContext(dispatchers.io) {
        context.newsCacheDataStore.data.map { preferences ->
            preferences[newsCacheKey]?.let {
                json.decodeFromString<NewsCache>(it)
            }
        }.first()
    }

    companion object {
        private val Context.newsCacheDataStore by preferencesDataStore("news_cache")
        private val newsCacheKey = stringPreferencesKey("newsCache")
    }
}