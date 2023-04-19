package com.example.quiztree.data.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface QuizApiServices {
    @GET("api.php")
    suspend fun getQuizList(
        @Query("amount") amount: Int,
        @Query("type") type: String
    ): Response<QuizResponse>
}