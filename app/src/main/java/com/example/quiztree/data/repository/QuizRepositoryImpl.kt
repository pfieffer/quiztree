package com.example.quiztree.data.repository

import com.example.quiztree.AppConstants
import com.example.quiztree.data.local.QuizDao
import com.example.quiztree.data.local.QuizEntity
import com.example.quiztree.data.local.toQuizEntity
import com.example.quiztree.data.remote.QuizApiServices
import com.example.quiztree.utils.DataResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class QuizRepositoryImpl(
    private val quizApiServices: QuizApiServices,
    private val quizDao: QuizDao
) : QuizRepository {
    override suspend fun fetchQuizList(): DataResource<List<QuizEntity>> {
        return try {
            val response =
                quizApiServices.getQuizList(
                    amount = AppConstants.QUESTIONS_AMOUNT_PER_ROUND,
                    type = AppConstants.Api.QuestionType.MULTIPLE.toString()
                )
            if (response.isSuccessful) {
                withContext(Dispatchers.IO) {
                    quizDao.deleteAll()
                }
                val quizList = arrayListOf<QuizEntity>()
                response.body()?.results?.let { quizDtoList ->
                    quizDtoList.map { dto ->
                        dto.toQuizEntity().let {
                            withContext(Dispatchers.IO) {
                                quizDao.insert(it)
                            }
                            quizList.add(it)
                        }
                    }
                }
                return DataResource.Success(quizList)
            } else {
                throw HttpException(response)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is HttpException) {
                DataResource.Error(exception = e, message = e.message(), code = e.code())
            } else {
                DataResource.Error(
                    exception = e,
                    message = e.message ?: "Sorry. Something went wrong",
                    code = null
                )
            }
        }
    }

    override fun quizListFlow(): Flow<List<QuizEntity>> {
        return quizDao.getAllQuiz()
    }
}