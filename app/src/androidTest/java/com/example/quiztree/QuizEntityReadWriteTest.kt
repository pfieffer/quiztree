package com.example.quiztree

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.quiztree.data.local.QuizDao
import com.example.quiztree.data.local.QuizDatabase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class QuizEntityReadWriteTest {
    private lateinit var quizDao: QuizDao
    private lateinit var db: QuizDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, QuizDatabase::class.java
        ).build()
        quizDao = db.getQuizDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeQuizListAndReadInList() = runTest {
        val quizList = TestUtil.createQuizEntities(3)
        quizList.forEach {
            quizDao.insert(it)
        }
        val allQuiz = quizDao.getAllQuiz().
        assertThat(allQuiz[0].question, equalTo(quizList[0].question))
        assertThat(allQuiz[0].correctAnswer, equalTo(quizList[0].correctAnswer))
        assertThat(allQuiz[0].wrongAnswer1, equalTo(quizList[0].wrongAnswer1))
        assertThat(allQuiz[0].wrongAnswer2, equalTo(quizList[0].wrongAnswer2))
        assertThat(allQuiz[0].wrongAnswer3, equalTo(quizList[0].wrongAnswer3))
    }
}
