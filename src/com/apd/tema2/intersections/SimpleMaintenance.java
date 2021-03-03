package com.apd.tema2.intersections;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

import com.apd.tema2.Main;
import com.apd.tema2.entities.Intersection;

public class SimpleMaintenance implements Intersection {
    private int numPassingCars;   // the number of cars allowed to pass in one go
    private volatile int numCars0 = 0;  // the number of cars from lane 0 that have passed it one interation
    private volatile int numCars1 = 0;  // the number of cars from lane 1 that have passed it one interation
    private Semaphore sem = new Semaphore(1); // a sempahore to synchronize the incrementing of different counters
    private int totalPassedCars = 0;  // the number of total cars passed 

    private CyclicBarrier barr; // a barrier which holds until all other cars have arrived

    public void setNumPassingCars(int numPassingCars) {
        this.numPassingCars = numPassingCars;
        barr = new CyclicBarrier(Main.carsNo);
    }
    
    public Semaphore getSem() {
        return sem;
    }

    public void incrementTotalPassedCars() {
        totalPassedCars++;
    }
    public int getTotalPassedCars() {
        return totalPassedCars;
    }

    public void setNumCars1(int numCars1) {
        this.numCars1 = numCars1;
    }

    public int getNumCars1() {
        return numCars1;
    }

    public void setNumCars0(int numCars0) {
        this.numCars0 = numCars0;
    }

    public int getNumCars0() {
        return numCars0;
    }

    public int getNumPassingCars() {
        return numPassingCars;
    }

    public CyclicBarrier getBarr() {
        return barr;
    }
}
