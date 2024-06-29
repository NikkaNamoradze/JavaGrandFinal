module com.example.javagrandfinal {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.sql;
    requires static lombok;

    opens com.example.javagrandfinal to javafx.fxml;
    exports com.example.javagrandfinal;
}