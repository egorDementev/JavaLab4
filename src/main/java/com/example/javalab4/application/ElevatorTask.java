package com.example.javalab4.application;

import lombok.Setter;

import java.util.Objects;

/**
 * Класс Runnable, который отраджает в себе задачу работы одного потока (пока у лифта список целей не пустой, задача выполняется)
 */
@Setter
public class ElevatorTask implements Runnable {
    private final Integer numberOfFloors;
    private final Integer speed;
    private final Elevator elevator;

    /**
     * @param elevator объект класса Elevator (лифт)
     * @param speed скорость передвиженря лифта
     * @param floors количество этажей в здании
     */
    public ElevatorTask(Elevator elevator, Integer speed, Integer floors) {
        this.speed = speed;
        this.numberOfFloors = floors;
        this.elevator = elevator;
    }

    /**
     * Функция расчитывает координаты текущего положения лифта
     * @return У координату
     */
    private double getCoordinate() {
        return (24.5 * numberOfFloors) - (elevator.getCurrentFloor() * 24.5);
    }

    /**
     * Метод описывает работу задачи: пока лифт не пустой, лифт делает один шаг в сторону самой первой цели, если мы дошли до цели, то лифт дополнительно останавливается на 300мс
     */
    @Override
    public void run() {
        try {

            while (!elevator.isEmpty()) {
                Request request = elevator.getTargetList().get(0);
                int destination = elevator.getCurrentFloor() >
                        (request.isInWork() ? request.getEndFloor() : request.getStartFloor()) ? -1 : 1;

                changeCoordinates(destination);
                checkDone(request);
            }

            elevator.setDirection(0);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Метод изменяет координаты лифта и его текущий этаж (тоесть выполняет перемещение лифта на 1 этаж)
     * @param destination направление движения лифта
     * @throws InterruptedException вылетает при проверке, спит ли этот поток (если да, то выбрасывается исключение)
     */
    private void changeCoordinates(Integer destination) throws InterruptedException {
        elevator.changeCurrentFloor(destination);
        elevator.getLayoutY().set(getCoordinate());
        Thread.sleep(speed);
    }

    /**
     * Метод проверяет, достиг ли лифт этажа, где нужно посадить или высадить человека
     * @param request заявка на перемещение между этажами
     * @throws InterruptedException вылетает при проверке, спит ли этот поток (если да, то выбрасывается исключение)
     */
    private void checkDone(Request request) throws InterruptedException {
        if (Objects.equals(elevator.getCurrentFloor(), request.isInWork() ? request.getEndFloor() : request.getStartFloor())) {
            Thread.sleep(300);
            if (request.isInWork())
                elevator.deleteTarget();
            else request.setInWork(true);
        }
    }
}
