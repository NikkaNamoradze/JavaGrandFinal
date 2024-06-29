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

    private static final String URL = "jdbc:mysql://localhost:3306/";
    private static final String DATABASE_NAME = "demoproducts";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    private Connection connection;
    private Statement statement;
    private static DBUtils instance;

    private DBUtils() {
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            statement = connection.createStatement();
            createDatabaseIfNotExists();
            useDatabase();
            createTableIfNotExists();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public synchronized static DBUtils getInstance() {
        if (instance == null) {
            instance = new DBUtils();
        }
        return instance;
    }

    public void openConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        }
    }

    private void createDatabaseIfNotExists() {
        try {
            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DATABASE_NAME);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void useDatabase() {
        try {
            statement.executeUpdate("USE " + DATABASE_NAME);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTableIfNotExists() {
        try {
            String createTableSQL = "CREATE TABLE IF NOT EXISTS Products (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "name VARCHAR(255) NOT NULL," +
                    "price INT NOT NULL," +
                    "quantity INT NOT NULL" +
                    ")";
            statement.executeUpdate(createTableSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
