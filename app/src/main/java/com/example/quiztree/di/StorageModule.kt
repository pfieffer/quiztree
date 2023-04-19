package com.example.quiztree.di

import android.content.Context
import androidx.room.Room
import com.example.quiztree.AppConstants
import com.example.quiztree.data.local.QuizDao
import com.example.quiztree.data.local.QuizDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StorageModule {
    @Singleton
    @Provides
    fun provideQuizDatabase(
        @ApplicationContext appContext: Context
    ): QuizDatabase {
        return Room.databaseBuilder(
            appContext,
            QuizDatabase::class.java,
            AppConstants.DB_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideQuizDao(
        quizDatabase: QuizDatabase
    ): QuizDao {
        return quizDatabase.getQuizDao()
    }

}