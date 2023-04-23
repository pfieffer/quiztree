package com.example.quiztree

import com.example.quiztree.data.local.QuizEntity

object TestUtil {
    fun createQuizEntities(size: Int): List<QuizEntity> {
        val quizList = arrayListOf<QuizEntity>()
        for (i in 0..size) {
            quizList.add(
                QuizEntity(
                    question = "Question ${i + 1}",
                    correctAnswer = "Correct answer for question ${i + 1}",
                    wrongAnswer1 = "Wrong answer 1 for question ${i + 1}",
                    wrongAnswer2 = "Wrong answer 2 for question ${i + 1}",
                    wrongAnswer3 = "Wrong answer 3 for question ${i + 1}"
                )
            )
        }
        return quizList
    }
}