package com.apd.tema2.factory;

import com.apd.tema2.Main;
import com.apd.tema2.entities.*;
import com.apd.tema2.intersections.*;

import static java.lang.Thread.sleep;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

/**
 * Clasa Factory ce returneaza implementari ale InterfaceHandler sub forma unor
 * clase anonime.
 */
public class IntersectionHandlerFactory {

    public static IntersectionHandler getHandler(String handlerType) {
        // simple semaphore intersection
        // max random N cars roundabout (s time to exit each of them)
        // roundabout with exactly one car from each lane simultaneously
        // roundabout with exactly X cars from each lane simultaneously
        // roundabout with at most X cars from each lane simultaneously
        // entering a road without any priority
        // crosswalk activated on at least a number of people (s time to finish all of
        // them)
        // road in maintenance - 2 ways 1 lane each, X cars at a time
        // road in maintenance - 1 way, M out of N lanes are blocked, X cars at a time
        // railroad blockage for s seconds for all the cars
        // unmarked intersection
        // cars racing
        return switch (handlerType) {
            case "simple_semaphore" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                    // The car enters the semaphore
                    System.out.println("Car " + car.getId() + " has reached the semaphore, now waiting...");

                    // The car waits at the semaphore
                    try {
                        sleep(car.getWaitingTime());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // The car start driving
                    System.out.println("Car " + car.getId() + " has waited enough, now driving...");
                }
            };
            case "simple_n_roundabout" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                    SimpleNRoundAbout rAbout = (SimpleNRoundAbout) Main.intersection;
                    Semaphore s = rAbout.getSem();

                    // The car reached the roundabout
                    System.out.println("Car " + car.getId() + " has reached the roundabout, now waiting...");

                    // The car tries to enter the roundabout but the semaphore stops it
                    // if there are already an n number of cars in the roundabout
                    // as soon as another car leaves the roundabout it can enter
                    try {
                        s.acquire();
                        System.out.println("Car " + car.getId() + " has entered the roundabout");

                        sleep(rAbout.getSleepTime());

                        System.out.println("Car " + car.getId() + " has exited the roundabout after "
                                + rAbout.getSleepTime() / 1000 + " seconds");

                        s.release();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            case "simple_strict_1_car_roundabout" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                    Simple1CarRoundAbout rAbout = (Simple1CarRoundAbout) Main.intersection;
                    CyclicBarrier b = rAbout.getBar();

                    // The car has reached the roundabout
                    System.out.println("Car " + car.getId() + " has reached the roundabout");

                    // It tries to enter the lane, but cannot do so if another car has already entered
                    // and has not left
                    synchronized (rAbout.lanes[car.getStartDirection()]) {
                        try {
                            // With this barrier all cars will enter the roundabout at the same time
                            b.await();

                            System.out.println("Car " + car.getId() + " has entered the roundabout from lane "
                                    + car.getStartDirection());

                            sleep(rAbout.getSleepTime());

                            System.out.println("Car " + car.getId() + " has exited the roundabout after "
                                    + rAbout.getSleepTime() / 1000 + " seconds");

                        } catch (InterruptedException | BrokenBarrierException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            case "simple_strict_x_car_roundabout" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                    SimpleXCarRoundAbout rAbout = (SimpleXCarRoundAbout) Main.intersection;

                    try {
                        System.out.println("Car " + car.getId() + " has reached the roundabout, now waiting...");
                        // All the cars have to get to the roundabout before any car proceeds
                        rAbout.getAllBarr().await();

                        // Each lane allows a maximum of X cars to get in the roundabout
                        rAbout.getLanes()[car.getStartDirection()].acquire();

                        // All the previous must leave before a new selection is made
                        rAbout.getRAboutBarr().await();
                        
                        System.out.println("Car " + car.getId() + " was selected to enter the roundabout from lane "
                                + car.getStartDirection());

                        // All the selected cars enter the roundabout at the same time
                        rAbout.getRAboutBarr().await();

                        System.out.println("Car " + car.getId() + " has entered the roundabout from lane "
                                + car.getStartDirection());

                        sleep(rAbout.getSleepTime());

                        System.out.println("Car " + car.getId() + " has exited the roundabout after "
                                + rAbout.getSleepTime() / 1000 + " seconds");

                        rAbout.getLanes()[car.getStartDirection()].release();
                    } catch (InterruptedException | BrokenBarrierException e) {
                        e.printStackTrace();
                    }

                }
            };
            case "simple_max_x_car_roundabout" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                    // Get your Intersection instance
                    SimpleMaxXCarRoundAbout rAbout = (SimpleMaxXCarRoundAbout) Main.intersection;

                    try {
                        sleep(car.getWaitingTime());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } // NU MODIFICATI

                    // Continuati de aici

                    try {
                        System.out.println("Car " + car.getId() + " has reached the roundabout from lane "
                                + car.getStartDirection());

                        // The car tries to enter the roundabout and, if there are fewer that X
                        // cars enter from that lane, it is allowed to enter
                        rAbout.getLanes()[car.getStartDirection()].acquire();

                        System.out.println("Car " + car.getId() + " has entered the roundabout from lane "
                                + car.getStartDirection());

                        sleep(rAbout.getSleepTime());

                        System.out.println("Car " + car.getId() + " has exited the roundabout after "
                                + rAbout.getSleepTime() / 1000 + " seconds");

                        rAbout.getLanes()[car.getStartDirection()].release();

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            };
            case "priority_intersection" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                    // Get your Intersection instance
                    PriorityIntersection intersection = (PriorityIntersection) Main.intersection;

                    try {
                        sleep(car.getWaitingTime());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } // NU MODIFICATI

                    // Continuati de aici

                    if (car.getPriority() == 1) {
                        // If the car has low priority it checks to see if there are any cars in the 
                        // intersection.
                        try {
                            System.out.println(
                                    "Car " + car.getId() + " with low priority is trying to enter the intersection...");
                            intersection.getSem().acquire();

                            // If there are priority cars, it busy waits until the are no more cars 
                            while (intersection.getNumPriorityCarsIn() != 0) {
                                System.out.print("");
                            }

                            System.out
                                    .println("Car " + car.getId() + " with low priority has entered the intersection");

                            intersection.getSem().release();

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        // If the car has high priority it increments the priority cars counter
                        // waits the required intersection time and then it decrements that ssame counter
                        // to let low priority cars that it left the intersection
                        try {
                            intersection.getPrioritySem().acquire();
                            intersection.incrementNumPriorityCarsIn();
                            intersection.getPrioritySem().release();

                            System.out
                                    .println("Car " + car.getId() + " with high priority has entered the intersection");

                            try {
                                sleep(intersection.getWaitTime());
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            System.out
                                    .println("Car " + car.getId() + " with high priority has exited the intersection");

                            intersection.getPrioritySem().acquire();
                            intersection.decrementNumPriorityCarsIn();
                            intersection.getPrioritySem().release();

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            case "crosswalk" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                    Crosswalk crosswalk = (Crosswalk) Main.intersection;
                    String currentMessage;

                    // While not all pedestrians have finished crossing
                    // the cars check if pedestrians are crossing
                    while (!Main.pedestrians.isFinished()) {
                        if (!Main.pedestrians.isPass()) {
                            // If they are not crossing the current message is green light
                            currentMessage = "Car " + car.getId() + " has now green light";
                        } else {
                            // If they are crossing the message becomes red light
                            currentMessage = "Car " + car.getId() + " has now red light";
                        }

                        // Here we check if the current message for this car differs from the
                        // previous one from the same car, in which case it will be displayed
                        if (crosswalk.getLastMessage(car.getId()) == "null" + car.getId()
                                || crosswalk.getLastMessage(car.getId()).compareTo(currentMessage) != 0) {
                            System.out.println(currentMessage);
                            crosswalk.setLastMessage(currentMessage, car.getId());
                        }
                    }
                    
                    // Once all the pedestrians have crossed if the car still had red light
                    // it now becomes green
                    currentMessage = "Car " + car.getId() + " has now green light";

                    if (crosswalk.getLastMessage(car.getId()).compareTo(currentMessage) != 0) {
                        System.out.println(currentMessage);
                        crosswalk.setLastMessage(currentMessage, car.getId());
                    }
                }
            };
            case "simple_maintenance" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                    SimpleMaintenance intersection = (SimpleMaintenance) Main.intersection;
                    boolean hasPassed = false;   // is true when a car has passed the bottleneck

                    System.out.println("Car " + car.getId() + " from side number " + car.getStartDirection()
                            + " has reached the bottleneck");

                    // Each car goes through this loop until all the cars have passed
                    // in order to satisfy the barrier requirements
                    while (intersection.getTotalPassedCars() != Main.carsNo) {
                        // The car with id 0 resets the number of passed cars 
                        // on each iteration of the loop
                        if (car.getId() == 0) {
                            intersection.setNumCars0(0);
                            intersection.setNumCars1(0);
                        }

                        try {
                            // all the other cars wait for the car number 0 to reset the numbers above
                            intersection.getBarr().await();

                            // if a car comes from lane 0 and has not passed
                            if (car.getStartDirection() == 0 && !hasPassed) {
                                // it tries to acquire the semaphore to increment the number of total passed
                                // cars and the number of cars from lane 0 passed in this iteration
                                intersection.getSem().acquire();

                                // if there is already the maximum number of cars in the intersection
                                // then it move on and will try again in the next interation
                                if (intersection.getNumCars0() < intersection.getNumPassingCars()) {
                                    // if not, then it does what was mentioned above
                                    intersection.incrementTotalPassedCars();
                                    hasPassed = true;
                                    System.out.println("Car " + car.getId() + " from side number "
                                            + car.getStartDirection() + " has passed the bottleneck");
                                    intersection.setNumCars0(intersection.getNumCars0() + 1);
                                }
                                intersection.getSem().release();
                            }

                            // all cars wait for the cars from lane number 0 to pass before moving on to lane nr 1
                            intersection.getBarr().await();

                            // if a car comes from lane number 1 it does the exact same thing as a car
                            // from lane number 0, bu it increments a different counter
                            if (car.getStartDirection() == 1 && !hasPassed) {
                                intersection.getSem().acquire();

                                if (intersection.getNumCars1() < intersection.getNumPassingCars()) {
                                    intersection.setNumCars1(intersection.getNumCars1() + 1);
                                    intersection.incrementTotalPassedCars();
                                    hasPassed = true;
                                    System.out.println("Car " + car.getId() + " from side number "
                                            + car.getStartDirection() + " has passed the bottleneck");
                                }

                                intersection.getSem().release();
                            }

                            // the cars wait here for all the cars from lane one to finish crossing
                            // before resetting everyhting and starting again
                            intersection.getBarr().await();
                        } catch (InterruptedException | BrokenBarrierException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };

            case "complex_maintenance" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                    ComplexMaintenance intersection = (ComplexMaintenance) Main.intersection;
                    boolean hasPassed = false;  // is true if a car has passed

                    // Set the order in which cars came on each old lane
                    synchronized (intersection.getCarsPerOldLane().get(car.getStartDirection())) {
                        intersection.getCarsPerOldLane().get(car.getStartDirection()).add(car.getId());
                        System.out.println(
                            "Car " + car.getId() + " has come from the lane number " + car.getStartDirection());
                    }

                    // the new lane of the car
                    int myNewLane = intersection.getOldLaneToNewLane().get(car.getStartDirection());

                    // the queue of old lanes in the new lane
                    ArrayBlockingQueue<Integer> newLane = intersection.getAvailableLanes()
                            .get(intersection.getOldLaneToNewLane().get(car.getStartDirection()));

                    // cars passed per new lane
                    ConcurrentHashMap<Integer, Integer> carsPassedPerLane = intersection.getCarsPassedPerLane();
                    try {

                        // all cars go through this loop until all cars have passed
                        while (intersection.getTotalCarsPassed() < Main.carsNo) {
                            // wait for all cars to arrive
                            intersection.getBarr().await();

                            // if a car has not passed and it is the first in lane then try to pass
                            if(!hasPassed && intersection.getCarsPerOldLane().get(car.getStartDirection()).get(0) == car.getId()) {
                                // if the current lane is the car's lane then try to pass
                                if (newLane.peek() == car.getStartDirection()) {
                                    // if the maximum number of cars has not been reached the pass
                                    synchronized (carsPassedPerLane.get(myNewLane)) {
                                        if (carsPassedPerLane.get(myNewLane) < intersection.getPassingCarsInOneGo()) {
                                            carsPassedPerLane.put(myNewLane, carsPassedPerLane.get(myNewLane) + 1);
                                            hasPassed = true;
                                            System.out.println("Car " + car.getId() + " from the lane " +  car.getStartDirection() +
                                                                    " has entered lane number " + myNewLane);

                                            synchronized(intersection.getTotalCarsPassed()) {
                                                intersection.incrementTotalCarsPassed();
                                            }
                                            
                                            synchronized(intersection.getCarsPerOldLane().get(car.getStartDirection())) {
                                                intersection.getCarsPerOldLane().get(car.getStartDirection()).remove(0);
                                            }
                                        }
                                    }
                                }
                            }

                            // wait before car 0 does the computations
                            intersection.getBarr().await();

                            if(car.getId() == 0) {
                                boolean check = true;
                                for (int i = 0; i < intersection.getFreeLanes(); i++) {
                                    // for each new lane if the number of cars allowed has not been reached, check 
                                    // if the are any more cars on the old lane it currently hosts
                                    if(carsPassedPerLane.get(i) < intersection.getPassingCarsInOneGo()) {
                                        if (intersection.getAvailableLanes().get(i).peek() != null)
                                            // if there are cars on the old lane then do not check to see if you can reove the lane from the queue
                                            // and try to pass more cars
                                            if (intersection.getCarsPerOldLane().get(intersection.getAvailableLanes().get(i).peek()).size() != 0)
                                                check = false;
                                    }
                                }

                                // if there are no more cars on the old lanes, or thhe number of cars allowed
                                // has been reached then check to see if to remove an old lane or to add it back in the queu
                                if (check) {
                                    intersection.resetCarsPassedPerLane();

                                    for(int i = 0; i < intersection.getFreeLanes(); i++) {
                                        if (intersection.getAvailableLanes().get(i).peek() != null) {
                                            int oldLane = intersection.getAvailableLanes().get(i).poll();
                                            if(intersection.getCarsPerOldLane().get(oldLane).size() != 0) {
                                                intersection.getAvailableLanes().get(i).add(oldLane);
                                                System.out.println("The initial lane " + oldLane + " has no permits and is moved to the back of the new lane queue");
                                            } else {
                                                System.out.println("The initial lane " + oldLane + " has been emptied and removed from the new lane queue");
                                            }
                                        }
                                    }
                                }
                            }

                            intersection.getBarr().await();
                        }
                    } catch (InterruptedException | BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                   
                }
            };
            case "railroad" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                    Railroad rRoad = (Railroad) Main.intersection;

                    // The cars arrive in order, show their messages and increment a counter
                    // that hold the number of cars arrived
                    try {
                        rRoad.getArrivedSem().acquire();
                        System.out.println("Car " + car.getId() + " from side number " + car.getStartDirection()
                        + " has stopped by the railroad");
                        rRoad.incrementCarsArrived();
                        rRoad.getArrivedSem().release();
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    
                    try {
                        rRoad.getSem().acquire();
                        // Once a car has arrived it busy waits until all other cars have arrived
                        while(rRoad.getCarsArrived() != Main.carsNo) {
                            rRoad.getCarsArrived();
                        }
                        
                        // The first car that arrives also checks if the train passed message has been shown
                        // in which case it alerts the other cars to not show the message again
                        if (!rRoad.getTrainPassed()) {
                            System.out.println("The train has passed, cars can now proceed");
                            rRoad.setTrainPassed(true);
                        }
                        
                        System.out.println("Car " + car.getId() + " from side number " + car.getStartDirection()
                            + " has started driving");

                        rRoad.getSem().release();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            };
            default -> null;
        };
    }
}
