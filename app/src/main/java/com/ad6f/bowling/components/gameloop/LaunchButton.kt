package com.ad6f.bowling.components.gameloop

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LaunchButton(enabled: Boolean, onClick : () -> Unit) {
    Button(
        onClick,
        Modifier
            .width(100.dp)
            .height(100.dp),
        enabled
    ) {
        Icon(Icons.Filled.KeyboardArrowUp, modifier = Modifier.fillMaxSize(), contentDescription = "Launch")
    }
}