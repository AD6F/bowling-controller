package com.ad6f.bowling.components.gameloop

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.ad6f.bowling.R

@Preview
@Composable
fun WaitingForGameSetup() {
    AlertDialog(
        onDismissRequest = {},
        confirmButton = {},
        title = { Text("Bowling")},
        text = {
            Column {
                Text("Setting up all keels in Chromecast...")
                Image(painter = painterResource(id = R.drawable.bowling_setup), contentDescription = "FAS")
            }
        }

    )
}