package com.example.quiztree.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.quiztree.ui.theme.QuizTreeTheme
import com.example.quiztree.ui.theme.Typography
import com.example.quiztree.ui.theme.orange

/**
 * Stateless composable
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnswerItem(answerText: String, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor =  orange,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        onClick = onClick
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
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
