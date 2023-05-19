package com.example.javalab4.application;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ListProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Класс лифта, соджержит всю информацию про лифт и функции его упаравления
 */
@Getter
public class Elevator {
    private final Integer numberOfLift;
    private final ArrayList<Request> targetList;
    private Integer currentFloor;
    private final DoubleProperty layoutY;
    private final ListProperty<Request> listProperty;
    @Setter
    private Integer direction;


    /**
     * Конструктор класса
     * @param numberOfLift номер лифта (1 или 2)
     * @param numberOfFloors количество этажей в здании
     * @param y свойство, которое отвечает за У координату лифта
     * @param listProperty свойство, которое отвечает за список запросов на работу лифта
     */
    public Elevator(Integer numberOfLift, Integer numberOfFloors, DoubleProperty y, ListProperty<Request> listProperty) {
        this.numberOfLift = numberOfLift;
        this.targetList = new ArrayList<>();
        this.currentFloor = numberOfFloors;
        this.layoutY = y;
        this.listProperty = listProperty;
        direction = 0;

    }

    /**
     * Проверка, пустой ли список целей (этажей, куда лифту надо проехать)
     * @return переменная типа Boolean
     */
    public Boolean isEmpty() {
        return targetList.size() == 0;
    }

    /**
     * Метод добавляет новую цель для лифта и сортирует этот список в зависимости от направления движения
     * @param target запрос на поездку на лифте
     */
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

    /**
     * Метод вызывается, когда цель достигнута, и удаляет ее из списка
     */
    public void deleteTarget() {
        Request el = targetList.get(0);
        Platform.runLater(() -> listProperty.remove(el));
        targetList.remove(0);
        if (isEmpty()) direction = 0;
    }

    /**
     * Метод изменяет текущее положение лифта
     * @param number значение, на которое надо изменить положение лифта (1 или -1)
     */
    public void changeCurrentFloor(Integer number) {
        currentFloor += number;
    }
}
