package com.ad6f.bowling.components.gameloop

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ad6f.bowling.R
import com.ad6f.bowling.components.BowlingLoadingDialog
import com.ad6f.bowling.components.GenericDialog

@Composable
fun SetupLoadingDialog(isVisible: Boolean) {
    BowlingLoadingDialog(
        isVisible,
        message = "Setting up all keels in Chromecast...",
        imageId = R.drawable.bowling_setup,
        imageAlt = "Setup bowling"
    )
}

@Composable
fun LaunchLoadingDialog(isVisible: Boolean) {
    BowlingLoadingDialog(
        isVisible,
        message = "Mission Started! Purpose : no keels left.",
        imageId = R.drawable.mario_launch,
        imageAlt = "Mario launching bowling ball"
    )
}

@Composable
fun MenuButton(label: String, onClick: () -> Unit, enabled: Boolean = true) {
    Button(onClick, modifier = Modifier.fillMaxWidth(), enabled) {
        Text(label)
    }
}

@Composable
fun PauseMenuDialog(isVisible: Boolean) {
    GenericDialog(isVisible) {
        Column {
            MenuButton("Resume", {})
            MenuButton("MainMenu", {})
            MenuButton("coming soon..", {}, false)
        }
    }
}

@Composable
fun EndGameDialog(isVisible: Boolean) {
    GenericDialog(
        isVisible,
        title = {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Text("End Game")
            }
        }
    ) {
        Column {
            MenuButton("Replay", {})
            MenuButton("MainMenu", {})
        }
    }
}