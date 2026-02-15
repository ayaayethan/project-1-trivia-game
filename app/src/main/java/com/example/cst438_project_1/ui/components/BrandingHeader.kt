package com.example.cst438_project_1.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.cst438_project_1.R

/**
 * Shared branding section (logo + subtitle) used on Login and MainMenu screens.
 * Uses consistent top padding so the logo doesn't jump between views.
 */
@Composable
fun BrandingHeader(
    subtitle: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(top = 64.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.logo),
            contentDescription = "Game Diff logo",
            modifier = Modifier
                .fillMaxWidth(1f)
                .aspectRatio(1f),
            contentScale = ContentScale.Fit
        )
        Column(
            modifier = Modifier.offset(y = (-42).dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            subtitle()
        }
    }
}
