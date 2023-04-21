package com.example.quiztree.ui

import android.os.SystemClock
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.quiztree.AppConstants
import com.example.quiztree.R
import com.example.quiztree.ui.theme.QuizTreeTheme
import com.example.quiztree.ui.theme.Typography
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

@Composable
fun Timer(onTimerComplete: () -> Unit) {
    val time = timer(initialMillis = AppConstants.TOTAL_TIME_ALLOWED_MILLIS)
    val seconds = time.value / 1000L
    val minRemaining = seconds / 60
    val secondsRemaining = seconds % 60
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = String.format("%02d", minRemaining),
            modifier = Modifier.padding(dimensionResource(id = R.dimen.margin_padding_medium)),
            color = if (seconds <= AppConstants.HURRY_UP_TIME_SECONDS) Color.Red else Color.Black,
            style = Typography.titleLarge
        )
        Text(
            text = ":",
            modifier = Modifier.padding(dimensionResource(id = R.dimen.margin_padding_medium)),
            color = if (seconds <= AppConstants.HURRY_UP_TIME_SECONDS) Color.Red else Color.Black,
            style = Typography.titleLarge
        )
        Text(
            text = String.format("%02d", secondsRemaining),
            modifier = Modifier.padding(dimensionResource(id = R.dimen.margin_padding_medium)),
            color = if (seconds <= AppConstants.HURRY_UP_TIME_SECONDS) Color.Red else Color.Black,
            style = Typography.titleLarge
        )
    }
    if (time.value == 0L) {
        onTimerComplete()
    }
}

@Composable
fun timer(
    initialMillis: Long,
    step: Long = 1000
): MutableState<Long> {
    val timeLeft = remember { mutableStateOf(initialMillis) }
    LaunchedEffect(initialMillis, step) {
        val startTime = SystemClock.uptimeMillis()
        while (isActive && timeLeft.value > 0) {
            // how much time actually passed
            val duration = (SystemClock.uptimeMillis() - startTime).coerceAtLeast(0)
            timeLeft.value = (initialMillis - duration).coerceAtLeast(0)
            delay(step.coerceAtMost(timeLeft.value))
        }
    }
    return timeLeft
}

@Preview(showBackground = true)
@Composable
fun TimerPreview() {
    QuizTreeTheme {
        Timer(onTimerComplete = {})
    }
}