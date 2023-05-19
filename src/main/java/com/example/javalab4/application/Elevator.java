package com.example.javalab4.application;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ListProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

@Getter
public class Elevator {
    private final Integer numberOfLift;
    private final ArrayList<Request> targetList;
    private Integer currentFloor;
    private final DoubleProperty layoutY;
    private final ListProperty<Request> listProperty;
    @Setter
    private Integer direction;


    public Elevator(Integer numberOfLift, Integer numberOfFloors, DoubleProperty y, ListProperty<Request> listProperty) {
        this.numberOfLift = numberOfLift;
        this.targetList = new ArrayList<>();
        this.currentFloor = numberOfFloors;
        this.layoutY = y;
        this.listProperty = listProperty;
        direction = 0;

    }

    public Boolean isEmpty() {
        return targetList.size() == 0;
    }

    public void addTarget(Request target) {
        targetList.add(target);
        Comparator<Request> comparator = Comparator.comparing(Request::isInWork)
                .thenComparing(request -> request.isInWork() ? request.getEndFloor() : request.getStartFloor());
        if (direction == -1) {
            comparator = comparator.reversed();
        }
        targetList.sort(comparator);
        System.out.println(numberOfLift + "   " + Arrays.toString(new ArrayList[]{targetList}));
    }

    public void deleteTarget() {
        Request el = targetList.get(0);
        Platform.runLater(() -> listProperty.remove(el));
        targetList.remove(0);
        if (isEmpty()) direction = 0;
    }

    public void changeCurrentFloor(Integer number) {
        currentFloor += number;
    }
}
