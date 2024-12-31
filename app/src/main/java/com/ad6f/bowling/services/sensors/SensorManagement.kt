package com.ad6f.bowling.services.sensors

import android.hardware.Sensor
import android.hardware.SensorManager

class SensorManagement(private val sensorManager: SensorManager, sendData: () -> Unit) {
    private var isRegister = false

    private val linearCalculator = SensorCalculator(
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)!!,
        coordinate = Coordinate.Y,
        sendData
    )

    private val rotationCalculator = SensorCalculator(
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)!!,
        coordinate = Coordinate.Y
    )

    private val gravityCalculator = SensorCalculator(
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY)!!,
        coordinate = Coordinate.X
    )

    fun start() {
        if (isRegister) return

        linearCalculator.registerListener(sensorManager)
        rotationCalculator.registerListener(sensorManager)
        gravityCalculator.registerListener(sensorManager)
        isRegister = true
    }

    fun close() {
        if (!isRegister) return

        linearCalculator.unregisterListener(sensorManager)
        rotationCalculator.unregisterListener(sensorManager)
        gravityCalculator.unregisterListener(sensorManager)
        isRegister = false
    }

    fun getJsonData(): String {
        return "{\"rotation\":${rotationCalculator.getBestPercent()}, \"force\":${linearCalculator.getBestPercent()}, \"tilt\":${gravityCalculator.getLastPercent()}}"
    }
}
