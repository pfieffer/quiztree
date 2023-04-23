package com.example.quiztree.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Upsert
import androidx.sqlite.db.SupportSQLiteQuery
import kotlinx.coroutines.flow.Flow

@Dao
interface QuizDao {
    @Insert
    fun insert(quiz: QuizEntity)

    @Query("SELECT * FROM quiz_table")
    fun getAllQuiz(): Flow<List<QuizEntity>>

    @Query("DELETE FROM quiz_table")
    fun deleteAll()
}