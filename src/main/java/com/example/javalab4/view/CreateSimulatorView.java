package com.example.javalab4.view;

import com.example.javalab4.viewModel.CreateSimulatorViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

public class CreateSimulatorView {
    @FXML private TextField numberOfFloors;
    @FXML private TextField requestsTime;
    @FXML private Label errorMessage;
    private CreateSimulatorViewModel viewModel;

    public void init(CreateSimulatorViewModel viewModel) {
        this.viewModel = viewModel;

        numberOfFloors.textProperty().bindBidirectional(viewModel.getNumberOfFloorsProperty());
        requestsTime.textProperty().bindBidirectional(viewModel.getNumberOfMilliseconds());
        errorMessage.textProperty().bind(viewModel.getErrorMessageProperty());
    }

    @FXML protected void onCreateSimulatorClicked() throws IOException {
        viewModel.openSimulator();
    }
}