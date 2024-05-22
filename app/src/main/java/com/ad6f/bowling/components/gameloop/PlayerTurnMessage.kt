package com.ad6f.bowling.components.gameloop

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp

@Composable
fun PlayerTurnMessage(player: String?) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        if(player != null) {
            Text("${player}’s turn", fontSize = 20.sp)
        }
    }
}