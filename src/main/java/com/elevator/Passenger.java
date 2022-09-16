package com.elevator;

public class Passenger {
    private int currentFloor;
    private int destinationFloor;

    public Passenger(int currentFloor, int destinationFloor) {
        this.currentFloor = currentFloor;
        this.destinationFloor = destinationFloor;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public void setCurrentFloor(int floor) {
        currentFloor = floor;
    }

    public int getDestinationFloor() {
        return destinationFloor;
    }

    public void setDestinationFloor(int floor) {
        destinationFloor = floor;
    }
}
