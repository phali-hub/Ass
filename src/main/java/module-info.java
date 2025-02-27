module com.example.ass {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.ass to javafx.fxml;
    exports com.example.ass;
}