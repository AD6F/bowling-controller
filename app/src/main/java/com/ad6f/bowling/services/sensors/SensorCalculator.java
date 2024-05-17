package com.ad6f.bowling.services.sensors;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.ToString;

@ToString
@AllArgsConstructor
public class SensorCalculator {
    private Sensor sensor;

    private static boolean isAddingIntoList = true;

    private SensorManager sensorManager;

    public List<Coordinate> coordinateList = new ArrayList<>();

    public final float MAX_SENSOR_VALUE;

    public SensorCalculator(SensorManager sensorManager, int sensorType, SensorAction sensorAction) {
        this.sensorManager = sensorManager;
        this.sensor = sensorManager.getDefaultSensor(sensorType);
        MAX_SENSOR_VALUE = sensor.getMaximumRange();

        sensorManager.registerListener(
            new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent event) {
                    if(isAddingIntoList) {
                        var currentCoordinate = Coordinate.fromArray(event.values);
                        coordinateList.add(currentCoordinate);

                        if(sensorType == Sensor.TYPE_LINEAR_ACCELERATION && currentCoordinate.y - coordinateList.get(coordinateList.size() - 1).y > 0) {
                            // To block the all the other SensorCalculator, to add data into their list
                            isAddingIntoList = false;

                            // To unblock all SensorCalculator, if the data was successfully send.
                            isAddingIntoList = sensorAction.sendData();
                        }
                    }
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) {

                }
            },
            sensor,
            1
        );
    }
}
