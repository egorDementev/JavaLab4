package com.example.javalab4;

import com.example.javalab4.application.StartApplication;
import javafx.stage.Stage;

import java.io.IOException;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        StartApplication.start(stage);
    }

    public static void main(String[] args) {
        launch();
    }
}