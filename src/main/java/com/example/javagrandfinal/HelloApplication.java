package com.example.javagrandfinal;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Tooltip;

import java.sql.SQLException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws SQLException {
        DBUtils.getInstance().openConnection();

        PieChart pieChart = new PieChart();
        pieChart.setLegendVisible(false);
        pieChart.setData(DBUtils.getInstance().getGroupedProducts());

        for (PieChart.Data data : pieChart.getData()) {
            Tooltip tooltip = new Tooltip(data.getName() + ": " + (int) data.getPieValue());
            Tooltip.install(data.getNode(), tooltip);
        }

        Label formLabel = new Label("Add Product");
        formLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #1d3557;");

        Label nameLabel = new Label("Name");
        nameLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #457b9d;");
        Label priceLabel = new Label("Price");
        priceLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #457b9d;");
        Label quantityLabel = new Label("Quantity");
        quantityLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #457b9d;");

        TextField nameField = new TextField();
        nameField.setStyle("-fx-min-width: 200px; -fx-min-height: 40px; -fx-border-color: #457b9d; -fx-border-radius: 5px; -fx-padding: 5px;");
        TextField priceField = new TextField();
        priceField.setStyle("-fx-min-width: 200px; -fx-min-height: 40px; -fx-border-color: #457b9d; -fx-border-radius: 5px; -fx-padding: 5px;");
        TextField quantityField = new TextField();
        quantityField.setStyle("-fx-min-width: 200px; -fx-min-height: 40px; -fx-border-color: #457b9d; -fx-border-radius: 5px; -fx-padding: 5px;");

        Button addButton = new Button("Add Product");
        addButton.setStyle(
                "-fx-text-fill: #fff; " +
                        "-fx-background-color: #e63946; " +
                        "-fx-min-height: 40px; " +
                        "-fx-cursor: hand; " +
                        "-fx-border-radius: 5px; " +
                        "-fx-padding: 0 20px;"
        );
        addButton.setOnMouseEntered(e -> addButton.setStyle("-fx-text-fill: #fff; -fx-background-color: #d62828; -fx-min-height: 40px; -fx-cursor: hand; -fx-border-radius: 5px; -fx-padding: 0 20px;"));
        addButton.setOnMouseExited(e -> addButton.setStyle("-fx-text-fill: #fff; -fx-background-color: #e63946; -fx-min-height: 40px; -fx-cursor: hand; -fx-border-radius: 5px; -fx-padding: 0 20px;"));
        addButton.setMaxWidth(Double.MAX_VALUE);

        addButton.setOnAction(actionEvent -> {
            try {
                DBUtils.getInstance().insertProduct(nameField.getText(), Integer.parseInt(priceField.getText()), Integer.parseInt(quantityField.getText()));
                pieChart.setData(DBUtils.getInstance().getGroupedProducts());
                for (PieChart.Data data : pieChart.getData()) {
                    Tooltip tooltip = new Tooltip(data.getName() + ": " + (int) data.getPieValue());
                    Tooltip.install(data.getNode(), tooltip);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        VBox nameBox = new VBox(10, nameLabel, nameField);
        VBox priceBox = new VBox(10, priceLabel, priceField);
        VBox quantityBox = new VBox(10, quantityLabel, quantityField);

        VBox formFields = new VBox(10, nameBox, priceBox, quantityBox);
        formFields.setAlignment(Pos.CENTER);

        VBox form = new VBox(20, formLabel, formFields, addButton);
        form.setAlignment(Pos.CENTER);
        form.setPadding(new Insets(20));

        VBox mainLayout = new VBox(30, form, pieChart);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setPadding(new Insets(40));

        Scene scene = new Scene(mainLayout, 900, 800);
        stage.setTitle("Products!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
