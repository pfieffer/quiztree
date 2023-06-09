package com.example.quiztree

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.quiztree.AppConstants.QUESTIONS_AMOUNT_PER_ROUND
import com.example.quiztree.ui.AnswerList
import com.example.quiztree.ui.NameInput
import com.example.quiztree.ui.QuizQuestion
import com.example.quiztree.ui.Score
import com.example.quiztree.ui.Timer
import com.example.quiztree.ui.theme.LightPrimary
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
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(R.drawable.bg_orange),
                    contentDescription = "background_image",
                    contentScale = ContentScale.FillBounds
                )
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 0.dp),
                    color = Color.Transparent,
                ) {
                    val coroutineScope = rememberCoroutineScope()
                    val pagerState = rememberPagerState()

                    if (pagerState.currentPage in 1..QUESTIONS_AMOUNT_PER_ROUND) {
                        Row {
                            Timer(onTimerComplete = {
                                // Navigate to last screen on Timer Complete
                                coroutineScope.launch {
                                    pagerState.scrollToPage(QUESTIONS_AMOUNT_PER_ROUND + 1)
                                }
                            })
                        }
                    }
                    QuizPager(
                        viewModel = mainViewModel,
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
                        playerName = mainViewModel.nameInput.value.orEmpty()
                    ) {
                        mainViewModel.fetchQuizList()
                        mainViewModel.liveScoreLD.postValue(0)
                        coroutineScope.launch {
                            delay(700L)
                            pagerState.scrollToPage(1)
                        }
                    }
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
    viewModel: MainViewModel,
    onNameProvided: (name: String) -> Unit,
    pagerState: PagerState,
    totalScore: Int,
    incrementScore: () -> Unit,
    playerName: String,
    onPlayAgainClicked: () -> Unit
) {
    val quizListState by viewModel.quizFlow.collectAsState(initial = emptyList())

    val quizAnswerMap = hashMapOf<Int, List<String>>()
    quizListState.forEachIndexed { index, quizEntity ->
        quizAnswerMap[index] = listOf(
            quizEntity.correctAnswer,
            quizEntity.wrongAnswer3,
            quizEntity.wrongAnswer2,
            quizEntity.wrongAnswer1
        ).shuffled()
    }
    val correctAnswerIndices = arrayListOf<Int>()
    quizListState.forEachIndexed { index, quizEntity ->
        quizAnswerMap[index]?.let { innerList ->
            correctAnswerIndices.add(
                innerList.indexOf(
                    quizEntity.correctAnswer
                )
            )
        }
    }
    QuizPager(
        quizQuestions = quizListState.map { it.question },
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
                    modifier = Modifier
                        .fillMaxSize(),
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
        if (pagerState.currentPage == QUESTIONS_AMOUNT_PER_ROUND + 1) {
            //Show score on last page
            Box {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
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
                        .padding(top = dimensionResource(id = R.dimen.margin_padding_x_large))
                ) {
                    QuizQuestion(quizQuestions[pagerState.currentPage - 1])
                }
                Row(
                    modifier = Modifier
                        .padding(top = dimensionResource(id = R.dimen.margin_padding_x_large))
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
            Row(
                modifier = Modifier
                    .height(50.dp)
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(QUESTIONS_AMOUNT_PER_ROUND) { iteration ->
                    val color =
                        if (pagerState.currentPage - 1 == iteration) LightPrimary else Color.LightGray
                    Box(
                        modifier = Modifier
                            .padding(2.dp)
                            .clip(CircleShape)
                            .background(color)
                            .size(dimensionResource(id = R.dimen.page_indicator_size))
                    )
                }
            }
        }
    }
}