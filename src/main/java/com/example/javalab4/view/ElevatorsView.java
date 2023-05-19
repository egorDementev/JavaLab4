package com.example.javalab4.view;

import com.example.javalab4.application.Request;
import com.example.javalab4.viewModel.ElevatorsViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * Класс выступает в роли контроллера для стартового окна приложения, управляет поведением и отображением элементов на экране
 */
public class ElevatorsView {
    @FXML private Button startButton;
    @FXML private ImageView roof;
    @FXML private StackPane lift1;
    @FXML private StackPane lift2;
    @FXML private VBox mine1;
    @FXML private VBox mine2;
    @FXML private VBox numbers1;
    @FXML private VBox numbers2;
    @FXML private ImageView elevator1;
    @FXML private ImageView elevator2;
    @FXML private ListView<Request> requestListView;
    private ElevatorsViewModel viewModel;

    /**
     * Инициализация класса и вью, в методе происходит привязка к ElevatorsViewModel, bind property, и заполнение элементов интерфейса
     * @param viewModel вью модель
     */
    public void init(ElevatorsViewModel viewModel) {
        this.viewModel = viewModel;

        // установка координаты для крыши дома
        roof.setY(viewModel.getRoofY());

        // установка координат и высоты для первой шахты
        lift1.setLayoutY(viewModel.getRoofY() + 70);
        lift1.setPrefHeight(viewModel.getHeight());
        lift1.setMaxHeight(viewModel.getHeight());

        // установка координат и высоты для второй шахты
        lift2.setLayoutY(viewModel.getRoofY() + 70);
        lift2.setPrefHeight(viewModel.getHeight());
        lift2.setMaxHeight(viewModel.getHeight());

        // заполнение шахты этажами и их номерами
        viewModel.fillMine(mine1);
        viewModel.fillNumbers(numbers1);

        viewModel.fillMine(mine2);
        viewModel.fillNumbers(numbers2);

        // связка координат лифтов и проперти во вью модел
        elevator1.layoutYProperty().bindBidirectional(viewModel.getFirstElevatorYProperty());
        elevator2.layoutYProperty().bindBidirectional(viewModel.getSecondElevatorYProperty());

        // связка элементов листа заявок со свойством во вью модели и установка фабрики ячеек
        requestListView.itemsProperty().bind(viewModel.getListProperty());
        requestListView.setCellFactory(requestListView -> viewModel.getCellFactory());
    }

    /**
     * Метод срабатывает при нажатии на кнопку, запускает процесс симуляции лифтов
     */
    @FXML protected void onStartSimulatingClicked() {
        viewModel.startSimulating();
        startButton.setDisable(true);
    }
}
