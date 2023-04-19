package com.example.quiztree.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.quiztree.ui.theme.QuizTreeTheme
import com.example.quiztree.ui.theme.Typography

/**
 * Stateless composable
 */
@Composable
fun QuizQuestion(questionText: String) {
    Text(
        text = questionText,
        modifier = Modifier.padding(10.dp),
        style = Typography.titleLarge
    )
}


@Preview(showBackground = true)
@Composable
fun QuizQuestionPreview() {
    QuizTreeTheme {
        QuizQuestion("Who was the first lady president of France?")
    }
}