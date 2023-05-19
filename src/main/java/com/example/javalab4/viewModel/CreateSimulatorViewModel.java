package com.example.javalab4.viewModel;

import com.example.javalab4.application.ViewHandler;
import com.example.javalab4.model.SimulatorModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;

import java.io.IOException;

/**
 * Класс является прослойкой между View и Model, отправляет полученные данные в модель, получает изменения и отправляет их на View
 */
@Getter
public class CreateSimulatorViewModel {
    private final StringProperty numberOfFloorsProperty = new SimpleStringProperty();
    private final StringProperty numberOfMilliseconds = new SimpleStringProperty();
    private final StringProperty errorMessageProperty = new SimpleStringProperty();
    private final SimulatorModel model;
    private final ViewHandler viewHandler;

    /**
     * Конструктор класса, связывает его с модельнб и вью хендлером
     * @param model модель, где происходит управление потоками
     * @param viewHandler вью хендлер, в котором происходит управление вьюшками
     */
    public CreateSimulatorViewModel(SimulatorModel model, ViewHandler viewHandler) {
        this.model = model;
        this.viewHandler = viewHandler;
    }

    /**
     * Метод передает данные, полученные от пользователя, в модель и открывает вью с симулятором лифта
     * @throws IOException ошибка при открытии вью
     */
    public void openSimulator() throws IOException {
        if (!isCorrect()) { errorMessageProperty.set("Your data are incorrect!!");}
        else {
            model.fillData(Integer.parseInt(numberOfFloorsProperty.getValue()),
                    Integer.parseInt(numberOfMilliseconds.getValue()));
            viewHandler.openSimulator();
        }
    }

    /**
     * Метод проверяет, корректна ли информация, введенная пользователем
     * @return переменная типа Boolean
     */
    private Boolean isCorrect() {
        try {
            int number = Integer.parseInt(numberOfFloorsProperty.getValue());
            int seconds = Integer.parseInt(numberOfMilliseconds.getValue());
            return number >= 10 && number <= 20 && seconds >= 500 && seconds <= 5000;
        }
        catch (RuntimeException e) {
            return false;
        }
    }
}
