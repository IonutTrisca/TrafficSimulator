package com.apd.tema2.intersections;

import java.util.concurrent.Semaphore;

import com.apd.tema2.entities.Intersection;

public class SimpleMaxXCarRoundAbout implements Intersection {
    private int numOfLanes;
    private int sleepTime;
    private int numCarsAllowed;

    public Semaphore[] lanes;

    public void setNumCarsAllowed(int numCarsAllowed) {
        this.numCarsAllowed = numCarsAllowed;
    }

    public void setNumOfLanes(int numOfLanes) {
        this.numOfLanes = numOfLanes;
        this.lanes = new Semaphore[numOfLanes];

        for (int i = 0; i < numOfLanes; i++) {
            lanes[i] = new Semaphore(numCarsAllowed);
        }
    }

    public Semaphore[] getLanes() {
        return lanes;
    }

    public int getNumCarsAllowed() {
        return numCarsAllowed;
    }

    public void setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
    }

    public int getNumOfLanes() {
        return numOfLanes;
    }

    public int getSleepTime() {
        return sleepTime;
    }
}
