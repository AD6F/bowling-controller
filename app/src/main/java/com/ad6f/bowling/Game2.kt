package com.ad6f.bowling

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
                    PlayerList()
                    AddPlayerDialog()
                }
            }
        }
    }
}

val players = mutableStateListOf<String>("121", "12")

@Preview
@Composable
fun PlayerList() {
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

@Preview
@Composable
fun AddPlayerDialog() {
    // Le rememberSaveable permet de save même si l'écran rotate
    var isShown by rememberSaveable { mutableStateOf(true) }
    var inputValue by rememberSaveable { mutableStateOf("") }
    var canAdd by rememberSaveable { mutableStateOf(false) }

    if(isShown) {
        Dialog(
            onDismissRequest = { isShown = false },
        ) {
            Surface(
                color = Color.White,
                modifier = Modifier.padding(20.dp)
            ) {
                Row(Modifier.padding(10.dp)) {
                    Column {
                        OutlinedTextField(
                            value = inputValue,
                            onValueChange = {
                                inputValue = it;

                                // These if else are there to set if the Add button is enabled or not
                                if(it.isEmpty() && canAdd) {
                                    canAdd = false;
                                } else if(it.isNotEmpty() && !canAdd) {
                                    canAdd = true;
                                }
                            },
                            label = { Text("PlayerName") }
                        )

                        Row {
                            Button(onClick = { isShown = false }) {
                                Text("Cancel")
                            }

                            Button(enabled = canAdd, onClick = { players.add(inputValue); isShown = false }) {
                                Text("Add")
                            }
                        }
                    }
                }
            }
        }
    }
}