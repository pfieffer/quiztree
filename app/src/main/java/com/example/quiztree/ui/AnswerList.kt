package com.example.quiztree.ui

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.quiztree.ui.theme.QuizTreeTheme

/**
 * Stateless composable
 */
@Composable
fun AnswerList(answers: List<String>, correctAnsIndex: Int) {
    LazyColumn {
        item {
            answers.forEachIndexed { index, ans ->
                if (index == correctAnsIndex) {
                    AnswerItem(answerText = "$ans ---")
                } else {
                    AnswerItem(answerText = ans)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AnswerListPreview() {
    QuizTreeTheme {
        AnswerList(
            answers = listOf("Franklin D. Roosevelt", "Lady Gaga", "Rihanna", "Anne Frank"),
            correctAnsIndex = 1
        )
    }
}
