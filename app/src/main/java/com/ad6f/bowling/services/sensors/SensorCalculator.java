package com.ad6f.bowling.services.sensors;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.ToString;

@ToString
public class SensorCalculator {
    public SensorEventListener sensorEventListener = null;

    public List<Float> percentsList = new ArrayList<>();

    private boolean isListening = false;

    public float getBestPercent() {
        return Collections.max(percentsList);
    }

    public void start() {
        if (!isListening) {
            isListening = true;
        }
    }

    public void close() {
        if (isListening) {
            percentsList.clear();
            isListening = false;
        }
    }

    public SensorCalculator(SensorManager sensorManager, Sensor sensor, Coordinate coordinate, SensorAction sensorAction) {
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (isListening) {
                    System.out.println("LISTENING");
                    var currentCoordinate = event.values[coordinate.ordinal()] / sensor.getMaximumRange() * 100;

                    if (sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION && !percentsList.isEmpty() && percentsList.get(percentsList.size() - 1) - currentCoordinate >= 10) {
                        System.out.println("Stopped adding into list");
                        sensorAction.sendData();
                    }

                    percentsList.add(currentCoordinate);
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {}
        };

        sensorManager.registerListener(sensorEventListener, sensor, 1);
    }
}
