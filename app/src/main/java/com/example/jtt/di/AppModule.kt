package com.example.jtt.di

import com.example.jtt.repository.FirebaseRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideFirebaseRepo(): FirebaseRepository = FirebaseRepository()
}

annotation class Singleton
