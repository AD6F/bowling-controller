package com.ad6f.bowling.components.gameloop

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ad6f.bowling.R
import com.ad6f.bowling.components.BowlingLoadingDialog
import com.ad6f.bowling.components.GenericDialog

/**
 * Small reused Component Section
 */
@Composable
fun MenuButton(label: String, onClick: () -> Unit, enabled: Boolean = true) {
    Button(onClick, modifier = Modifier.fillMaxWidth(), enabled) {
        Text(label)
    }
}

@Composable
fun CenteredTitle(title: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Text(title)
    }
}


/**
 * Dialog Section
 */
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
        message = "Waiting for the ball to come back because Mario stole it!",
        imageId = R.drawable.mario_launch,
        imageAlt = "Mario launching bowling ball"
    )
}

@Composable
fun PauseMenuDialog(isVisible: Boolean, resumeAction: () -> Unit, mainMenuAction: () -> Unit) {
    GenericDialog(isVisible, title = {CenteredTitle("Pause Menu")}) {
        Column {
            MenuButton("Resume", resumeAction)
            MenuButton("Main Menu", mainMenuAction)
            MenuButton("Coming Soon...", {}, false)
        }
    }
}

@Composable
fun EndGameDialog(isVisible: Boolean, replayAction: () -> Unit, mainMenuAction: () -> Unit) {
    GenericDialog(
        isVisible,
        title = {
            CenteredTitle("End Game")
        }
    ) {
        Column {
            MenuButton("Replay", replayAction)
            MenuButton("Main Menu", mainMenuAction)
        }
    }
}