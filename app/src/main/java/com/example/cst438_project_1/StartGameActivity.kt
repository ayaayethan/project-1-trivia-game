package com.example.cst438_project_1

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.cst438_project_1.nav.AppNavigation
import com.example.cst438_project_1.ui.theme.Cst438project1Theme

class StartGameActivity : ComponentActivity() {

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
                    AppNavigation(userId = userId, onLogout = { onLogout() })
                }
            }
        }
    }

    private fun onLogout() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
    companion object {
        const val EXTRA_USER_ID = "extra_user_id"
    }
}
