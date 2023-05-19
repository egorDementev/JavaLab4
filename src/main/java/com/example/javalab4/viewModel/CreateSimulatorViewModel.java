package com.example.javalab4.viewModel;

import com.example.javalab4.application.ViewHandler;
import com.example.javalab4.model.SimulatorModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;

import java.io.IOException;

@Getter
public class CreateSimulatorViewModel {
    private final StringProperty numberOfFloorsProperty = new SimpleStringProperty();
    private final StringProperty numberOfMilliseconds = new SimpleStringProperty();
    private final StringProperty errorMessageProperty = new SimpleStringProperty();
    private final SimulatorModel model;
    private final ViewHandler viewHandler;

    public CreateSimulatorViewModel(SimulatorModel model, ViewHandler viewHandler) {
        this.model = model;
        this.viewHandler = viewHandler;
    }

    public void openSimulator() throws IOException {
        if (!isCorrect()) { errorMessageProperty.set("Your data are incorrect!!");}
        else {
            model.fillData(Integer.parseInt(numberOfFloorsProperty.getValue()),
                    Integer.parseInt(numberOfMilliseconds.getValue()));
            viewHandler.openSimulator();
        }
    }

    private Boolean isCorrect() {
        try {
            int number = Integer.parseInt(numberOfFloorsProperty.getValue());
            int seconds = Integer.parseInt(numberOfMilliseconds.getValue());
            return number >= 15 && number <= 20 && seconds >= 500 && seconds <= 5000;
        }
        catch (RuntimeException e) {
            return false;
        }
    }
}
