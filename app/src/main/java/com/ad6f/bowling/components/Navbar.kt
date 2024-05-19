package com.ad6f.bowling.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp

@Composable
fun Navbar(
    backSending: () -> Unit,
    pageTitle: String,
    canGoNext: Boolean,
    nextSending: () -> Unit,
) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
        Button(backSending) {
            Text("back")
        }

        Text(fontSize = 25.sp, text = pageTitle)

        Button(enabled = canGoNext, onClick = nextSending) {
            Text("next")
        }
    }
}