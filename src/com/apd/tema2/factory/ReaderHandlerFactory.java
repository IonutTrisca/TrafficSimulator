package com.apd.tema2.factory;

import com.apd.tema2.Main;
import com.apd.tema2.entities.Pedestrians;
import com.apd.tema2.entities.ReaderHandler;
import com.apd.tema2.intersections.*;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Returneaza sub forma unor clase anonime implementari pentru metoda de citire din fisier.
 */
public class ReaderHandlerFactory {

    public static ReaderHandler getHandler(String handlerType) {
        // simple semaphore intersection
        // max random N cars roundabout (s time to exit each of them)
        // roundabout with exactly one car from each lane simultaneously
        // roundabout with exactly X cars from each lane simultaneously
        // roundabout with at most X cars from each lane simultaneously
        // entering a road without any priority
        // crosswalk activated on at least a number of people (s time to finish all of them)
        // road in maintenance - 1 lane 2 ways, X cars at a time
        // road in maintenance - N lanes 2 ways, X cars at a time
        // railroad blockage for T seconds for all the cars
        // unmarked intersection
        // cars racing
        return switch (handlerType) {
            case "simple_semaphore" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {
                    // Exemplu de utilizare:
                    Main.intersection = IntersectionFactory.getIntersection("simpleIntersection");
                    
                }
            };
            case "simple_n_roundabout" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {
                    Main.intersection = IntersectionFactory.getIntersection("simple_n_roundabout");
                    
                    String[] properties = br.readLine().split(" ");
                    ((SimpleNRoundAbout) Main.intersection).setAllowedVehicles(Integer.parseInt(properties[0]));
                    ((SimpleNRoundAbout) Main.intersection).setSleepTime(Integer.parseInt(properties[1]));
                }
            };
            case "simple_strict_1_car_roundabout" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {
                    Main.intersection = IntersectionFactory.getIntersection("simple_strict_1_car_roundabout");
                    
                    String[] properties = br.readLine().split(" ");
                    ((Simple1CarRoundAbout) Main.intersection).setNumOfLanes(Integer.parseInt(properties[0]));
                    ((Simple1CarRoundAbout) Main.intersection).setSleepTime(Integer.parseInt(properties[1]));
                    

                }
            };
            case "simple_strict_x_car_roundabout" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {

                    Main.intersection = IntersectionFactory.getIntersection("simple_strict_x_car_roundabout");
                    
                    String[] properties = br.readLine().split(" ");
                    ((SimpleXCarRoundAbout) Main.intersection).setNumCarsAllowed(Integer.parseInt(properties[2]));
                    ((SimpleXCarRoundAbout) Main.intersection).setNumOfLanes(Integer.parseInt(properties[0]));
                    ((SimpleXCarRoundAbout) Main.intersection).setSleepTime(Integer.parseInt(properties[1]));
                }
            };
            case "simple_max_x_car_roundabout" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {
                    Main.intersection = IntersectionFactory.getIntersection("simple_max_x_car_roundabout");
                    
                    String[] properties = br.readLine().split(" ");
                    ((SimpleMaxXCarRoundAbout) Main.intersection).setNumCarsAllowed(Integer.parseInt(properties[2]));
                    ((SimpleMaxXCarRoundAbout) Main.intersection).setNumOfLanes(Integer.parseInt(properties[0]));
                    ((SimpleMaxXCarRoundAbout) Main.intersection).setSleepTime(Integer.parseInt(properties[1]));
                }
            };
            case "priority_intersection" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {
                    Main.intersection = IntersectionFactory.getIntersection("priority_intersection");
                    String[] properties = br.readLine().split(" ");
                    ((PriorityIntersection) Main.intersection).setNumCarsHigh(Integer.parseInt(properties[0]));
                    ((PriorityIntersection) Main.intersection).setNumCarsLow(Integer.parseInt(properties[1]));
                }
            };
            case "crosswalk" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {
                    Main.intersection = IntersectionFactory.getIntersection("crosswalk");
                    ((Crosswalk) Main.intersection).initMessageList(Main.carsNo);
                    String[] properties = br.readLine().split(" ");
                    Main.pedestrians = new Pedestrians(Integer.parseInt(properties[0]), Integer.parseInt(properties[1]));
                }
            };
            case "simple_maintenance" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {
                    int passingCars = Integer.parseInt(br.readLine());
                    Main.intersection = IntersectionFactory.getIntersection("simple_maintenance");
                    ((SimpleMaintenance) Main.intersection).setNumPassingCars(passingCars);
                }
            };
            case "complex_maintenance" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {
                    Main.intersection = IntersectionFactory.getIntersection("complex_maintenance");
                    String[] properties = br.readLine().split(" ");
                    ((ComplexMaintenance) Main.intersection).setFreeLanes(Integer.parseInt(properties[0]));
                    ((ComplexMaintenance) Main.intersection).setTotalLanes(Integer.parseInt(properties[1]));
                    ((ComplexMaintenance) Main.intersection).setPassingCarsInOneGo(Integer.parseInt(properties[2]));
                    ((ComplexMaintenance) Main.intersection).setUpIntersection();
                }
            };
            case "railroad" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {
                    Main.intersection = IntersectionFactory.getIntersection("railroad");
                }
            };
            default -> null;
        };
    }

}
