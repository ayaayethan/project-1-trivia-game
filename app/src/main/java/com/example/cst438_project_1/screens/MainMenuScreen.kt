package com.example.cst438_project_1.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.cst438_project_1.ui.components.BrandingHeader
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
                        Text(text = "Select which game you think is rated higher! " +
                                "If your guess is correct, you will move on to the next round and receive a point.\n" +
                                "\nTry to pump your streak as high as possible!")
                    },
                    confirmButton = {
                        TextButton(onClick = { showHelpDialog = false }) {
                            Text("Exit")
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
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    BrandingHeader(
                        subtitle = {
                            Text(
                                text = "THE ULTIMATE RATING CHALLENGE",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.secondary,
                                textAlign = TextAlign.Center
                            )
                        }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    GameButton(text = "Play Now", onClick = onPlayClick)
                }

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