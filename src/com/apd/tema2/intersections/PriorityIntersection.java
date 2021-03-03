package com.apd.tema2.intersections;

import java.util.concurrent.Semaphore;

import com.apd.tema2.entities.Intersection;

public class PriorityIntersection implements Intersection {
    private Semaphore sem = new Semaphore(1, true); // semaphore with fairness for making sure 
                                                    // the cars enter in the same order they came in
    private Semaphore prioritySem = new Semaphore(1, true); // sempahore for incrementing and decremeneting 
                                                            // the priority cars counter
    private int waitTime = 2000;
    private int numCarsLow;
    private int numCarsHigh;
    private int numPriorityCarsIn = 0;       // the number of priority cars in the intersection

    public void setNumCarsHigh(int numCarsHigh) {
        this.numCarsHigh = numCarsHigh;
    }

    public Semaphore getSem() {
        return sem;
    }

    public Semaphore getPrioritySem() {
        return prioritySem;
    }

    public void setNumCarsLow(int numCarsLow) {
        this.numCarsLow = numCarsLow;
    }

    public int getNumCarsHigh() {
        return numCarsHigh;
    }

    public int getNumCarsLow() {
        return numCarsLow;
    }

    public void incrementNumPriorityCarsIn() {
        numPriorityCarsIn++;
    }

    public void decrementNumPriorityCarsIn() {
        numPriorityCarsIn--;
    }
    
    public int getNumPriorityCarsIn() {
        return numPriorityCarsIn;
    }

    public int getWaitTime() {
        return waitTime;
    }
}
