package com.example.cst438_project_1.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cst438_project_1.ui.theme.Cst438project1Theme

@Composable
fun MainMenuScreen(
    modifier: Modifier = Modifier,
    onPlayClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {}
) {
    val dialogEnabled = remember { mutableStateOf(false) }
    if (dialogEnabled.value) {
        AlertDialog(
            onDismissRequest = {dialogEnabled.value = false},
            title = { Text(text = "Welcome to Game Diff!")},
            text = {
                Text(text = "Select which game you think is rated higher! " +
                        "If your guess is correct, you will move on to the next round and receive a point.\n" +
                        "\nTry to pump your streak as high as possible!")
            },
            confirmButton = {
                Button(onClick = {dialogEnabled.value = false})
                {Text(text = "Exit")}
            }
        )
    }
    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Game Diff",
                style = MaterialTheme.typography.headlineLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "pick which game you think is rated higher",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = onPlayClick) {
                Text(text = "Play")
            }
        }
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = onLogoutClick) {
                Text(text = "Logout")
            }
            Button(onClick = {dialogEnabled.value = true}) {
                Text(text = "Help")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainMenuScreenPreview() {
    Cst438project1Theme {
        MainMenuScreen()
    }
}