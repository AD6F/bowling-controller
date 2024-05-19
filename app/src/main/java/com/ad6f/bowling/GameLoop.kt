package com.ad6f.bowling

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.ad6f.bowling.services.sensors.SensorCalculator
import com.ad6f.bowling.services.sensors.Coordinate
import com.ad6f.bowling.ui.theme.MyApplicationTheme
import com.google.android.gms.cast.framework.CastContext
import com.google.android.gms.cast.framework.CastSession
import org.json.JSONObject

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
            sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager;

            linearSensorCal = SensorCalculator(
                sensorManager,
                sensorManager?.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION),
                Coordinate.Y
            ) {
                //println("LINEAR : ${linearSensor?.percentsList}")
                //println("ROTATION : ${rotationSensor?.percentsList}")
                println("BEST_LINEAR Y : ${linearSensorCal?.bestPercent}")
                println("BEST_ROTATION Y : ${rotationSensorCal?.bestPercent}")
                println("LAST_GRAVITY X : ${gravitySensorCal?.lastPercent}")
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
                    Column {
                        Pause()
                        LaunchButton(CastContext.getSharedInstance(LocalContext.current).sessionManager.currentCastSession!!)
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
        GameLoop.rotationSensorCal?.start()
        GameLoop.gravitySensorCal?.start()
        GameLoop.linearSensorCal?.start()

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
