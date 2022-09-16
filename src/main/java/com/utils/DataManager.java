package com.utils;

import com.elevator.Building;
import com.elevator.Floor;
import com.elevator.Passenger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class DataManager {

    private DataManager() {}

    /**
     * Generates random number in the given range
     * @min The minimum value, that can be returned
     * @max The maximum value, that can be returned
     * @return A random number between min and max values (inclusive)
     */
    public static int getRandomNumber(int min, int max) {
        return new Random().nextInt(min, max + 1);
    }

    /**
     * Generates list of passengers on the floor
     * @param amountOfPassengers Current amount of passengers on the floor
     * @param floor Current floor number
     * @param floorCounts Total amount of floors
     * @return List of passengers
     */
    public static List<Passenger> setPassengers(int amountOfPassengers, int floor, int floorCounts) {
        List<Passenger> list = new ArrayList<>();
        IntStream.range(1, amountOfPassengers + 1).forEach(index -> list.add(new Passenger(floor, getRandomFloor(floor, floorCounts))));
        return list;
    }

    /**
     * Generate list of floors in the building
     * @param amountOfFloors Amount of floors in the building
     * @return List of floors
     */
    public static List<Floor> setFloors(int amountOfFloors) {
        List<Floor> list = new ArrayList<>();
        IntStream.range(0, amountOfFloors).forEach(index -> list.add(new Floor(index + 1, amountOfFloors)));
        return list;
    }

    /**
     * Generate a random floor number as a passenger's destination target.
     * @param currentFloor Current floor of passenger
     * @param floorCounts Total amount of floors in the building
     * @return Random floor number different from the current number
     */
    public static int getRandomFloor(int currentFloor, int floorCounts) {
        int randomFloor = getRandomNumber(Building.getStartFloor(), floorCounts);
        while (randomFloor == currentFloor) {
            randomFloor = getRandomNumber(Building.getStartFloor(), floorCounts);
        }
        return randomFloor;
    }

    /**
     * Setting the direction of the elevator moving according to the most passenger's destination
     * @param passengers List of the passengers in the elevator
     * @return Elevator moving direction
     */
    public static String setDirection(List<Passenger> passengers) {
        String direction;
        int upDirection = passengers.stream().filter(passenger -> passenger.getDestinationFloor() > passenger.getCurrentFloor()).toList().size();
        int downDirection = passengers.stream().filter(passenger -> passenger.getDestinationFloor() < passenger.getCurrentFloor()).toList().size();
        if (downDirection > upDirection) {
            direction = "down";
        } else {
            direction = "up";
        }
        return direction;
    }

    /**
     * Getting the graphical representation of the elevator moving direction
     * @param floor Floor object
     * @return A string representing the up-arrow or down-arrow
     */
    public static String printElevatorDirection(Building building, Floor floor) {
        if (building.getElevator().getCurrentFloor() == floor.getFloorNumber()) {
            if (building.getElevator().getDirection().equals("up")) {
                return "\u21D1";
            } else {
                return "\u21D3";
            }
        }
        return " ";
    }

    /**
     * Getting the String contained floor-destination of all passengers in the elevator
     * @param floor Floor object
     * @return A string with destination floors of all passengers in the elevator if the elevator is on the current floor.
     * Otherwise, returned empty string
     */
    public static String printPassengersInTheElevator(Building building, Floor floor) {
        if (building.getElevator().getCurrentFloor() == floor.getFloorNumber()) {
            StringBuilder builder = new StringBuilder();
            building.getElevator().getPassengers().forEach(passenger -> builder.append(passenger.getDestinationFloor()).append(" "));
            return builder.toString().strip();
        }
        return " ".repeat(14);
    }

    /**
     * Getting floor-destination of all the passengers on the floor
     * @param floor Floor object
     * @return A string with destination floors of all passengers on the floor
     */
    public static String printPassengersOnTheFloor(Floor floor) {
        StringBuilder builder = new StringBuilder();
        floor.getPassengersOnFloor().forEach(passenger -> builder.append(passenger.getDestinationFloor()).append(" "));
        return builder.toString().strip();
    }
}
