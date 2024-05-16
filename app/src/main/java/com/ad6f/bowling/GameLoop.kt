package com.ad6f.bowling

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.ad6f.bowling.cast.CastInfo
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
    SmallFloatingActionButton(onClick = { /*TODO*/ }) {
        Icon(Icons.Default.Settings, contentDescription = "Pause")
    }
}

@Composable
fun LaunchButton(
    castSession: CastSession
) {
    Button(onClick = {
        val jsonObject = JSONObject()
        jsonObject.put("rotation", 35.75f)
        jsonObject.put("force", 30.99f)

        castSession.sendMessage(CastInfo.GAME_NAMESPACE, jsonObject.toString())
    }) {
        Text(text = "Launch")
    }

}
