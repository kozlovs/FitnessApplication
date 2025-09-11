package com.example.fitnessapplication.di

import com.example.fitnessapplication.data.repository.VideosRepositoryImpl
import com.example.fitnessapplication.data.repository.WorkoutsRepositoryImpl
import com.example.fitnessapplication.domain.repository.VideosRepository
import com.example.fitnessapplication.domain.repository.WorkoutsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
interface RepositoryModule {

    @Singleton
    @Binds
    fun bindWorkoutsRepository(impl: WorkoutsRepositoryImpl): WorkoutsRepository

    @Singleton
    @Binds
    fun bindVideosRepository(impl: VideosRepositoryImpl): VideosRepository
}