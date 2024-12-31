package com.ad6f.bowling.services.sensors

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import java.util.Collections

class SensorCalculator(
    private val sensor: Sensor,
    private val coordinate: Coordinate,
    private val sendData : (() -> Unit)? = null
) : SensorEventListener {
    private var percentsList: MutableList<Float> = ArrayList()
    private var isListening = false

    private val max: Float = sensor.maximumRange
    private val min: Float = max * -1

    fun getBestPercent(): Float {
        return Collections.max(percentsList)
    }

    fun getLastPercent(): Float {
        return percentsList.last()
    }

    fun registerListener(sensorManager: SensorManager) {
        sensorManager.registerListener(this, sensor, 1)
        isListening = true
    }

    fun unregisterListener(sensorManager: SensorManager) {
        sensorManager.unregisterListener(this)
        percentsList.clear()
        isListening = false
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (isListening) {
            val currentPercent = ((event.values[coordinate.ordinal] - min) * 100) / (max - min)

            if (sensor.type == Sensor.TYPE_LINEAR_ACCELERATION && percentsList.isNotEmpty() && (getLastPercent() - currentPercent >= 10)) {
                sendData?.invoke()
            }

            percentsList.add(currentPercent)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
}