package com.ad6f.bowling.components.gamesetup

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.ad6f.bowling.components.GenericDialog
import com.ad6f.bowling.ui.theme.DarkColorScheme
import com.ad6f.bowling.ui.theme.LightColorScheme
import com.ad6f.bowling.ui.theme.BowlingControllerTheme

enum class ErrorType(val value: String) {
    NONE(""),
    UNIQUE("Player already exists."),
    EMPTY("Player cannot be empty.")
}

fun getPlayerError(value: String, players: List<String>): ErrorType {
    return (
        if (value.isEmpty()) ErrorType.EMPTY
        else if (players.contains(value)) ErrorType.UNIQUE
        else ErrorType.NONE
    )
}

/**
 * Dialog(Popup) to add a player for the game.
 */
@Composable
fun AddPlayerDialog(
    isVisible: Boolean,
    setIsVisible: (isVisible: Boolean) -> Unit,
    players: SnapshotStateList<String>
) {
    // Le rememberSaveable permet de save même si l'écran rotate
    var inputValue by rememberSaveable { mutableStateOf("") }

    // To disable the button when we first open the dialog because it is empty
    var isStart by rememberSaveable { mutableStateOf(true) }
    var errorType by rememberSaveable { mutableStateOf(ErrorType.NONE) }

    fun close() {
        inputValue = ""
        isStart = true
        errorType = ErrorType.NONE
        setIsVisible(false)
    }

    GenericDialog(
        isVisible,
        title = {Text("Bowling")},
        closeAction = { close() },
        cancelButton = {
            Button(onClick = { close() }) {
                Text("Cancel")
            }
        },
        okButton = {
            Button(
                enabled = !isStart && errorType == ErrorType.NONE,
                onClick = { players.add(inputValue); close() }
            )
            {
                Text("Add")
            }
        }
    ) {
        Column {
            OutlinedTextField(
                label = { Text("Player") },
                value = inputValue,
                singleLine = true,
                isError = errorType != ErrorType.NONE,
                onValueChange = {
                    if (isStart) isStart = false

                    if (it.length <= 10) {
                        inputValue = it;

                        val tmpError = getPlayerError(inputValue, players)
                        if (tmpError != errorType) errorType = tmpError
                    }
                }
            )

            if (errorType != ErrorType.NONE) {
                Text(
                    text = errorType.value,
                    color = (if (isSystemInDarkTheme()) DarkColorScheme.error else LightColorScheme.error),
                    fontSize = 14.sp
                )
            }
        }
    }
}
