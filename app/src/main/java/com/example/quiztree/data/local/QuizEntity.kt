package com.example.quiztree.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quiz_table")
data class QuizEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val question: String,
    val correctAnswer: String,
    val wrongAnswer1: String,
    val wrongAnswer2: String,
    val wrongAnswer3: String
)