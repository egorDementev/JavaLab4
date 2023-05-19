module com.example.javalab4 {
    requires javafx.controls;
    requires javafx.fxml;
            
                            
    opens com.example.javalab4 to javafx.fxml;
    exports com.example.javalab4;
}