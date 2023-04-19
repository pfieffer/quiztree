package com.example.quiztree.data.repository

import com.example.quiztree.data.local.QuizEntity
import com.example.quiztree.utils.DataResource
import kotlinx.coroutines.flow.Flow

interface QuizRepository {
    suspend fun fetchQuizList(): DataResource<List<QuizEntity>>
    fun quizListFlow(): Flow<List<QuizEntity>>
}