package com.ad6f.bowling

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.ad6f.bowling.ui.theme.MyApplicationTheme

class Game2 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Navbar()
                        PlayerList()
                    }
                }
            }
        }
    }
}

val players = mutableStateListOf<String>()

@Preview
@Composable
fun Navbar() {
    val context = LocalContext.current;

    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
        Button(onClick = { context.startActivity(Intent(context, MainActivity::class.java)) }) {
            Text("back")
        }

        Text(fontSize = 25.sp, text = "Bowling Setup")

        Button(onClick = { /*TODO*/ }) {
            Text("next")
        }
    }
}

/**
 * The component that contains all the components related to players setup.
 */
@Preview
@Composable
fun PlayerList() {
    var isDialogShown by rememberSaveable { mutableStateOf(false) }

    AddPlayerDialog(isDialogShown) {
        isDialogShown = false
    }

    Row {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Players")

                Button(enabled = players.size < 4, onClick = {isDialogShown = true}) {
                    Text("+")
                }
            }

            LazyColumn {
                items (players) {
                        p -> Button(onClick = {
                    players.remove(p)
                }) {
                    Text(p)
                }
                }
            }
        }
    }
}

/**
 * Dialog(Popup) to add a player for the game.
 */
@Composable
fun AddPlayerDialog(
    isShown: Boolean,
    closeDialog: () -> Unit
) {
    // Le rememberSaveable permet de save même si l'écran rotate
    var inputValue by rememberSaveable { mutableStateOf("") }
    var canAdd by rememberSaveable { mutableStateOf(false) }
    var errorInput by rememberSaveable { mutableStateOf("") }

    fun close() {
        inputValue = ""
        canAdd = false
        errorInput = ""
        closeDialog()
    }

    if(isShown) {
        Dialog(
            onDismissRequest = { close() },
        ) {
            Surface(
                color = Color.White,
                modifier = Modifier.padding(20.dp)
            ) {
                Row(Modifier.padding(10.dp)) {
                    Column {
                        if(errorInput.isNotEmpty()) {
                            Text(errorInput)
                        }

                        OutlinedTextField(
                            value = inputValue,
                            onValueChange = {
                                inputValue = it;

                                if(players.contains(it)) {
                                    canAdd = false
                                    errorInput = "Player already exists."
                                }

                                // These if else are there to set if the Add button is enabled or not
                                else if(it.isEmpty() && canAdd) {
                                    errorInput = "Player cannot be empty."
                                    canAdd = false;
                                }

                                else if(it.isNotEmpty() && !canAdd) {
                                    errorInput = ""
                                    canAdd = true;
                                }
                            },
                            label = { Text("Player") }
                        )

                        Row(horizontalArrangement = Arrangement.Center) {
                            Button(onClick = { close() }) {
                                Text("Cancel")
                            }

                            Button(enabled = canAdd, onClick = { players.add(inputValue); close() }) {
                                Text("Add")
                            }
                        }
                    }
                }
            }
        }
    }
}