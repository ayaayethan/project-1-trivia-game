package com.example.cst438_project_1.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cst438_project_1.ui.components.GameButton
import com.example.cst438_project_1.ui.theme.gametheme // Use the exact name from gametheme.kt

@Composable
fun MainMenuScreen(
    modifier: Modifier = Modifier,
    onPlayClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {}
) {
    var showHelpDialog by remember { mutableStateOf(false) }

    // Wrap the entire screen in the GameTheme to apply background and primary colors
    gametheme {
        Surface(
            modifier = modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            if (showHelpDialog) {
                AlertDialog(
                    onDismissRequest = { showHelpDialog = false },
                    title = { Text(text = "HOW TO PLAY", fontWeight = FontWeight.Bold) },
                    text = {
                        Text(
                            text = "Select which game you think is rated higher! " +
                                    "Correct guesses increase your streak. See how high you can go!",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    confirmButton = {
                        TextButton(onClick = { showHelpDialog = false }) {
                            Text("GOT IT")
                        }
                    }
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Branding Header
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(top = 64.dp)
                ) {
                    Text(
                        text = "GAME DIFF",
                        style = MaterialTheme.typography.displayMedium.copy(
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.primary,
                            letterSpacing = 4.sp
                        )
                    )
                    Text(
                        text = "THE ULTIMATE RATING CHALLENGE",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.secondary,
                        textAlign = TextAlign.Center
                    )
                }

                // Primary Action
                GameButton(
                    text = "Play Now",
                    onClick = onPlayClick,
                    modifier = Modifier.padding(bottom = 48.dp)
                )

                // Secondary Actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedButton(
                        onClick = onLogoutClick,
                        modifier = Modifier.weight(1f),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
                    ) {
                        Text("LOGOUT")
                    }

                    Button(
                        onClick = { showHelpDialog = true },
                        modifier = Modifier.weight(1f),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Text("HELP")
                    }
                }
            }
        }
    }
}