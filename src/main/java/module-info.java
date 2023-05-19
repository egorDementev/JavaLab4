module com.example.javalab4 {
    requires javafx.controls;
    requires javafx.fxml;
    requires lombok;


    opens com.example.javalab4 to javafx.fxml;
    exports com.example.javalab4;
    exports com.example.javalab4.view;
    exports com.example.javalab4.viewModel;
    exports com.example.javalab4.model;
    exports com.example.javalab4.application;
    opens com.example.javalab4.view to javafx.fxml;
}