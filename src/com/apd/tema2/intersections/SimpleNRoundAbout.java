package com.apd.tema2.intersections;

import java.util.concurrent.Semaphore;

import com.apd.tema2.entities.Intersection;

public class SimpleNRoundAbout implements Intersection {
    private int numOfAllowedVehicles;
    private int sleepTime;
    private Semaphore sem;

    public void setAllowedVehicles(int numOfAllowedVehicles) {
        this.numOfAllowedVehicles = numOfAllowedVehicles;
        this.sem = new Semaphore(numOfAllowedVehicles);
    }

    public Semaphore getSem() {
        return sem;
    }

    public void setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
    }

    public int getNumOfAllowedVehicles() {
        return numOfAllowedVehicles;
    }

    public int getSleepTime() {
        return sleepTime;
    }
}
