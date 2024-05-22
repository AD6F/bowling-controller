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
    private final float MAX_VALUE;
    private final float MIN_VALUE;
    public SensorEventListener sensorEventListener = null;
    public List<Float> percentsList = new ArrayList<>();
    private boolean isListening = false;
    private Sensor sensor;

    public float getBestPercent() {
        return Collections.max(percentsList);
    }

    public float getLastPercent() {
        return percentsList.get(percentsList.size() - 1);
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

    public void registerListener(SensorManager sensorManager) {
        sensorManager.registerListener(sensorEventListener, sensor, 1);
    }

    public void unregisterListener(SensorManager sensorManager) {
        sensorManager.unregisterListener(sensorEventListener);
    }

    public SensorCalculator(SensorManager sensorManager, int sensorType, Coordinate coordinate, SensorAction sensorAction) {
        this.sensor = sensorManager.getDefaultSensor(sensorType);
        MAX_VALUE = sensor.getMaximumRange();
        MIN_VALUE = MAX_VALUE * -1;

        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (isListening) {
                    //System.out.println("LISTENING");
                    var currentCoordinate = ((event.values[coordinate.ordinal()] - MIN_VALUE) * 100) / (MAX_VALUE - MIN_VALUE);

                    if (sensorType == Sensor.TYPE_LINEAR_ACCELERATION && !percentsList.isEmpty() && percentsList.get(percentsList.size() - 1) - currentCoordinate >= 10) {
                        System.out.println("Stopped adding into list");
                        sensorAction.sendData();
                    }

                    percentsList.add(currentCoordinate);
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {}
        };

        registerListener(sensorManager);
    }
}
