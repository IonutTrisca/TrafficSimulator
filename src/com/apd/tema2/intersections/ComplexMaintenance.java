package com.apd.tema2.intersections;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CyclicBarrier;

import com.apd.tema2.Main;
import com.apd.tema2.entities.Intersection;

public class ComplexMaintenance implements Intersection {
    private int freeLanes;    // number of free lanes
    private int totalLanes;   // number of total lanes
    private int passingCarsInOneGo;    // number of cars allowe to pass in one go per lane
    private List<ArrayBlockingQueue<Integer>> availableLanes; // the avaiable new lanes and which old lanes 
                                                             // each one will carry
    private HashMap<Integer, Integer> oldLaneToNewLane;     // a map from an old lane to a new one
    private ConcurrentHashMap<Integer, List<Integer>> carsPerOldLane;   // a lsit of cars in order which came on each of 
                                                                        // the old lanes
    private volatile ConcurrentHashMap<Integer, Integer> carsPassedPerLane; // number of cars passed per new lane
    private CyclicBarrier barr; //barrier which holds all the cars
    private Integer totalCarsPassed = 0;    // number of all the cars that have passed
    
    public void setUpIntersection() {
        availableLanes = new ArrayList<>();
        oldLaneToNewLane = new HashMap<>();
        carsPerOldLane = new ConcurrentHashMap<>();
        barr = new CyclicBarrier(Main.carsNo);
        carsPassedPerLane = new ConcurrentHashMap<>();

        for(int i = 0; i < totalLanes; i++){
            carsPerOldLane.put(i, new ArrayList<>());
        }

        // determining which new lane each old lane gets
        for(int i = 0; i < freeLanes; i++) {
            ArrayBlockingQueue<Integer> q = new ArrayBlockingQueue<>(totalLanes);
            carsPassedPerLane.put(i, 0);

            int start = (int) (i * (double) totalLanes / freeLanes);
            int end = (int) Math.min((i + 1) * (double) totalLanes / freeLanes, totalLanes);

            for(int j = start; j < end; j++) {
                oldLaneToNewLane.put(j, i);
                q.add(j);
            }
            
            availableLanes.add(q);
        }

    }

    public void resetCarsPassedPerLane() {
        for(int i = 0; i < freeLanes; i++)
            carsPassedPerLane.put(i, 0);
    }
    
    public void incrementTotalCarsPassed() {
        this.totalCarsPassed++;
    }

    public Integer getTotalCarsPassed() {
        return totalCarsPassed;
    }

    public ConcurrentHashMap<Integer, Integer> getCarsPassedPerLane() {
        return carsPassedPerLane;
    }

    public List<ArrayBlockingQueue<Integer>> getAvailableLanes() {
        return availableLanes;
    }

    public CyclicBarrier getBarr() {
        return barr;
    }

    public HashMap<Integer, Integer> getOldLaneToNewLane() {
        return oldLaneToNewLane;
    }

    public ConcurrentHashMap<Integer, List<Integer>> getCarsPerOldLane() {
        return carsPerOldLane;
    }

    public void setFreeLanes(int freeLanes) {
        this.freeLanes = freeLanes;
    }

    public int getFreeLanes() {
        return freeLanes;
    }

    public void setTotalLanes(int totalLanes) {
        this.totalLanes = totalLanes;
    }

    public int getTotalLanes() {
        return totalLanes;
    }

    public void setPassingCarsInOneGo(int passingCarsInOneGo) {
        this.passingCarsInOneGo = passingCarsInOneGo;
    }

    public int getPassingCarsInOneGo() {
        return passingCarsInOneGo;
    }
}
