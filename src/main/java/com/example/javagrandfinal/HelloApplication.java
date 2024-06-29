package com.example.javagrandfinal;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Tooltip;

import java.sql.SQLException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws SQLException {
        DBUtils.getInstance().openConnection();

        PieChart pieChart = new PieChart();
        pieChart.setData(DBUtils.getInstance().getGroupedProducts());

        for (PieChart.Data data : pieChart.getData()) {
            Tooltip tooltip = new Tooltip(data.getName() + ": " + (int) data.getPieValue());
            Tooltip.install(data.getNode(), tooltip);
            data.setName(data.getName() + " (" + (int) data.getPieValue() + ")");
        }

        Label formLabel = new Label("Add Product");
        formLabel.setStyle("-fx-font-size: 25px; -fx-font-weight: bold; -fx-text-fill: #2a9d8f;");

        Label nameLabel = new Label("Name");
        Label priceLabel = new Label("Price");
        Label quantityLabel = new Label("Quantity");

        TextField nameField = new TextField();
        nameField.setStyle("-fx-min-width: 200px; -fx-min-height: 40px; -fx-border-color: #264653; -fx-border-radius: 5px;");
        TextField priceField = new TextField();
        priceField.setStyle("-fx-min-width: 200px; -fx-min-height: 40px; -fx-border-color: #264653; -fx-border-radius: 5px;");
        TextField quantityField = new TextField();
        quantityField.setStyle("-fx-min-width: 200px; -fx-min-height: 40px; -fx-border-color: #264653; -fx-border-radius: 5px;");

        Button addButton = new Button("Add Product");
        addButton.setStyle("-fx-text-fill: #fff; -fx-background-color: #e76f51; -fx-min-height: 40px; -fx-cursor: hand; -fx-border-radius: 5px;");
        addButton.setMaxWidth(Double.MAX_VALUE);

        addButton.setOnAction(actionEvent -> {
            try {
                DBUtils.getInstance().insertProduct(nameField.getText(), Integer.parseInt(priceField.getText()), Integer.parseInt(quantityField.getText()));
                pieChart.setData(DBUtils.getInstance().getGroupedProducts());

                // Add tooltips to new data
                for (PieChart.Data data : pieChart.getData()) {
                    Tooltip tooltip = new Tooltip(data.getName() + ": " + (int) data.getPieValue());
                    Tooltip.install(data.getNode(), tooltip);
                    data.setName(data.getName() + " (" + (int) data.getPieValue() + ")");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        VBox nameBox = new VBox(8, nameLabel, nameField);
        VBox priceBox = new VBox(8, priceLabel, priceField);
        VBox quantityBox = new VBox(8, quantityLabel, quantityField);

        HBox formFields = new HBox(20, nameBox, priceBox, quantityBox);
        formFields.setAlignment(Pos.CENTER);

        VBox form = new VBox(20, formLabel, formFields, addButton);
        form.setAlignment(Pos.CENTER);
        form.setPadding(new Insets(20));
        form.setStyle("-fx-background-color: #f4a261; -fx-background-radius: 10px; -fx-padding: 20px;");

        VBox mainLayout = new VBox(50, form, pieChart);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setPadding(new Insets(40));
        mainLayout.setStyle("-fx-background-color: #f4f1de;");

        Scene scene = new Scene(mainLayout, 900, 800);
        stage.setTitle("Products!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
