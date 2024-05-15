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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ad6f.bowling.cast.CastInfo
import com.ad6f.bowling.components.gamesetup.AddPlayerDialog
import com.ad6f.bowling.components.Navbar
import com.ad6f.bowling.ui.theme.MyApplicationTheme
import com.google.android.gms.cast.framework.CastContext
import org.json.JSONArray
import org.json.JSONObject
import kotlin.math.round

class GameSetup : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                val context = LocalContext.current;

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {

                        Navbar(
                            backSending = { startActivity(Intent(context, MainActivity::class.java)) },
                            pageTitle = "Bowling Setup",
                            canGoNext = players.isNotEmpty(),
                            nextSending = {
                                val castSession = CastContext.getSharedInstance(context).sessionManager.currentCastSession
                                val jsonObject = JSONObject()

                                jsonObject.put("players", JSONArray(players))
                                jsonObject.put("round", round(roundOptionValue))
                                jsonObject.put("map", mapOptions.indexOf(mapOptionValue))
                                castSession?.sendMessage(CastInfo.SETTING_NAMESPACE, jsonObject.toString())
                                context.startActivity(Intent(context, GameLoop::class.java))
                            }
                        )

                        GameOptions()
                    }
                }
            }
        }
    }
}
val mapOptions = arrayListOf("Classic", "New York", "Undercover", "Cold Sea", "Infiltration")
const val OPTION_TITLE = 20;

// state to send to the chromecast
val players = mutableStateListOf<String>()
var roundOptionValue by mutableFloatStateOf(4f)
var mapOptionValue by mutableStateOf(mapOptions[0])

/**
 * The component that contain the slider for chosing the round.
 */
@Composable
fun RoundSetup() {
    Text(fontSize = OPTION_TITLE.sp, text = String.format("Round(%.0f)", round(roundOptionValue)))
    Slider(valueRange = 4f..10f, steps = 5, value = roundOptionValue, onValueChange = {roundOptionValue = it})
}

@Composable
fun MapSetup() {
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
fun PlayerSetup() {
    var isDialogShown by rememberSaveable { mutableStateOf(false) }

    AddPlayerDialog(isDialogShown, setIsShown = {isDialogShown = it}, players = players)

    Row {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
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
                    Button(onClick = {
                        players.remove(it)
                    }) {
                        Text(it)
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
        RoundSetup()
        MapSetup()
        PlayerSetup()
    }
}