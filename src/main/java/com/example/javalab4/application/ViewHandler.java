package com.example.javalab4.application;

import com.example.javalab4.view.CreateSimulatorView;
import com.example.javalab4.view.ElevatorsView;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Getter;

import java.io.IOException;

/**
 * Класс осуществляет переключение между вью
 */
@Getter
public class ViewHandler {
    private final Stage stage;
    private final ViewModelProvider viewModelProvider;

    public ViewHandler(ViewModelProvider viewModelProvider, Stage stage) {
        this.viewModelProvider = viewModelProvider;
        this.stage = stage;
        viewModelProvider.instantiateViewModels(this);
    }

    /**
     * Открытие стартового окна приложения
     * @throws IOException если возникают проблемы с закрузкой фхмл файла
     */
    public void start() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(CreateSimulatorView.class.getResource("create-simulator-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 600);

        CreateSimulatorView view = fxmlLoader.getController();
        view.init(viewModelProvider.getCreateSimulatorViewModel());

        stage.setTitle("Lift Simulator!");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Открытие окна симуляции работы лифта
     * @throws IOException если возникают проблемы с закрузкой фхмл файла
     */
    public void openSimulator() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ElevatorsView.class.getResource("elevators-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 600);

        ElevatorsView view = fxmlLoader.getController();
        view.init(viewModelProvider.getElevatorsViewModel());

        stage.setTitle("Lift Simulator!");
        stage.setScene(scene);
        stage.show();
    }
}
