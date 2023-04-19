package com.example.quiztree.data.local

import com.example.quiztree.data.remote.QuizResponse

fun QuizResponse.QuizDto.toQuizEntity(): QuizEntity {
    return QuizEntity(
        question = question,
        correctAnswer = correctAnswer,
        wrongAnswer1 = incorrectAnswers[0],
        wrongAnswer2 = incorrectAnswers[1],
        wrongAnswer3 = incorrectAnswers[2],
    )
}