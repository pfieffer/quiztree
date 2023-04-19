package com.example.quiztree.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.quiztree.ui.theme.QuizTreeTheme
import com.example.quiztree.ui.theme.Typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NameInput(onDoneClicked: (name: String) -> Unit) {
    var name by rememberSaveable { mutableStateOf("") }
    Column(modifier = Modifier.padding(10.dp)) {
        Row(modifier = Modifier.align(alignment = Alignment.CenterHorizontally)) {
            Text(text = "Welcome", style = Typography.titleLarge)
        }
        Row(
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .padding(top = 10.dp)
        ) {
            Text(text = "Hi there, What should we call you?")
        }
        Row {
            TextField(
                value = name,
                onValueChange = {
                    name = it
                },
                modifier = Modifier.padding(10.dp)
            )
        }
        Row(
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .padding(top = 10.dp)
        ) {
            IconButton(onClick = {
                onDoneClicked(name)
            }) {
                Icon(imageVector = Icons.Filled.ArrowForward, contentDescription = null)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NameInputPreview() {
    QuizTreeTheme {
        NameInput(onDoneClicked = {})
    }
}