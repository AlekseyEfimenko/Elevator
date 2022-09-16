package com.elevator;

import static com.utils.FileManager.getInstance;
import com.utils.DataManager;
import org.apache.log4j.Logger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Elevator implements IElevator<Building> {
    private static final int MAX_CAPACITY = Integer.parseInt(getInstance().getProperties("maxCapacity"));
    private static final Logger LOGGER = Logger.getLogger(Elevator.class);
    private int currentCapacity;
    private int currentFloor;
    private List<Passenger> passengers;
    private String direction;

    public Elevator() {
        currentCapacity = Integer.parseInt(getInstance().getProperties("startCapacity"));
        currentFloor = Integer.parseInt(getInstance().getProperties("startFloor"));
        passengers = new ArrayList<>();
        direction = "up";
    }

    public void move(Building building, int step) {
        unload(building);
        load(building);
        printElevatorMovings(building, step);
        LOGGER.info(String.format("Elevator moves %1$s", direction));
        if (direction.equals("up")) {
            if (currentFloor == building.getFloorCounts()) {
                currentFloor--;
            } else {
                currentFloor++;
            }
        }
        if (direction.equals("down")) {
            if (currentFloor == Building.getStartFloor()) {
                currentFloor++;
            } else {
                currentFloor--;
            }
        }
        LOGGER.info(String.format("Elevator now is on the %1$d floor", currentFloor));
    }

    public void load(Building building) {
        LOGGER.info("Loading the passengers according to the elevator moving direction");
        selectDirection(building);
        if (currentCapacity < MAX_CAPACITY) {
            if (direction.equals("up")) {
                building.getFloors().get(currentFloor - 1).getPassengersOnFloor()
                        .stream().filter(passenger -> passenger.getDestinationFloor() > currentFloor)
                        .toList()
                        .forEach(pas -> loadPassenger(building, pas));
            } else if (direction.equals("down")) {
                building.getFloors().get(currentFloor - 1).getPassengersOnFloor()
                        .stream().filter(passenger -> passenger.getDestinationFloor() < currentFloor)
                        .toList()
                        .forEach(pas -> loadPassenger(building, pas));
            }
        }
    }

    public void unload(Building building) {
        LOGGER.info("Unloading the passengers");
        if (building.getFloors().get(currentFloor - 1).isFreeSpaceAvailable()) {
            passengers.stream().filter(passenger -> passenger.getDestinationFloor() == currentFloor).toList()
                    .forEach(passenger -> unloadPassenger(building, passenger));

        } else {
            changePassengerDestinationFloor(building);
        }
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public List<Passenger> getPassengers() {
        return passengers;
    }

    public String getDirection() {
        return direction;
    }

    /**
     * Selecting the direction to move according to the desire of most passengers on the floor (if the elevator is empty).
     * Or if the elevator has reached the max/min floor.
     * @param building Building where elevator works
     */
    private void selectDirection(Building building) {
        LOGGER.info("Select the elevator moving direction");
        if (currentCapacity == 0) {
            String directionOfMostPassengers = building.getFloors().get(currentFloor - 1).getDirectionOfMostPassengers();
            if (directionOfMostPassengers.equals("up")) {
                direction = "up";
            }
            if (directionOfMostPassengers.equals("down")) {
                direction = "down";
            }
            LOGGER.info(String.format("The elevator is empty. Most of the passengers on the floor have voted to move %1$s", direction));
        }
        if (currentFloor == building.getFloorCounts()) {
            direction = "down";
            LOGGER.info(String.format("The elevator has reached the top floor of the building, so the direction is set to %1$s", direction));
        } else if (currentFloor == Building.getStartFloor()) {
            LOGGER.info(String.format("The elevator has reached the bottom floor of the building, so the direction is set to %1$s", direction));
            direction = "up";
        }
    }

    private void loadPassenger(Building building, Passenger passenger) {
        if (currentCapacity < MAX_CAPACITY) {
            passengers.add(passenger);
            currentCapacity++;
            LOGGER.info(String.format("The current capacity of the elevator is %1$d passenger(s)", currentCapacity));
            building.getFloors().get(currentFloor - 1).removePassenger(passenger);
        }
    }

    private void unloadPassenger(Building building, Passenger passenger) {
        if (building.getFloors().get(currentFloor - 1).isFreeSpaceAvailable()) {
            passengers.remove(passenger);
            currentCapacity--;
            LOGGER.info(String.format("The current capacity of the elevator is %1$d passenger(s)", currentCapacity));
            passenger.setCurrentFloor(currentFloor);
            passenger.setDestinationFloor(DataManager.getRandomFloor(currentFloor, building.getFloorCounts()));
            building.getFloors().get(currentFloor - 1).addPassenger(passenger);
        }
    }

    /**
     * If all the passengers in the elevator want to quit on the floors, where the max capacity is reached,
     * they will be informed, that the floors are overcrowded. And they will be offered to select another floor.
     * @param building Building with an elevator
     */
    private void changePassengerDestinationFloor(Building building) {
        LOGGER.error("No free space for the passengers on the destination floor. They need to select another floor");
        Collection<Integer> passengerDestinations = new ArrayList<>();
        passengers.stream().distinct().toList().forEach(passenger -> passengerDestinations.add(passenger.getDestinationFloor()));
        Collection<Integer> overpopulatedFloors = new ArrayList<>();
        building.getFloors().stream()
                .filter(floor -> floor.getPassengerCounts() == Floor.getMaxAmountOfPassengers())
                .toList()
                .forEach(floor -> overpopulatedFloors.add(floor.getFloorNumber()));
        if (overpopulatedFloors.containsAll(passengerDestinations)) {
            passengers.forEach(psngr -> psngr.setDestinationFloor(DataManager.getRandomFloor(currentFloor, building.getFloorCounts())));
            if (DataManager.setDirection(passengers).equals("up")) {
                direction = "up";
            } else {
                direction = "down";
            }
        }
    }

    private void printElevatorMovings (Building building, int steps) {
        System.out.format("%1$s Step %2$d %1$s%n%n", "=".repeat(54), steps);
        building.getFloors().stream().collect(Collectors.collectingAndThen(Collectors.toList(), lst -> {
            Collections.reverse(lst);
            return lst.stream();
        })).forEach(floor -> System.out.format("%1$sFloor %2$-2d |%3$s %4$-14s %3$s| %5$2s%n"
                , " ".repeat(40)
                , floor.getFloorNumber()
                , DataManager.printElevatorDirection(building, floor)
                , DataManager.printPassengersInTheElevator(building, floor)
                , DataManager.printPassengersOnTheFloor(floor)));
        System.out.println("");
    }
}
