package com.example.javalab4.application;

import com.example.javalab4.model.SimulatorModel;
import com.example.javalab4.viewModel.CreateSimulatorViewModel;
import com.example.javalab4.viewModel.ElevatorsViewModel;
import lombok.Getter;

@Getter
public class ViewModelProvider {
    private final SimulatorModel model;
    private CreateSimulatorViewModel createSimulatorViewModel;
    private ElevatorsViewModel elevatorsViewModel;

    public ViewModelProvider(SimulatorModel model) {
        this.model = model;
    }

    public void instantiateViewModels(ViewHandler viewHandler) {
        createSimulatorViewModel = new CreateSimulatorViewModel(model, viewHandler);
        elevatorsViewModel = new ElevatorsViewModel(model, viewHandler);
    }

}
