package com.example.quiztree.ui

import android.text.Html
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.quiztree.R
import com.example.quiztree.ui.theme.QuizTreeTheme
import com.example.quiztree.ui.theme.Typography
import com.example.quiztree.ui.theme.orange

/**
 * Stateless composable
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnswerItem(htmlEncodedAnswerText: String, onClick: () -> Unit) {
    val answerText =
        Html.fromHtml(htmlEncodedAnswerText, Html.FROM_HTML_MODE_LEGACY).toString()
    Card(
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.margin_padding_medium)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = dimensionResource(id = R.dimen.margin_padding_medium)),
        onClick = onClick
    ) {
        Column(modifier = Modifier.padding(dimensionResource(id = R.dimen.margin_padding_large))) {
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
