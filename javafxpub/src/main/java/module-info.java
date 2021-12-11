module com.example.pubui {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.pubui to javafx.fxml;
    exports com.example.pubui;
}