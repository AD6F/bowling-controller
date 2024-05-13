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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
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
import com.ad6f.bowling.cast.CastInfo
import com.ad6f.bowling.ui.theme.MyApplicationTheme
import com.google.android.gms.cast.framework.CastContext
import com.google.android.gms.cast.framework.CastSession
import org.json.JSONArray
import org.json.JSONObject
import kotlin.math.round

class GameSetup : ComponentActivity() {
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
                        GameOptions()
                    }
                }
            }
        }
    }
}
val mapOptions = arrayListOf("New York", "Undercover", "Cold Sea", "Infiltration")
const val OPTION_TITLE = 20;

// state to send to the chromecast
val players = mutableStateListOf<String>()
var roundOptionValue by mutableFloatStateOf(1f)
var mapOptionValue by mutableStateOf(mapOptions[0])


@Preview
@Composable
fun Navbar() {
    val context = LocalContext.current;

    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
        Button(onClick = { context.startActivity(Intent(context, MainActivity::class.java)) }) {
            Text("back")
        }

        Text(fontSize = 25.sp, text = "Bowling Setup")

        Button(enabled = players.isNotEmpty(), onClick = {
            val castSession = CastContext.getSharedInstance(context).sessionManager.currentCastSession;
            val jsonObject = JSONObject();

            jsonObject.put("players", JSONArray(players));
            jsonObject.put("round", round(roundOptionValue));
            jsonObject.put("map", mapOptions.indexOf(mapOptionValue));
            castSession?.sendMessage(CastInfo.SETTING_NAMESPACE, jsonObject.toString());
            context.startActivity(Intent(context, GameLoop::class.java));
        }) {
            Text("next")
        }
    }
}

/**
 * The component that contain the slider for chosing the round.
 */
@Composable
fun Round() {
    Text(fontSize = OPTION_TITLE.sp, text = String.format("Round(%.0f)", round(roundOptionValue)))
    Slider(valueRange = 1f..10f, steps = 8, value = roundOptionValue, onValueChange = {roundOptionValue = it})
}

@Composable
fun MapSelector() {
    var expanded by rememberSaveable { mutableStateOf(false) }

    Column {
        Text(fontSize = OPTION_TITLE.sp, text = "Map")

        Button(onClick = { expanded = !expanded}) {
            Text(mapOptionValue)
        }

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            mapOptions.forEach {
                DropdownMenuItem(
                    text = { Text(it) },
                    onClick = { mapOptionValue = it; expanded = false; }
                )
            }
        }
    }

}
/**
 * The component that contains all the components related to players setup.
 */
@Composable
fun PlayerList() {
    var isDialogShown by rememberSaveable { mutableStateOf(false) }

    AddPlayerDialog(isDialogShown) {
        isDialogShown = false
    }

    Row {
        Column {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                Text(fontSize = OPTION_TITLE.sp, text = "Players")

                Button(enabled = players.size < 4, onClick = {isDialogShown = true}) {
                    Text("+")
                }
            }

            if(players.size == 0) {
                Column {
                    Text(text = "No player found")
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

                        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
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

@Preview
@Composable
fun GameOptions() {
    Column {
        Round()
        MapSelector()
        PlayerList()
    }
}