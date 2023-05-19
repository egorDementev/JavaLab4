package com.example.javalab4.view;

import com.example.javalab4.application.Request;
import com.example.javalab4.viewModel.ElevatorsViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class ElevatorsView {
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


    public void init(ElevatorsViewModel viewModel) {
        this.viewModel = viewModel;

        roof.setY(viewModel.getRoofY());

        lift1.setLayoutY(viewModel.getRoofY() + 70);
        lift1.setPrefHeight(viewModel.getHeight());
        lift1.setMaxHeight(viewModel.getHeight());

        lift2.setLayoutY(viewModel.getRoofY() + 70);
        lift2.setPrefHeight(viewModel.getHeight());
        lift2.setMaxHeight(viewModel.getHeight());

        viewModel.fillMine(mine1);
        viewModel.fillNumbers(numbers1);

        viewModel.fillMine(mine2);
        viewModel.fillNumbers(numbers2);

        elevator1.layoutYProperty().bindBidirectional(viewModel.getFirstElevatorYProperty());
        elevator2.layoutYProperty().bindBidirectional(viewModel.getSecondElevatorYProperty());

        requestListView.itemsProperty().bind(viewModel.getListProperty());
        requestListView.setCellFactory(requestListView -> viewModel.getCellFactory());
    }

    public void onStartSimulatingClicked() {
        viewModel.startSimulating();
    }
}
