package com.apd.tema2.intersections;

import java.util.concurrent.CyclicBarrier;

import com.apd.tema2.entities.Intersection;

public class Simple1CarRoundAbout implements Intersection {
    private int numOfLanes;
    private int sleepTime;
    private CyclicBarrier bar;
    public String[] lanes; // each string from this array represents one lane
                           // and a car will try to syncronize on the string from its lane
                           // in order to be the only one that enters the roundabout from that lane

    public void setNumOfLanes(int numOfLanes) {
        this.numOfLanes = numOfLanes;
        this.bar = new CyclicBarrier(numOfLanes);
        this.lanes = new String[numOfLanes];
        
        for (int i = 0; i < numOfLanes; i++) {
            lanes[i] = Integer.toString(i);
        }
    }

    public CyclicBarrier getBar() {
        return bar;
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
