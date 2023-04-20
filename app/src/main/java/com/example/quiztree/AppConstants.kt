package com.example.quiztree

object AppConstants {
    const val BASE_URL = "https://opentdb.com/"

    enum class QuestionType {
        MULTIPLE {
            override fun toString(): String = "multiple"
        },
        TRUE_FALSE {
            override fun toString(): String = "boolean"
        }
    }

    const val QUESTIONS_AMOUNT = 10

    const val DB_NAME = "quiz_database"

    enum class TimeUnit {
        SEC, MIN
    }
}