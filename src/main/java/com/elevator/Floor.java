package com.elevator;

import static com.utils.FileManager.getInstance;
import com.utils.DataManager;
import org.apache.log4j.Logger;
import java.util.List;

public class Floor {
    private static final int MIN_AMOUNT_OF_PASSENGERS = Integer.parseInt(getInstance().getProperties("minPassengers"));
    private static final int MAX_AMOUNT_OF_PASSENGERS = Integer.parseInt(getInstance().getProperties("maxPassengers"));
    private static final Logger LOGGER = Logger.getLogger(Floor.class);
    private final int floorNumber;
    private String directionOfMostPassengers;
    private int passengerCounts;
    private final List<Passenger> passengersOnFloor;

    public Floor(int floorNumber, int floorCounts) {
        this.floorNumber = floorNumber;
        LOGGER.info(String.format("Generating random amounts of passenger on the %1$d floor", this.floorNumber));
        passengerCounts = DataManager.getRandomNumber(MIN_AMOUNT_OF_PASSENGERS, MAX_AMOUNT_OF_PASSENGERS);
        passengersOnFloor = DataManager.setPassengers(passengerCounts, floorNumber, floorCounts);
    }

    public int getPassengerCounts() {
        return  passengerCounts;
    }

    public List<Passenger> getPassengersOnFloor() {
        return passengersOnFloor;
    }

    public void addPassenger(Passenger passenger) {
        passengersOnFloor.add(passenger);
        passengerCounts++;
        LOGGER.info(String.format("The current amounts of passengers on the floor is %1$d", passengerCounts));
    }

    public void removePassenger(Passenger passenger) {
        passengersOnFloor.remove(passenger);
        passengerCounts--;
        LOGGER.info(String.format("The current amounts of passengers on the floor is %1$d", passengerCounts));
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public static int getMaxAmountOfPassengers() {
        return MAX_AMOUNT_OF_PASSENGERS;
    }

    public boolean isFreeSpaceAvailable() {
        return passengerCounts < MAX_AMOUNT_OF_PASSENGERS;
    }

    public String getDirectionOfMostPassengers() {
        setDirectionOfMostPassengers();
        return directionOfMostPassengers;
    }

    private void setDirectionOfMostPassengers() {
        directionOfMostPassengers = DataManager.setDirection(passengersOnFloor);
    }
}
