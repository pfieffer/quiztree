package com.example.quiztree.data.remote


import com.google.gson.annotations.SerializedName

data class QuizResponse(
    @SerializedName("response_code")
    val responseCode: Int,
    @SerializedName("results")
    val results: List<QuizDto>
) {
    data class QuizDto(
        @SerializedName("category")
        val category: String,
        @SerializedName("correct_answer")
        val correctAnswer: String,
        @SerializedName("difficulty")
        val difficulty: String,
        @SerializedName("incorrect_answers")
        val incorrectAnswers: List<String>,
        @SerializedName("question")
        val question: String,
        @SerializedName("type")
        val type: String
    )
}