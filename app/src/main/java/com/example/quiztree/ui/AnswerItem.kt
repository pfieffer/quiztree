package com.example.quiztree.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.quiztree.ui.theme.QuizTreeTheme
import com.example.quiztree.ui.theme.Typography

/**
 * Stateless composable
 */
@Composable
fun AnswerItem(answerText: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.Yellow)
            .padding(10.dp)
            .clickable(
                enabled = true,
                onClick = {
                    onClick()
                }
            )
    ) {
        QuizTreeTheme {
            Text(text = answerText, style = Typography.bodyLarge)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AnswerItemPreview() {
    QuizTreeTheme {
        AnswerItem("Franklin D. Roosevelt", onClick = {})
    }
}
