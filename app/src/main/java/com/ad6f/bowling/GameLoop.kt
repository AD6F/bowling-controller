package com.ad6f.bowling

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.ad6f.bowling.services.sensors.SensorCalculator
import com.ad6f.bowling.services.sensors.Coordinate
import com.ad6f.bowling.ui.theme.MyApplicationTheme
import com.google.android.gms.cast.framework.CastContext
import org.json.JSONObject

@Composable
fun LaunchButton() {
    Button(onClick = {
        val jsonObject = JSONObject()
        GameLoop.rotationSensorCal?.start()
        GameLoop.gravitySensorCal?.start()
        GameLoop.linearSensorCal?.start()
    }) {
        Icon(Icons.Default.Add, contentDescription = "Launch")
    }
}

@Preview(showBackground = true)
@Composable
fun GameNavbar() {
    Row(
        Modifier.fillMaxWidth().padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Bowling Game", fontSize = 25.sp)

        IconButton(onClick = { isPauseMenuVisible = true }) {
            Icon(Icons.Default.Menu, contentDescription = "Pause", modifier = Modifier.fillMaxSize())
        }
    }
}

var isPauseMenuVisible by mutableStateOf(false);

class GameLoop : ComponentActivity() {
    companion object {
        var sensorManager: SensorManager? = null
        var linearSensorCal: SensorCalculator? = null
        var rotationSensorCal: SensorCalculator? = null
        var gravitySensorCal: SensorCalculator? = null
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager?.unregisterListener(rotationSensorCal?.sensorEventListener)
        sensorManager?.unregisterListener(gravitySensorCal?.sensorEventListener)
        sensorManager?.unregisterListener(linearSensorCal?.sensorEventListener)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val context = LocalContext.current
            sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager;

            linearSensorCal = SensorCalculator(
                sensorManager,
                sensorManager?.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION),
                Coordinate.Y
            ) {
                val castSession = CastContext.getSharedInstance(context).sessionManager.currentCastSession!!
                val jsonObject = JSONObject()

                jsonObject.put("rotation", linearSensorCal?.bestPercent)
                jsonObject.put("force", rotationSensorCal?.bestPercent)
                jsonObject.put("tilt", gravitySensorCal?.lastPercent)

                println(jsonObject.toString())
                castSession.sendMessage(CastInfo.GAME_NAMESPACE, jsonObject.toString())
                rotationSensorCal?.close()
                gravitySensorCal?.close()
                linearSensorCal?.close()
            }

            rotationSensorCal = SensorCalculator(
                sensorManager,
                sensorManager?.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR),
                Coordinate.Y,
                null
            )

            gravitySensorCal = SensorCalculator(
                sensorManager,
                sensorManager?.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR),
                Coordinate.X,
                null
            )

            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SetupLoadingDialog(isVisible = false)
                    LaunchLoadingDialog(isVisible = false)
                    PauseMenuDialog(
                        isVisible = isPauseMenuVisible,
                        resumeAction = {isPauseMenuVisible = false},
                        mainMenuAction = {
                            startActivity(Intent(this, MainMenu::class.java))
                            isPauseMenuVisible = false
                        }
                    )
                    EndGameDialog(isVisible = false)

                    Column {
                        GameNavbar()
                        LaunchButton()
                    }
                }
            }
        }
    }
}