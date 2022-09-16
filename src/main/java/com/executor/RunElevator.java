package com.executor;

import static com.utils.FileManager.getInstance;
import com.elevator.Building;
import org.apache.log4j.Logger;

public class RunElevator {
    private static final int MIN_AMOUNT_OF_FLOORS = Integer.parseInt(getInstance().getProperties("minFloor"));
    private static final int MAX_AMOUNT_OF_FLOORS = Integer.parseInt(getInstance().getProperties("maxFloor"));
    private static final Logger LOGGER = Logger.getLogger(RunElevator.class);

    public static void main(String[] args) {
        Building building = new Building(MIN_AMOUNT_OF_FLOORS, MAX_AMOUNT_OF_FLOORS);
        for (int i = 1; i < 1000; i++) {
            LOGGER.info(String.format("%1$s  step: %2$d  %1$s", "*".repeat(20), i));
            building.getElevator().move(building, i);
        }
    }
}
