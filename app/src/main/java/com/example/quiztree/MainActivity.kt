package com.example.quiztree

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.quiztree.data.local.QuizEntity
import com.example.quiztree.ui.AnswerList
import com.example.quiztree.ui.NameInput
import com.example.quiztree.ui.QuizQuestion
import com.example.quiztree.ui.theme.QuizTreeTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

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
                    HorizontalPager(
                        quizList = mainViewModel.quizFlow.collectAsState(initial = listOf()).value,
                        onNameProvided = {
                            if (it.isEmpty()) {
                                Toast.makeText(
                                    this,
                                    "Please provide your name",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                mainViewModel.nameInput.postValue(it)
                            }
                        }
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
fun HorizontalPager(quizList: List<QuizEntity>, onNameProvided: (name: String) -> Unit) {
    val quizAnswerMap = hashMapOf<Int, List<String>>()
    quizList.forEachIndexed { index, quizEntity ->
        quizAnswerMap[index] = listOf(
            quizEntity.correctAnswer,
            quizEntity.wrongAnswer3,
            quizEntity.wrongAnswer2,
            quizEntity.wrongAnswer1
        ).shuffled()
    }
    val correctAnswerIndices = arrayListOf<Int>()
    // correct
    quizList.forEachIndexed { index, quizEntity ->
        quizAnswerMap[index]?.let { innerList ->
            correctAnswerIndices.add(
                innerList.indexOf(
                    quizEntity.correctAnswer
                )
            )
        }
    }
    HorizontalPager(
        quizQuestions = quizList.map { it.question },
        quizAnswers = quizAnswerMap,
        correctAnswerIndices = correctAnswerIndices,
        onNameProvided = onNameProvided
    )
}

/**
 * State less Composable
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HorizontalPager(
    quizQuestions: List<String>,
    quizAnswers: Map<Int, List<String>>,
    correctAnswerIndices: List<Int>,
    onNameProvided: (name: String) -> Unit
) {
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    androidx.compose.foundation.pager.HorizontalPager(
        pageCount = quizQuestions.count()
                + 2,
        state = pagerState,
        userScrollEnabled = true
    ) {
        if (pagerState.currentPage == 0) {
            Box {
                Column(
                    Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        NameInput(onDoneClicked = {
                            onNameProvided(it)
                            if (it.isNotEmpty()) {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(1)
                                }
                            }
                        })
                    }
                }
            }
            return@HorizontalPager
        }
//        if (pagerState.currentPage == quizQuestions.size) {
//            //todo: Show score on last page
//            Log.i("On page last", "Show score to the user")
//            return@HorizontalPager
//        }
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
                    quizAnswers[pagerState.currentPage]?.let { answers ->
                        AnswerList(
                            answers = answers,
                            correctAnsIndex = correctAnswerIndices[pagerState.currentPage]
                        )
                    }
                }
            }
        }
    }
}