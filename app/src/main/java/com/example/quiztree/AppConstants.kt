package com.example.quiztree

object AppConstants {
    object Api{
        const val BASE_URL = "https://opentdb.com/"

        enum class QuestionType {
            MULTIPLE {
                override fun toString(): String = "multiple"
            },
            TRUE_FALSE {
                override fun toString(): String = "boolean"
            }
        }
    }

    const val QUESTIONS_AMOUNT = 10

    const val DB_NAME = "quiz_database"

    const val TOTAL_TIME_ALLOWED_MILLIS = 120_000L
    const val HURRY_UP_TIME_SECONDS = 30
}