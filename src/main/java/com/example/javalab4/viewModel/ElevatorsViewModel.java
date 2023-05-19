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

    public ElevatorsViewModel(SimulatorModel model, ViewHandler viewHandler) {
        this.model = model;
        this.viewHandler = viewHandler;
    }

    public double getRoofY() {
        return 600 - 100 - 25 * model.getFloors();
    }

    public double getHeight() {
        return 24.5 * model.getFloors();
    }

    public void fillMine(VBox mine) {
        Image image = new Image(Objects.requireNonNull(Application.class.getResourceAsStream("img/floor.png")));
        for (int i = 0; i < model.getFloors(); i++) {
            mine.getChildren().add(new ImageView(image));
        }
    }

    public void fillNumbers(VBox numbers) {
        for (int i = 0; i < model.getFloors(); i++)
            numbers.getChildren().add(createLabel(model.getFloors() - i));
    }

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

    public void startSimulating(){
        model.startSimulating(firstElevatorYProperty, secondElevatorYProperty, listProperty);
    }

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
                    Platform.runLater(() -> label.setPrefWidth(300));
                    Platform.runLater(() -> label.setAlignment(Pos.CENTER));
                    Platform.runLater(() -> setGraphic(label));
                }
            }
        };
    }

    private String prepareRequestToBeShown(Request request) {
        return request.getStartFloor() + " --> " + request.getEndFloor() + "    list: " +
                (request.getElevatorNumber() != 0 ? request.getElevatorNumber() : "no");
    }
}
