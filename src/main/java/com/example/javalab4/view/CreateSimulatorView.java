package com.example.javalab4.view;

import com.example.javalab4.viewModel.CreateSimulatorViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

/**
 * Класс выступает в роли контроллера для окна симуляции работы лифтов, управляет поведением и отображением элементов на экране
 */
public class CreateSimulatorView {
    @FXML private TextField numberOfFloors;
    @FXML private TextField requestsTime;
    @FXML private Label errorMessage;
    private CreateSimulatorViewModel viewModel;

    /**
     * Инициализация класса, связывает его с вью модел и байндит все необходимые проперти
     * @param viewModel вью модель
     */
    public void init(CreateSimulatorViewModel viewModel) {
        this.viewModel = viewModel;

        numberOfFloors.textProperty().bindBidirectional(viewModel.getNumberOfFloorsProperty());
        requestsTime.textProperty().bindBidirectional(viewModel.getNumberOfMilliseconds());
        errorMessage.textProperty().bind(viewModel.getErrorMessageProperty());
    }

    /**
     * Метод срабатывает при нажатии на кнопку и вызывает функцию открытия нового вью
     * @throws IOException выбрасывается при неудачном открытии окна
     */
    @FXML protected void onCreateSimulatorClicked() throws IOException {
        viewModel.openSimulator();
    }
}