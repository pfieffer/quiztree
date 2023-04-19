package com.example.quiztree.di

import com.example.quiztree.data.local.QuizDao
import com.example.quiztree.data.remote.QuizApiServices
import com.example.quiztree.data.repository.QuizRepository
import com.example.quiztree.data.repository.QuizRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideQuizRepository(
        quizApiServices: QuizApiServices,
        quizDao: QuizDao
    ): QuizRepository {
        return QuizRepositoryImpl(
            quizApiServices = quizApiServices, quizDao = quizDao
        )
    }

}