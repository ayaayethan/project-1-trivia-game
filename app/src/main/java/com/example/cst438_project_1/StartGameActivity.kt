package com.example.cst438_project_1

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.cst438_project_1.nav.AppNavigation
import com.example.cst438_project_1.ui.theme.Cst438project1Theme
import com.example.cst438_project_1.viewmodels.GamesViewModel
import kotlin.getValue

class StartGameActivity : ComponentActivity() {

    private val viewModel: GamesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val userId = intent.getLongExtra(EXTRA_USER_ID, -1)
        if (userId == 0L) {
            finish()
            return
        }


        setContent {
            Cst438project1Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(onLogout = { onLogout() })
                }
            }
        }
    }

    private fun onLogout() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
