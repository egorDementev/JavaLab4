package com.example.javalab4.application;

import lombok.Setter;

import java.util.Objects;

@Setter
public class ElevatorTask implements Runnable {
    private final Integer numberOfFloors;
    private final Integer speed;
    private final Elevator elevator;

    public ElevatorTask(Elevator elevator, Integer speed, Integer floors) {
        this.speed = speed;
        this.numberOfFloors = floors;
        this.elevator = elevator;
    }

    public double getCoordinate() {
        return (24.5 * numberOfFloors) - (elevator.getCurrentFloor() * 24.5);
    }

    @Override
    public void run() {
        try {
            while (!elevator.isEmpty()) {
                Request request = elevator.getTargetList().get(0);
                int destination = elevator.getCurrentFloor() >
                        (request.isInWork() ? request.getEndFloor() : request.getStartFloor()) ? -1 : 1;
                elevator.changeCurrentFloor(destination);
                Thread.sleep(speed);
                elevator.getLayoutY().set(getCoordinate());

                if (Objects.equals(elevator.getCurrentFloor(), request.isInWork() ? request.getEndFloor() : request.getStartFloor())) {
                    Thread.sleep(300);
                    if (request.isInWork())
                        elevator.deleteTarget();
                    else request.setInWork(true);
                }
            }

            elevator.setDirection(0);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
