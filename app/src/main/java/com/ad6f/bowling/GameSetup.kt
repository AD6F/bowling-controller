package com.ad6f.bowling

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.sharp.KeyboardArrowDown
import androidx.compose.material.icons.sharp.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ad6f.bowling.components.Navbar
import com.ad6f.bowling.components.gamesetup.AddPlayerDialog
import com.ad6f.bowling.services.cast.CastInfo
import com.ad6f.bowling.services.cast.SessionManagerListenerImpl
import com.ad6f.bowling.ui.theme.BowlingControllerTheme
import com.google.android.gms.cast.framework.CastContext
import org.json.JSONArray
import org.json.JSONObject
import kotlin.math.round

class GameSetup : ComponentActivity() {
    companion object {
        /**
         * Function to reset all the state value that the user give to default.
         */
        @JvmStatic
        fun reset() {
            players.clear()
            roundOptionValue = 4f
            mapOptionValue = mapOptions[0]
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        SessionManagerListenerImpl.currentActivity = this.localClassName
        SessionManagerListenerImpl.gameSetup = this

        setContent {
            BowlingControllerTheme {
                val context = LocalContext.current;

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Navbar(
                            backSending = { startActivity(Intent(context, MainMenu::class.java)) },
                            pageTitle = "Bowling Setup",
                            canGoNext = players.isNotEmpty(),
                            nextSending = {
                                val castSession =
                                    CastContext.getSharedInstance(context).sessionManager.currentCastSession
                                val jsonObject = JSONObject()

                                jsonObject.put("players", JSONArray(players))
                                jsonObject.put("round", round(roundOptionValue))
                                jsonObject.put("map", mapOptions.indexOf(mapOptionValue))
                                castSession?.sendMessage(
                                    CastInfo.SETTING_NAMESPACE,
                                    jsonObject.toString()
                                )
                                GameLoop.reset()
                                context.startActivity(Intent(context, GameLoop::class.java))
                                finish()
                            }
                        )

                        GameOptions()
                    }
                }
            }
        }
    }
}

data class Map(val name: String, val imageId: Int);

val mapOptions = arrayListOf(
    Map("Classic", R.drawable.map_classic),
    Map("New York", R.drawable.map_new_york),
    Map("The Matrix", R.drawable.map_matrix),
    Map("Cold Sea", R.drawable.map_cold_sea),
    Map("Infiltration", R.drawable.map_infiltration),
    Map("Galaxy", R.drawable.map_galaxy),
    Map("Grimace", R.drawable.map_grimace),
    Map("Deltarune", R.drawable.map_deltarune),
    Map("House", R.drawable.map_house),
    Map("School", R.drawable.map_school)
)

const val OPTION_TITLE = 20;

// state to send to the chromecast
val players = mutableStateListOf<String>()
var roundOptionValue by mutableFloatStateOf(4f)
var mapOptionValue by mutableStateOf(mapOptions[0])

/**
 * The component that contain the slider for choosing the round.
 */
@Composable
fun RoundSetup() {
    Column {
        Text(fontSize = OPTION_TITLE.sp, text = "Round(${round(roundOptionValue).toInt()})")
        Slider(
            valueRange = 4f..10f,
            steps = 5,
            value = roundOptionValue,
            onValueChange = { roundOptionValue = it })
    }
}

@Composable
fun MapSetup() {
    var expanded by rememberSaveable { mutableStateOf(false) }

    Column {
        Text(fontSize = OPTION_TITLE.sp, text = "Alley Theme")

        TextField(
            modifier = Modifier
                .clickable { expanded = !expanded }
                .fillMaxWidth(),
            value = mapOptionValue.name,
            readOnly = true,
            enabled = false,
            onValueChange = {},
            colors = TextFieldDefaults.colors()
                .copy(
                    disabledTextColor = MaterialTheme.colorScheme.scrim,
                    disabledTrailingIconColor = MaterialTheme.colorScheme.scrim,
                    disabledIndicatorColor = if (expanded) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                ),
            trailingIcon = {
                Icon(
                    (if (expanded) Icons.Sharp.KeyboardArrowUp else Icons.Sharp.KeyboardArrowDown),
                    "DropDownArrow",
                )
            },
        )

        Image(
            painter = painterResource(mapOptionValue.imageId),
            contentDescription = "Not enough.",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp)
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .fillMaxWidth(0.75f)
        ) {
            mapOptions.forEach {
                DropdownMenuItem(
                    text = { Text(it.name) },
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

    AddPlayerDialog(isDialogShown, setIsVisible = { isDialogShown = it }, players = players)

    Row {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(fontSize = OPTION_TITLE.sp, text = "Players")

                IconButton(enabled = players.size < 4, onClick = { isDialogShown = true }) {
                    Icon(
                        Icons.Filled.Add,
                        contentDescription = "Add player",
                        modifier = Modifier.size(35.dp)
                    )
                }
            }

            if (players.size == 0) {
                Column {
                    Text(text = "No player found")
                }
            }

            Column {
                players.forEach {
                    Button(onClick = { players.remove(it) }) {
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
    val scrollState = rememberScrollState()

    Column(
        verticalArrangement = Arrangement.spacedBy(30.dp),
        modifier = Modifier.verticalScroll(scrollState)
    ) {
        RoundSetup()
        MapSetup()
        PlayerSetup()
    }
}
