package com.example.quiztree.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.quiztree.R
import com.example.quiztree.ui.theme.QuizTreeTheme
import com.example.quiztree.ui.theme.Typography
import com.example.quiztree.ui.theme.orange

/**
 * Stateless composable
 */
@Composable
fun QuizQuestion(questionText: String) {
    Card(
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.margin_padding_medium)),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = orange,
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = questionText,
                modifier = Modifier.padding(dimensionResource(id = R.dimen.margin_padding_medium)),
                style = Typography.titleLarge
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun QuizQuestionPreview() {
    QuizTreeTheme {
        QuizQuestion("Who was the first lady president of France?")
    }
}