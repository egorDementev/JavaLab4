package com.example.javalab4.application;

import com.example.javalab4.viewModel.ElevatorsViewModel;
import javafx.beans.property.DoubleProperty;

public class Elevator implements Runnable {
    private final DoubleProperty layoutY;
    private final Integer numberOfFloors;
    private Integer currentFloor;
    private final Integer speed;

    public Elevator(Integer speed, Integer floor, Integer floors, DoubleProperty property) {
        this.speed = speed;
        this.currentFloor = floor;
        this.numberOfFloors = floors;
        this.layoutY = property;
    }

    public double getCoordinate() {
        return (24.5 * numberOfFloors) - (currentFloor * 24.5);
    }

    @Override
    public void run() {
        try {
            int destination = 1;
            while (true) {
                if (currentFloor == 1 || currentFloor.equals(numberOfFloors)) {
                    destination *= -1;
                }
                currentFloor += destination;
                Thread.sleep(speed);
                layoutY.set(getCoordinate());
                System.out.println("floor" + currentFloor);
            }

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
