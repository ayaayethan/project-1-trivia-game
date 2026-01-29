package com.example.cst438_project_1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cst438_project_1.ui.theme.Cst438project1Theme

class StartGameActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StartGame()
        }
    }
}


@Composable
fun StartGame() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Text(
            text = "Highest Streak: ",
            fontSize = 16.sp,
            modifier = Modifier.align(Alignment.TopCenter)
        )

        // Center: Title + subtitle
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "GAME DIFF",
                fontSize = 65.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "pick which game you think is rated higher",
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(100.dp))
            Button(onClick = {},
//                shape = RectangleShape
            ) {
                Text(
                    "START",
                    fontSize = 50.sp
                )
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun Preview() {
//    Title()
    StartGame()
}
