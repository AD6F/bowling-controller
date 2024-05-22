package com.ad6f.bowling

import android.content.Context
import android.content.Intent
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ad6f.bowling.components.gameloop.EndGameDialog
import com.ad6f.bowling.components.gameloop.SetupLoadingDialog
import com.ad6f.bowling.components.gameloop.LaunchLoadingDialog
import com.ad6f.bowling.components.gameloop.PauseMenuDialog
import com.ad6f.bowling.services.cast.CastInfo
import com.ad6f.bowling.services.cast.CastPage
import com.ad6f.bowling.services.cast.SessionManagerListenerImpl
import com.ad6f.bowling.services.sensors.SensorManagement
import com.ad6f.bowling.ui.theme.MyApplicationTheme
import com.google.android.gms.cast.framework.CastContext
import com.google.android.gms.cast.framework.CastSession
import java.util.Timer
import kotlin.concurrent.schedule

@Composable
fun LaunchButton() {
    Button(
        onClick = {
            GameLoop.sensorManagement?.start()
            isLaunchPressed = true
        },
        Modifier
            .width(100.dp)
            .height(100.dp),
        enabled = !isLaunchPressed
        ) {
        Icon(Icons.Default.Add, modifier = Modifier.fillMaxSize(), contentDescription = "Launch")
    }
}

@Preview(showBackground = true)
@Composable
fun GameNavbar() {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Bowling Game", fontSize = 25.sp)

        IconButton(onClick = { isPauseMenuVisible = true }) {
            Icon(Icons.Default.Menu, contentDescription = "Pause", modifier = Modifier.fillMaxSize())
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PlayerTurnMessage() {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        if(currentPlayer != null) {
            Text("$currentPlayer’s turn", fontSize = 20.sp)
        }
    }
}
var isPauseMenuVisible by mutableStateOf(false)
var isSetupLoadingVisible by mutableStateOf(true)
var isLaunchingLoadingVisible by mutableStateOf(false)
var isEndGameVisible by mutableStateOf(false)
var isLaunchPressed by mutableStateOf(false)
var currentPlayer by mutableStateOf<String?>(null)

fun navigate(castSession: CastSession?, page: CastPage) {
    castSession?.sendMessage(CastInfo.NAVIGATION_NAMESPACE, "{\"page\" : ${page.ordinal}}")
}

class GameLoop : ComponentActivity() {
    companion object {
        var sensorManagement: SensorManagement? = null

        @JvmStatic
        fun playerReceived(player: String) {
            if(player.isEmpty()) {
                println("END GAME")
                isLaunchingLoadingVisible = false
                isEndGameVisible = true
            }

            else {
               if(isSetupLoadingVisible) {
                   Timer().schedule(1000) {
                        isSetupLoadingVisible = false
                        println("TASKING")
                   }
               }

               if(isLaunchingLoadingVisible) isLaunchingLoadingVisible = false
               currentPlayer = player
               isLaunchPressed = false
            }
        }

        /**
         * Function to reset all the state value that the user give to default.
         */
        @JvmStatic
        fun reset() {
            isPauseMenuVisible = false
            isSetupLoadingVisible = true
            isLaunchingLoadingVisible = false
            isEndGameVisible = false
            isLaunchPressed = false
            currentPlayer = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        SessionManagerListenerImpl.currentActivity = this.localClassName
        SessionManagerListenerImpl.gameLoop = this

        setContent {
            val context = LocalContext.current
            sensorManagement = SensorManagement(
                getSystemService(Context.SENSOR_SERVICE) as SensorManager
            ) {
                isLaunchingLoadingVisible = true
                val castSession = CastContext.getSharedInstance(context).sessionManager.currentCastSession!!

                val json = "{\"rotation\":${sensorManagement?.rotation}, \"force\":${sensorManagement?.force}, \"tilt\":${sensorManagement?.tilt}}"
                castSession.sendMessage(CastInfo.GAME_NAMESPACE, json)

                sensorManagement?.close()
            }

            fun goToMainMenu() {
                startActivity(Intent(this, MainMenu::class.java))
                navigate(CastContext.getSharedInstance(context).sessionManager.currentCastSession, CastPage.MAIN_MENU)
                finish()
            }

            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SetupLoadingDialog(isSetupLoadingVisible)
                    LaunchLoadingDialog(isLaunchingLoadingVisible)
                    PauseMenuDialog(
                        isVisible = isPauseMenuVisible,
                        resumeAction = {isPauseMenuVisible = false},
                        mainMenuAction = {goToMainMenu()}
                    )

                    EndGameDialog(
                        isEndGameVisible,
                        replayAction = {
                            reset()
                            navigate(CastContext.getSharedInstance(context).sessionManager.currentCastSession, CastPage.GAME_LOOP)
                        },
                        mainMenuAction = {goToMainMenu()}
                    )

                    Column {
                        GameNavbar()
                        PlayerTurnMessage()
                    }

                    Row(
                        Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.Bottom) {
                        LaunchButton()
                    }
                }
            }
        }
    }
}