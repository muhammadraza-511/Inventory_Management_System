package com.example.demo;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class viewInventoryController  {

    @FXML
    private TableView<Inventory> inventoryTableView;


    @FXML
    private TableColumn<Inventory, Integer> itemIDColumn;

    @FXML
    private TableColumn<Inventory, String> itemNameColumn;

    @FXML
    private TableColumn<Inventory, Integer> quantityColumn;

    @FXML
    private TableColumn<Inventory, Integer> priceColumn;

    @FXML
    private TableColumn<Inventory, String> itemTypeColumn;

    @FXML
    private TableColumn<Inventory, Integer> SupplierIDColumn;

    public void initialize() {
        itemIDColumn.setCellValueFactory(cellData -> cellData.getValue().itemIDProperty().asObject());
        itemNameColumn.setCellValueFactory(cellData -> cellData.getValue().itemNameProperty());
        quantityColumn.setCellValueFactory(cellData -> cellData.getValue().quantityProperty().asObject());
        priceColumn.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());
        itemTypeColumn.setCellValueFactory(cellData -> cellData.getValue().itemTypeProperty());
        SupplierIDColumn.setCellValueFactory(cellData -> cellData.getValue().supplierIDProperty().asObject());

//        // Add the columns to the TableView
//        inventoryTableView.getColumns().addAll(itemIDColumn, itemNameColumn, quantityColumn, priceColumn, itemTypeColumn, SupplierIDColumn);
//        // Fetch data from the database and populate the TableView
        loadDataFromDatabase();
    }
    private void loadDataFromDatabase() {
        ObservableList<Inventory> inventoryList = FXCollections.observableArrayList();
        // Fetch data from the database and populate the TableView
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/inventory", "root", "momi")) {
            // Execute a SELECT query to retrieve inventory items
            String selectQuery = "SELECT * FROM Inventory";
            try (ResultSet resultSet = connection.createStatement().executeQuery(selectQuery)) {
                while (resultSet.next()) {
                    // Create Inventory objects from the ResultSet
                    Inventory item = new Inventory(
                            resultSet.getInt("itemID"),
                            resultSet.getString("itemName"),
                            resultSet.getInt("quantity"),
                            resultSet.getInt("price"),
                            resultSet.getString("itemType"),
                            resultSet.getInt("SupplierID")
                    );
                    // Add the item to the TableView
                    inventoryList.add(item);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        inventoryTableView.setItems(inventoryList);
    }

    public void gotomanageinventory(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("manageInventory.fxml"));
        Parent root = fxmlLoader.load();

        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void gotoCustomerMenu(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("customerLogin.fxml"));
        Parent root = fxmlLoader.load();

        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
