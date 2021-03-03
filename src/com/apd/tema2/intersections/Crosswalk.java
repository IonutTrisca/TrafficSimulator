package com.apd.tema2.intersections;

import com.apd.tema2.entities.Intersection;

public class Crosswalk implements Intersection {
    private String[] lastMessage; // array with the last message for each car

    public String getLastMessage(int id) {
        return lastMessage[id];
    }

    public void setLastMessage(String lastMessage, int id) {
        this.lastMessage[id] = lastMessage;
    }

    public void initMessageList(int numOfCars) {
        lastMessage = new String[numOfCars];

        for (int i = 0; i < numOfCars; i++) {
            lastMessage[i] = "null" + i;
        }
    }
}
