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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
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
import com.example.quiztree.ui.Score
import com.example.quiztree.ui.Timer
import com.example.quiztree.ui.theme.QuizTreeTheme
import com.example.quiztree.utils.DataResource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.net.UnknownHostException

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            QuizTreeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 0.dp),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    val coroutineScope = rememberCoroutineScope()
                    val pagerState = rememberPagerState()

                    if (pagerState.currentPage in 1..10) {
                        Row {
                            Timer(onTimerComplete = {
                                // Navigate to last screen on Timer Complete
                                coroutineScope.launch {
                                    pagerState.scrollToPage(11)
                                }
                            })
                        }
                    }
                    QuizPager(
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
                        },
                        pagerState = pagerState,
                        totalScore = mainViewModel.liveScoreLD.value ?: 0,
                        incrementScore = {
                            val lastScore = mainViewModel.liveScoreLD.value ?: 0
                            mainViewModel.liveScoreLD.postValue(lastScore.inc())
                        },
                        playerName = mainViewModel.nameInput.value.orEmpty(),
                        onPlayAgainClicked = {
                            mainViewModel.fetchQuizList()
                            mainViewModel.liveScoreLD.postValue(0)
                            coroutineScope.launch {
                                delay(700L)
                                pagerState.scrollToPage(1)
                            }
                        }
                    )
                }
            }
        }

        with(mainViewModel) {
            fetchQuizList()

            quizMLD.observe(this@MainActivity) {
                when (it) {
                    is DataResource.Error -> {
                        if (it.exception is UnknownHostException) {
                            Toast.makeText(
                                this@MainActivity,
                                "Sorry but you appear to be offline",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            Toast.makeText(this@MainActivity, it.message, Toast.LENGTH_LONG).show()
                        }
                    }

                    is DataResource.Loading -> {}
                    is DataResource.Success -> {}
                }
            }
        }
    }
}


/**
 * Stateful
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun QuizPager(
    quizList: List<QuizEntity>,
    onNameProvided: (name: String) -> Unit,
    pagerState: PagerState,
    totalScore: Int,
    incrementScore: () -> Unit,
    playerName: String,
    onPlayAgainClicked: () -> Unit
) {
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
    QuizPager(
        quizQuestions = quizList.map { it.question },
        quizAnswers = quizAnswerMap,
        correctAnswerIndices = correctAnswerIndices,
        onNameProvided = onNameProvided,
        pagerState = pagerState,
        totalScore = totalScore,
        incrementScore = incrementScore,
        playerName = playerName,
        onPlayAgainClicked = onPlayAgainClicked
    )
}

/**
 * State less Composable
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun QuizPager(
    quizQuestions: List<String>,
    quizAnswers: Map<Int, List<String>>,
    correctAnswerIndices: List<Int>,
    onNameProvided: (name: String) -> Unit,
    pagerState: PagerState,
    totalScore: Int,
    incrementScore: () -> Unit,
    playerName: String,
    onPlayAgainClicked: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    HorizontalPager(
        pageCount = quizQuestions.count()
                + 2,
        state = pagerState,
        userScrollEnabled = false
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
                                    pagerState.scrollToPage(1)
                                }
                            }
                        })
                    }
                }
            }
            return@HorizontalPager
        }
        if (pagerState.currentPage == 11) {
            //Show score on last page
            Box {
                Column(
                    Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Score(
                            totalScoreOutOfTen = totalScore,
                            name = playerName,
                            onPlayAgainClicked = {
                                onPlayAgainClicked()
                            }
                        )
                    }
                }
            }
            return@HorizontalPager
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            contentAlignment = Alignment.Center
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .padding(top = 32.dp)
                ) {
                    QuizQuestion(quizQuestions[pagerState.currentPage - 1])
                }
                Row(
                    modifier = Modifier
                        .padding(top = 32.dp)
                ) {
                    quizAnswers[pagerState.currentPage - 1]?.let { answers ->
                        AnswerList(
                            answers = answers
                        ) { tappedIndex ->
                            if (tappedIndex == correctAnswerIndices[pagerState.currentPage - 1]) {
                                incrementScore()
                            }
                            coroutineScope.launch {
                                pagerState.scrollToPage(pagerState.currentPage.inc())
                            }
                        }
                    }
                }
            }
        }
    }
}