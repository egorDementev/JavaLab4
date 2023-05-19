package com.example.javalab4.application;

import com.example.javalab4.model.SimulatorModel;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Класс отвечает за запуск приложения: создание модели, ViewModelProvider и viewHandler
 */
public class StartApplication {

    public static void start(Stage stage) throws IOException {
        System.out.println("Starting the application ...");

        SimulatorModel model = new SimulatorModel();

        ViewModelProvider viewModelProvider = new ViewModelProvider(model);
        ViewHandler viewHandler = new ViewHandler(viewModelProvider, stage);

        viewHandler.start();
    }

}
