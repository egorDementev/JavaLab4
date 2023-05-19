package com.example.javalab4;

import com.example.javalab4.model.SimulatorModel;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class StartApplication extends Application {
    public SimulatorModel model;
    @Override
    public void start(Stage stage) throws IOException {
        model = new SimulatorModel(stage);

    }

    public static void main(String[] args) {
        launch();
    }
}