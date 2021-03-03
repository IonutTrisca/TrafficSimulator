package com.apd.tema2.intersections;

import java.util.concurrent.Semaphore;

import com.apd.tema2.entities.Intersection;

public class Railroad implements Intersection {
    private Semaphore sem = new Semaphore(1, true); // semaphore with fairness which makes sure the cars
                                                    // pass in the order that they came in
    private Semaphore arrivedSem = new Semaphore(1, true);
    private int carsArrived = 0;   // number of cars arrived
    private boolean trainPassed = false;  // lets the other cars know to not show the train passed message
    

    public Semaphore getArrivedSem() {
        return arrivedSem;
    }

    public Semaphore getSem() {
        return sem;
    }

    public int getCarsArrived() {
        return carsArrived;
    }

    public void incrementCarsArrived() {
        this.carsArrived++;
    }
    
    public boolean getTrainPassed() {
        return trainPassed;
    }

    public void setTrainPassed(boolean trainPassed) {
        this.trainPassed = trainPassed;
    }
}
