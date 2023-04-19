package com.example.quiztree

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.quiztree.data.local.QuizEntity
import com.example.quiztree.ui.theme.QuizTreeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel.fetchQuizList()
        setContent {
            QuizTreeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
//                    QuizQuestion(mainViewModel = mainViewModel)
                    HorizontalPager(
                        quizList = mainViewModel.quizFlow.collectAsState(initial = listOf()).value
                    )
                }
            }
        }
    }
}


/**
 * Stateful
 */
@Composable
fun HorizontalPager(quizList: List<QuizEntity>) {
    val quizAnswerMaps = arrayListOf<Map<Int, List<String>>>()
    quizList.forEachIndexed { index, quizEntity ->
        quizAnswerMaps.addAll(
            listOf(
                mapOf(
                    Pair(
                        index,
                        listOf(
                            quizEntity.correctAnswer,
                            quizEntity.wrongAnswer3,
                            quizEntity.wrongAnswer2,
                            quizEntity.wrongAnswer1
                        )
                    )
                )
            )
        )
    }
    HorizontalPager(
        quizQuestions = quizList.map { it.question },
        quizAnswers = quizAnswerMaps
    )
}

/**
 * State less
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HorizontalPager(
    quizQuestions: List<String>,
    quizAnswers: List<Map<Int, List<String>>>
) {
    val pagerState = rememberPagerState()
    HorizontalPager(
        pageCount = quizQuestions.count(),
        state = pagerState,
        userScrollEnabled = true
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        ) {
            Column {
                Row {
                    QuizQuestion(quizQuestions[pagerState.currentPage])
                }
                Row {
                    quizAnswers[pagerState.currentPage][pagerState.currentPage]?.let { answers ->
                        AnswerList(
                            answers = answers
                        )
                    }
                }
            }
        }
    }
}

/**
 * Stateful composable
 */
@Composable
fun QuizQuestion(mainViewModel: MainViewModel) {
    val quizList = mainViewModel.quizFlow.collectAsState(initial = listOf())
    quizList.value.forEach {
        QuizQuestion(questionText = it.question)
    }
}

/**
 * Stateless composable
 */
@Composable
fun QuizQuestion(questionText: String) {
    Text(
        text = questionText,
        modifier = Modifier.padding(10.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun QuizQuestionPreview() {
    QuizTreeTheme {
        QuizQuestion("Who was the first lady president of France?")
    }
}

@Composable
fun AnswerItem(answerText: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.Yellow)
            .padding(10.dp)
    ) {
        QuizTreeTheme {
            Text(text = answerText)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AnswerItemPreview() {
    QuizTreeTheme {
        AnswerItem("Franklin D. Roosevelt")
    }
}

@Composable
fun AnswerList(answers: List<String>) {
    LazyColumn {
        item {
            answers.forEach {
                AnswerItem(answerText = it)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AnswerListPreview() {
    QuizTreeTheme {
        AnswerList(answers = listOf("Franklin D. Roosevelt", "Lady Gaga", "Rihanna", "Anne Frank"))
    }
}
