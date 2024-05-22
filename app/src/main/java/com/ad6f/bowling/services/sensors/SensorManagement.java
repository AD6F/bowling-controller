package com.ad6f.bowling.services.sensors;

import android.hardware.Sensor;
import android.hardware.SensorManager;

public class SensorManagement {
    private boolean isRegister = false;

    private SensorManager sensorManager;
    
    private SensorCalculator linearCalculator;

    private SensorCalculator rotationCalculator;

    private SensorCalculator gravityCalculator;
    
    public void unregisterListeners() {
        if(isRegister) {
            linearCalculator.unregisterListener(sensorManager);
            rotationCalculator.unregisterListener(sensorManager);
            gravityCalculator.unregisterListener(sensorManager);
            isRegister = false;
        }
    }

    public void registerListeners() {
        if(!isRegister) {
            linearCalculator.registerListener(sensorManager);
            rotationCalculator.registerListener(sensorManager);
            gravityCalculator.registerListener(sensorManager);
            isRegister = true;
        }
    }

    public void start() {
        linearCalculator.start();
        rotationCalculator.start();
        gravityCalculator.start();
    }

    public void close() {
        linearCalculator.close();
        rotationCalculator.close();
        gravityCalculator.close();
    }

    public float getForce() {
        return linearCalculator.getBestPercent();
    }

    public float getTilt() {
        return gravityCalculator.getLastPercent();
    }

    public float getRotation() {
        return rotationCalculator.getBestPercent();
    }

    public SensorManagement(SensorManager sensorManager, SensorAction sendData) {
        this.sensorManager = sensorManager;
        
        this.linearCalculator = new SensorCalculator(
            sensorManager, 
            Sensor.TYPE_LINEAR_ACCELERATION,
            Coordinate.Y,
            sendData
        );

        this.rotationCalculator = new SensorCalculator(
            sensorManager,
            Sensor.TYPE_ROTATION_VECTOR,
            Coordinate.Y,
            null
        );

        this.gravityCalculator = new SensorCalculator(
            sensorManager,
            Sensor.TYPE_ROTATION_VECTOR,
            Coordinate.X,
            null
        );

        this.registerListeners();
    }
}
