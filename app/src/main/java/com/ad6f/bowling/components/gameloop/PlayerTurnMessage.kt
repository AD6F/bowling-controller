package com.ad6f.bowling.components.gameloop

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp

@Composable
fun PlayerTurnMessage(player: String?) {
    Text(if(player != null) "${player}’s turn" else "", fontSize = 20.sp)
}