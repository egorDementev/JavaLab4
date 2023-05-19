package com.example.javalab4.viewModel;

import com.example.javalab4.Application;
import com.example.javalab4.application.Request;
import com.example.javalab4.application.ViewHandler;
import com.example.javalab4.model.SimulatorModel;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * Класс является прослойкой между View и Model, отправляет полученные данные в модель, получает изменения и отправляет их на View
 */
@Getter
@Setter
public class ElevatorsViewModel {
    private final ListProperty<Request> listProperty = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final DoubleProperty firstElevatorYProperty = new SimpleDoubleProperty(0);
    private final DoubleProperty secondElevatorYProperty = new SimpleDoubleProperty(0);
    private final SimulatorModel model;
    private final ViewHandler viewHandler;

    /**
     * Конструктор класса, связывает его с модельнб и вью хендлером
     * @param model модель, где происходит управление потоками
     * @param viewHandler вью хендлер, в котором происходит управление вьюшками
     */
    public ElevatorsViewModel(SimulatorModel model, ViewHandler viewHandler) {
        this.model = model;
        this.viewHandler = viewHandler;
    }

    /**
     * Функция рассчитывает положение крыши, в зависимости от количества этажей в доме
     * @return координату У у крыши
     */
    public double getRoofY() {
        return 600 - 100 - 25 * model.getFloors();
    }

    /**
     * Функция рассчитывает ваысоту дома (без крыши), в зависимости от количества этажей
     * @return высоту дома
     */
    public double getHeight() {
        return 24.5 * model.getFloors();
    }

    /**
     * Функция заполняет VBox картинками этажей
     * @param mine объект интерфейса, который отвечает за отображение этажей и шахты лифта
     */
    public void fillMine(VBox mine) {
        Image image = new Image(Objects.requireNonNull(Application.class.getResourceAsStream("img/floor.png")));
        for (int i = 0; i < model.getFloors(); i++) {
            mine.getChildren().add(new ImageView(image));
        }
    }

    /**
     * Функция заполняет VBox номерами этажей
     * @param numbers объект интерфейса, который отвечает за отображение номеров каждого этаджа
     */
    public void fillNumbers(VBox numbers) {
        for (int i = 0; i < model.getFloors(); i++)
            numbers.getChildren().add(createLabel(model.getFloors() - i));
    }

    /**
     * Функция создает Label с текстом, соответствующем номеру этажа
     * @param number номер этажа
     * @return Label, отображающий номер этажа
     */
    private Label createLabel(Integer number) {
        Label label = new Label("" + number);
        label.setStyle("-fx-font-family: 'Poppins SemiBold'");
        label.setStyle("-fx-font-size: 17");
        label.setStyle("-fx-text-fill: #FFFFFF");
        label.setPrefHeight(24.5);
        label.setMaxHeight(24.5);
        label.setPrefWidth(50);
        label.setAlignment(Pos.CENTER);
        return label;
    }

    /**
     * Метод вызывает в модели функцию запуска симуляции
     */
    public void startSimulating(){
        model.startSimulating(firstElevatorYProperty, secondElevatorYProperty, listProperty);
    }

    /**
     * Метод определяет, как будет выглядеть каждая ячейка ListCell листа с запросами
     * @return фабрику ячеек
     */
    public ListCell<Request> getCellFactory() {
        return new ListCell<>() {
            private final Label label = new Label();
            @Override
            protected void updateItem(Request item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    Platform.runLater(() -> setGraphic(null));
                } else {
                    Platform.runLater(() -> label.setText(prepareRequestToBeShown(item)));
                    Platform.runLater(() -> label.setPrefWidth(330));
                    Platform.runLater(() -> label.setAlignment(Pos.CENTER));
                    Platform.runLater(() -> setGraphic(label));
                }
            }
        };
    }

    /**
     * Функция подготавливает строку для отображения важой информации о заявке
     * @param request запрос на лифт
     * @return строка, с указанием, с какого этажи и на какой нужно проехать, а так же, какой лифт взял ее в работу
     */
    private String prepareRequestToBeShown(Request request) {
        return request.getStartFloor() + " --> " + request.getEndFloor() + "    lift: " +
                (request.getElevatorNumber() != 0 ? request.getElevatorNumber() : "no");
    }
}
