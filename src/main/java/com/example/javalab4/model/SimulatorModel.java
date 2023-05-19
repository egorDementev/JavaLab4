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

/**
 * Класс модели, отвечает за всю "бизнес-логику" приложения
 */
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

    /**
     * Заполняет необходимые поля класса
     * @param numberOfFloors количество этажей в доме, которое ввел пользователь
     * @param numberOfMilliseconds интервал генерации заявки на лифт
     */
    public void fillData(Integer numberOfFloors, Integer numberOfMilliseconds) {
        floors = numberOfFloors;
        requestTime = numberOfMilliseconds;
        executor = Executors.newFixedThreadPool(2);
    }

    /**
     * Метод срабатывает при запуске симуляции лифтов
     * @param first свойство У координаты первого лифта
     * @param second свойство У координаты второго лифта
     * @param listProperty свойство, которое отвечает за список заявок на работу лифта
     */
    public void startSimulating(DoubleProperty first, DoubleProperty second, ListProperty<Request> listProperty) {
        this.listProperty = listProperty;

        executor = Executors.newFixedThreadPool(2);
        elevator1 = new Elevator(1, floors, first, listProperty);
        elevator2 = new Elevator(2, floors, second, listProperty);
        
        startRequestFlow();
    }

    /**
     * Метод создает и запускает поток на генерацию заявок для лифта
     */
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

                    checkRequests(start, end);
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Поток завершен");
        });
        thread.start();
    }

    /**
     * Мкетод осуществляет проверку всех заявок излиста ожидания на то, может ли какой-то лифт ее взять, после чего собирает новую заявку из случайных чисел
     * @param start начальный этаж
     * @param end конечный этаж
     * @throws InterruptedException вылетает при проверке, спит ли этот поток (если да, то выбрасывается исключение)
     */
    private void checkRequests(Integer start, Integer end) throws InterruptedException {
        synchronized (waitingList) {
            for (Request req : waitingList) {
                distributionRequest(req, true);
            }
        }
        distributionRequest(buildRequest(start, end), false);
        Thread.sleep(requestTime);
    }

    /**
     * Метод рассчитывает время, необходимое лифту на обработку заявки, выбирает наилучшый лифт и либо добавляет к его задачам эту, либо если лифт свободен, то создает новый ElevatorTask. Однако есои ни один лифт не может взять эту заявку, то она отправляется в лист ожиданий
     * @param request запрос пользователя
     * @param isInList это новый запрос или взял из листа ожидания
     */
    private void distributionRequest(Request request, Boolean isInList) {
        int time1 = Math.abs(elevator1.getCurrentFloor() - request.getStartFloor()) + 
                Math.abs(request.getStartFloor() - request.getEndFloor());
        int time2 = Math.abs(elevator2.getCurrentFloor() - request.getStartFloor()) + 
                Math.abs(request.getStartFloor() - request.getEndFloor());

        if (elevator1.isEmpty() && elevator2.isEmpty()) {
            createNewTask(time2 >= time1 ? elevator1 : elevator2, request);
            if (isInList) removeRequest(request);
        }
        else {
            int direction = request.getEndFloor() > request.getStartFloor() ? 1 : -1;
            if (chooseCorrectLift(request, time1, time2, direction, elevator1, elevator2)) {
                if (isInList) removeRequest(request);
                return;
            }
            if (chooseCorrectLift(request, time1, time2, direction, elevator2, elevator1)) {
                if (isInList) removeRequest(request);
                return;
            }

            if (!isInList)
                synchronized (waitingList) {
                    waitingList.add(request);
                }
        }
    }

    /**
     * Если лифт взял заявку из листа ожидания, то она удаляется от туда
     * @param request заявка
     */
    private void removeRequest(Request request) {
        synchronized (waitingList) {
            waitingList.remove(request);
        }
    }

    /**
     * На основе данных о заявке, метод выбирает, эсли это возможно, к какому лифту ее отнести
     * @param request заявка на передвижение на лифте
     * @param time1 время, необходимое первому лифту для выполнения заявки
     * @param time2 время, необходимое второму лифту для выполнения заявки
     * @param direction направление движения
     * @param elevator1 первый лифт
     * @param elevator2 второй лифт
     * @return true, если заявка была назначана какому-то из лифтов, и false в обратном случае
     */
    private boolean chooseCorrectLift(Request request, int time1, int time2, int direction, Elevator elevator1, Elevator elevator2) {
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

    /**
     * Метод создает новую задачу для лифта, который сейчас не двигается
     * @param elevator сводобный лифт
     * @param request заявка на передвижение по лифту
     */
    private void createNewTask(Elevator elevator, Request request) {
        System.out.println("create");
        elevator.setDirection(request.getEndFloor() > request.getStartFloor() ? 1 : -1);
        request.setElevatorNumber(elevator.getNumberOfLift());
        elevator.addTarget(request);
        ElevatorTask elevatorTask = new ElevatorTask(elevator, 50, floors);
        executor.submit(elevatorTask);
    }

    /**
     * Метод добавляет заявку к уже работающему лифту
     * @param elevator лифт, который находится в движении
     * @param request заявка на передвижение по лифту
     */
    private void addTargets(Elevator elevator, Request request) {
        System.out.println("add");
        request.setElevatorNumber(elevator.getNumberOfLift());
        elevator.addTarget(request);
    }

    /**
     * Метод создает заявку
     * @param start начальный этаж
     * @param end конечный этаж
     * @return заявку на передвижение по лифту
     */
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
