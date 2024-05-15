package com.ad6f.bowling.components.gamesetup

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.ad6f.bowling.ui.theme.MyApplicationTheme

enum class ErrorType(val value: String) {
    NONE(""),
    UNIQUE("Player already exists."),
    EMPTY("Player cannot be empty.")
}

fun getPlayerError(value: String, players: List<String>): ErrorType {
    return if (value.isEmpty()) {
        ErrorType.EMPTY
    } else if (players.contains(value)) {
        ErrorType.UNIQUE
    } else {
        ErrorType.NONE
    }
}

/**
 * Dialog(Popup) to add a player for the game.
 */
@Composable
fun AddPlayerDialog(
    isShown: Boolean,
    setIsShown: (isShown: Boolean) -> Unit,
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
        setIsShown(false)
    }

    if (isShown) {
        Dialog(
            onDismissRequest = { close() },
        ) {
            Surface(
                modifier = Modifier.padding(20.dp)
            ) {
                Row(Modifier.padding(10.dp)) {
                    Column {
                        OutlinedTextField(
                            value = inputValue,
                            isError = errorType != ErrorType.NONE,
                            singleLine = true,
                            onValueChange = {
                                if (isStart) isStart = false

                                if (it.length <= 10) {
                                    inputValue = it;

                                    val tmpError = getPlayerError(inputValue, players);
                                    if (tmpError != errorType) {
                                        errorType = tmpError
                                    }
                                }
                            },
                            label = {
                                Text("Player")
                            }
                        )

                        if (errorType != ErrorType.NONE) {
                            Text(text = errorType.value, color = (if(isSystemInDarkTheme()) Color(0xffcfa7b1) else Color(0xffb3261e)), fontSize = 14.sp)
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Button(onClick = { close() }) {
                                Text("Cancel")
                            }

                            Button(
                                enabled = !isStart && errorType == ErrorType.NONE,
                                onClick = { players.add(inputValue); close() }) {
                                Text("Add")
                            }
                        }
                    }
                }
            }
        }
    }
}
