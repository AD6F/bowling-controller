package com.ad6f.bowling

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.ad6f.bowling.ui.theme.MyApplicationTheme
import com.google.android.gms.cast.framework.CastContext
import com.google.android.gms.cast.framework.CastSession
import org.json.JSONObject

class GameLoop : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val context = LocalContext.current;

            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column {
                        Pause()
                        LaunchButton(CastContext.getSharedInstance(context).sessionManager.currentCastSession!!)
                    }
                }
            }
        }
    }
}

@Composable
fun Pause() {
    SmallFloatingActionButton(onClick = { }) {
        Icon(Icons.Default.Settings, contentDescription = "Pause")
    }
}

@Composable
fun LaunchButton(
    castSession: CastSession
) {

    Button(onClick = {
        val jsonObject = JSONObject()

        //val rotation = GameLoop.rotationYs.last().y / GameLoop.rotationSensor!!.maximumRange * 100
        //val force = GameLoop.linearAccelerationYs.last().y / GameLoop.linearSensor!!.maximumRange * 100
        //println("force : $force and rotation : $rotation")

        //jsonObject.put("rotation", rotation)
        //jsonObject.put("force", force)

        //castSession.sendMessage(CastInfo.GAME_NAMESPACE, jsonObject.toString())
    }) {
        Text(text = "Launch")
    }

}
