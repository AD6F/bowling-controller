package com.ad6f.bowling.services.sensors;

public class Coordinate {
    public float x;
    public float y;
    public float z;

    public Coordinate(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static Coordinate fromArray(float[] array) {
        return new Coordinate(array[0], array[1], array[2]);
    }
}