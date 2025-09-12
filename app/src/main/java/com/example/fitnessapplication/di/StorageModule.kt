package com.example.fitnessapplication.di

import android.app.Application
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.cache.LeastRecentlyUsedCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.io.File
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class StorageModule {
    @OptIn(UnstableApi::class)
    @Provides
    @Singleton
    fun provideVideoCash(app: Application): SimpleCache {
        val cacheEvictor = LeastRecentlyUsedCacheEvictor(VIDEO_CACHE_SIZE)
        val databaseProvider = StandaloneDatabaseProvider(app)
        return SimpleCache(File(app.cacheDir, "media"), cacheEvictor, databaseProvider)
    }

    companion object {
        private const val VIDEO_CACHE_SIZE = 10L * 1024 * 1024  // 10MB cache
    }
}