package com.example.javagrandfinal;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DBUtils {

    private static String URL = "jdbc:mysql://localhost:3306/products";
    private static String USERNAME = "root";
    private static String PASSWORD = "";
    private Statement statement;
    private static DBUtils instance;

    public synchronized static DBUtils getInstance() {
        if (instance == null) {
            instance = new DBUtils();
        }
        return instance;
    }

    public void openConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        this.statement = connection.createStatement();
    }

    public void insertProduct(String name, int price, int quantity) throws SQLException {
        String sql = "INSERT INTO Products (name, price, quantity) VALUES ('" + name + "'," + price + "," + quantity + ")";
        statement.execute(sql);
    }

    public ObservableList<PieChart.Data> getGroupedProducts() throws SQLException {
        String sql = "SELECT * FROM Products";
        ResultSet res = statement.executeQuery(sql);

        List<Product> products = new ArrayList<>();

        while (res.next()) {
            products.add(new Product(res.getInt("id"), res.getString("name"), res.getInt("price"), res.getInt("quantity")));
        }

        Map<String, Integer> groupedData = products.stream()
                .collect(Collectors.groupingBy(
                        Product::getName,
                        Collectors.summingInt(Product::getQuantity)
                ));

        ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
        groupedData.forEach((name, quantity) -> {
            data.add(new PieChart.Data(name, quantity));
        });

        return data;
    }
}
