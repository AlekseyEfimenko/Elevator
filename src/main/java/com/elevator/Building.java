package com.elevator;

import com.utils.DataManager;
import org.apache.log4j.Logger;
import java.util.List;

public class Building {
    private static final int START_FLOOR = 1;
    private static final Logger LOGGER = Logger.getLogger(Building.class);
    private final int floorCounts;
    private final List<Floor> floors;
    private final Elevator elevator;

    public Building(int min, int max) {
        LOGGER.info("Creating new building");
        LOGGER.info("Generating random amount of floors");
        floorCounts = DataManager.getRandomNumber(min, max);
        LOGGER.info(String.format("The amount of floors set to %1$d", floorCounts));
        floors = DataManager.setFloors(floorCounts);
        elevator = new Elevator();
    }

    public int getFloorCounts() {
        return floorCounts;
    }

    public static int getStartFloor() {
        return START_FLOOR;
    }

    public List<Floor> getFloors() {
        return floors;
    }

    public Elevator getElevator() {
        return elevator;
    }
}
