package com.example.javalab4.model;

import com.example.javalab4.application.Elevator;
import com.example.javalab4.application.ElevatorTask;
import com.example.javalab4.application.Request;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ListProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Getter
@NoArgsConstructor
public class SimulatorModel {
    private Integer floors;
    private Integer requestTime;
    private ExecutorService executor;
    private Elevator elevator1;
    private Elevator elevator2;
    private final CopyOnWriteArrayList<Request> waitingList = new CopyOnWriteArrayList<>();
    private ListProperty<Request> listProperty;

    public void fillData(Integer numberOfFloors, Integer numberOfMilliseconds) {
        floors = numberOfFloors;
        requestTime = numberOfMilliseconds;
        executor = Executors.newFixedThreadPool(2);
    }


    public void startSimulating(DoubleProperty first, DoubleProperty second, ListProperty<Request> listProperty) {
        this.listProperty = listProperty;

        executor = Executors.newFixedThreadPool(2);
        elevator1 = new Elevator(1, floors, first, listProperty);
        elevator2 = new Elevator(2, floors, second, listProperty);
        
        startRequestFlow();
    }

    private void startRequestFlow() {
        Thread thread = new Thread(() -> {
            System.out.println("Поток запущен");
            try {
                Random random = new Random();
                while (true) {
                    int start = random.nextInt(1, floors + 1);
                    int end = random.nextInt(1, floors + 1);
                    while (start == end) {
                        end = random.nextInt(1, floors + 1);
                    }
                    synchronized (waitingList) {
                        for (Request req : waitingList) {
                            checkRequest(req, true);
                        }
                    }
                    checkRequest(buildRequest(start, end), false);
                    Thread.sleep(requestTime);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Поток завершен");
        });
        thread.start();
    }

    private void checkRequest(Request request, Boolean isInList) {
        int time1 = Math.abs(elevator1.getCurrentFloor() - request.getStartFloor()) + 
                Math.abs(request.getStartFloor() - request.getEndFloor());
        int time2 = Math.abs(elevator2.getCurrentFloor() - request.getStartFloor()) + 
                Math.abs(request.getStartFloor() - request.getEndFloor());

        if (elevator1.isEmpty() && elevator2.isEmpty()) {
            createNewTask(time2 >= time1 ? elevator1 : elevator2, request);
            if (isInList)
                removeRequest(request);
        }
        else {
            int direction = request.getEndFloor() > request.getStartFloor() ? 1 : -1;
            if (cheeck(request, time1, time2, direction, elevator1, elevator2)) {
                if (isInList)
                    removeRequest(request);
                return;
            }
            if (cheeck(request, time1, time2, direction, elevator2, elevator1)) {
                if (isInList)
                    removeRequest(request);
                return;
            }

            if (!isInList)
                synchronized (waitingList) {
                    waitingList.add(request);
                }
        }
    }

    private void removeRequest(Request request) {
        synchronized (waitingList) {
            waitingList.remove(request);
        }
    }

    private boolean cheeck(Request request, int time1, int time2, int direction, Elevator elevator1, Elevator elevator2) {
        if ((time2 >= time1 ? elevator1 : elevator2).getDirection() == direction) {
            int cur = (time2 >= time1 ? elevator1 : elevator2).getCurrentFloor();
            if ((direction == 1 && request.getStartFloor() >= cur) || 
                    (direction == -1 && request.getStartFloor() <= cur)) {
                addTargets(time2 >= time1 ? elevator1 : elevator2, request);
                return true;
            }
        }
        if ((time2 >= time1 ? elevator1 : elevator2).isEmpty()) {
            createNewTask(time2 >= time1 ? elevator1 : elevator2, request);
            return true;
        }
        return false;
    }

    private void createNewTask(Elevator elevator, Request request) {
        System.out.println("create");
        elevator.setDirection(request.getEndFloor() > request.getStartFloor() ? 1 : -1);
        request.setElevatorNumber(elevator.getNumberOfLift());
        elevator.addTarget(request);
        ElevatorTask elevatorTask = new ElevatorTask(elevator, 50, floors);
        executor.submit(elevatorTask);
    }

    private void addTargets(Elevator elevator, Request request) {
        System.out.println("add");
        request.setElevatorNumber(elevator.getNumberOfLift());
        elevator.addTarget(request);
    }

    private Request buildRequest(Integer start, Integer end) {
        Request request = new Request();
        System.out.println(start + " " + end);
        request.setStartFloor(start);
        request.setEndFloor(end);
        request.setInWork(false);
        request.setElevatorNumber(0);
        listProperty.add(request);
        return request;
    }
}
