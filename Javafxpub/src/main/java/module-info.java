module com.example.subui {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.subui to javafx.fxml;
    opens com.example.subui.models to javafx.base;
    exports com.example.subui;
}