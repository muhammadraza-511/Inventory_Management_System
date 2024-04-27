package com.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class OrderController  {


    @FXML
    private TableView<Order> ordersTableView;

    @FXML
    private TableColumn<Order, Integer> orderIdColumn;

    @FXML
    private TableColumn<Order, Integer> customerIdColumn;

    @FXML
    private TableColumn<Order, String> orderDateColumn;

    @FXML
    private TableColumn<Order, Double> totalAmountColumn;

    @FXML
    private TableColumn<Order, String> orderStatusColumn;



    @FXML
    public void initialize() {
        // Initialize columns with data types
        orderIdColumn.setCellValueFactory(cellData -> cellData.getValue().orderIdProperty().asObject());
        customerIdColumn.setCellValueFactory(cellData -> cellData.getValue().customerIdProperty().asObject());
        orderDateColumn.setCellValueFactory(cellData -> cellData.getValue().orderDateProperty());
        totalAmountColumn.setCellValueFactory(cellData -> cellData.getValue().totalAmountProperty().asObject());
        orderStatusColumn.setCellValueFactory(cellData -> cellData.getValue().orderStatusProperty());

        // Load orders into the table
        loadOrders();
    }

    private void loadOrders() {
        ObservableList<Order> ordersList = FXCollections.observableArrayList();

        // Implement the logic to fetch orders from the database
        String jdbcUrl = "jdbc:mysql://localhost:3306/inventory";
        String username = "root";
        String password = "momi";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
            String selectQuery = "SELECT * FROM Orders";
            try (PreparedStatement selectStatement = connection.prepareStatement(selectQuery)) {
                try (ResultSet resultSet = selectStatement.executeQuery()) {
                    while (resultSet.next()) {
                        Order order = new Order(
                                resultSet.getInt("OrderID"),
                                resultSet.getInt("CustomerID"),
                                resultSet.getString("OrderDate"),
                                resultSet.getDouble("TotalAmount"),
                                resultSet.getString("OrderStatus")
                        );
                        ordersList.add(order);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error loading orders from the database.");
        }

        // Set the orders to the table
        ordersTableView.setItems(ordersList);
    }

    public void gotoStaffMenu(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("StaffLogin.fxml"));
        Parent root = fxmlLoader.load();

        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

        stage.setScene(new Scene(root));
        stage.show();
    }

}
