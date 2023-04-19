package com.example.quiztree.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        QuizEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class QuizDatabase : RoomDatabase() {
    abstract fun getQuizDao(): QuizDao
}