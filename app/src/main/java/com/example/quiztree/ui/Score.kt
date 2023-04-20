package com.example.quiztree.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.quiztree.ui.theme.QuizTreeTheme
import com.example.quiztree.ui.theme.Typography
import com.example.quiztree.ui.theme.orange

@Composable
fun Score(totalScoreOutOfTen: Int, name: String, onPlayAgainClicked: () -> Unit) {
    val encouragementText = when (totalScoreOutOfTen) {
        in (0..3) -> {
            "You can do better, $name!"
        }

        in (4..6) -> {
            "Lets aim higher, $name!"
        }

        else -> {
            "Well played, $name"
        }
    }
    Box {
        Column {
            Row(modifier = Modifier.align(CenterHorizontally)) {
                Text(text = encouragementText, style = Typography.titleLarge)
            }
            Row(modifier = Modifier.padding(top = 32.dp)) {
                Card(
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .align(CenterHorizontally)
                                .padding(16.dp)
                        ) {
                            Image(
                                imageVector = Icons.Filled.Star,
                                contentDescription = "Star",
                                colorFilter = ColorFilter.tint(
                                    orange
                                )
                            )
                            Text(
                                text = "Score : $totalScoreOutOfTen/10",
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }

                        Row(modifier = Modifier.align(CenterHorizontally)) {
                            ScoreCircularProgress(totalScoreOutOfTen / 10F)
                        }
                    }
                }
            }
            Row(
                modifier = Modifier
                    .align(CenterHorizontally)
                    .padding(top = 32.dp)
            ) {
                Button(onClick = {
                    onPlayAgainClicked()
                }) {
                    Text(text = "Take Another Shot")
                }
            }
        }
    }
}

@Composable
fun ScoreCircularProgress(progress: Float) {
    val animatedProgress = animateFloatAsState(
        targetValue = progress,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
    ).value

    Column(horizontalAlignment = CenterHorizontally) {
        CircularProgressIndicator(
            progress = animatedProgress,
            color = orange,
            modifier = Modifier.size(200.dp),
            strokeWidth = 12.dp
        )
        Spacer(Modifier.height(30.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun ScorePreview() {
    QuizTreeTheme {
        Score(totalScoreOutOfTen = 7, name = "Evan", onPlayAgainClicked = {})
    }
}
