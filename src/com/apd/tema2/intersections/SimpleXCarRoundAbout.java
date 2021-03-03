package com.apd.tema2.intersections;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

import com.apd.tema2.Main;
import com.apd.tema2.entities.Intersection;

public class SimpleXCarRoundAbout implements Intersection {
    private int numOfLanes;
    private int sleepTime;
    private CyclicBarrier rAboutBarr;
    private int numCarsAllowed;
    private CyclicBarrier allBarr;

    private Semaphore[] lanes;

    public void setNumCarsAllowed(int numCarsAllowed) {
        this.numCarsAllowed = numCarsAllowed;
    }

    public void setNumOfLanes(int numOfLanes) {
        this.numOfLanes = numOfLanes;
        this.rAboutBarr = new CyclicBarrier(numOfLanes * numCarsAllowed);
        this.lanes = new Semaphore[numOfLanes];
        this.allBarr = new CyclicBarrier(Main.carsNo);

        for (int i = 0; i < numOfLanes; i++) {
            lanes[i] = new Semaphore(numCarsAllowed);
        }
    }

    public Semaphore[] getLanes() {
        return lanes;
    }
    
    public CyclicBarrier getAllBarr() {
        return allBarr;
    }

    public int getNumCarsAllowed() {
        return numCarsAllowed;
    }

    public CyclicBarrier getRAboutBarr() {
        return rAboutBarr;
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
