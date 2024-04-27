package com.example.demo;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.List;
import java.util.Objects;

public class inventoryController {


    public TextField priceField;
    public TextField quantityField;
    public TextField nameField;
    public TextField itemTypeField;
    public TextField SupplierField;
    public Button addButton;
    public Label sys_Message;

    public ComboBox UpdateTypeComboBox;
    public TextField nameField1;
    public TextField changeField1;
    public TextField changeField;
    public AnchorPane inventoryView;
    //public TableColumn supplierIDColumn;

    public void handleLogOut(ActionEvent actionEvent) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("menu.fxml"));
        Parent root = fxmlLoader.load();

        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

        stage.setScene(new Scene(root));
        stage.show();
    }
    @FXML
   // private HelloController controllerObj;
    public void manageaccount(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("manageAccount.fxml"));
        Parent root = fxmlLoader.load();

        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

        stage.setScene(new Scene(root));
        stage.show();
    }
    @FXML
    private Button reportButton;
    public void reportsgeneration(ActionEvent actionEvent) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("viewReport.fxml"));
        Parent root = fxmlLoader.load();

        reportController reportController = fxmlLoader.getController();
        // You can pass data to the ViewOrderController if needed

        Stage stage = (Stage) reportButton.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void manageinventory(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("manageInventory.fxml"));
        Parent root = fxmlLoader.load();

        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }




    public void additem(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("addItem.fxml"));
        Parent root = fxmlLoader.load();

        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

        stage.setScene(new Scene(root));
        stage.show();
    }

    public void handleAddItem() {
        sys_Message.setText("");
        String itemName = nameField.getText();
        int quantity = Integer.parseInt(quantityField.getText());
        int price = Integer.parseInt(priceField.getText());
        String itemType = itemTypeField.getText();
        int supplierID = Integer.parseInt(SupplierField.getText());

        if (itemName.isEmpty() || itemType.isEmpty()|| quantityField.getText().isEmpty() ||priceField.getText().isEmpty()
        || SupplierField.getText().isEmpty()) {
            sys_Message.setText("Please fill in all the fields.");
            return;
        }

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/inventory", "root", "momi")) {

            String insertQuery = "INSERT INTO Inventory (itemName, quantity, price, itemType, SupplierID) VALUES (?, ?, ?, ?, ?);";
            try (PreparedStatement pst1 = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
                pst1.setString(1, itemName);
                pst1.setInt(2, quantity);
                pst1.setInt(3, price);
                pst1.setString(4, itemType);
                pst1.setInt(5, supplierID);
                pst1.executeUpdate();

                // You can retrieve the generated itemID if needed
//                ResultSet generatedKeys = pst1.getGeneratedKeys();
//                if (generatedKeys.next()) {
//                    int generatedID = generatedKeys.getInt(1);
//                    // Do something with the generatedID if needed
//                }
                sys_Message.setText("");
                sys_Message.setText("Item added to inventory successfully.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            sys_Message.setText("Failed to add item to inventory.");
        }


    }

    public void deleteItem(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("deleteItem.fxml"));
        Parent root = fxmlLoader.load();

        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void handledeleteItem(ActionEvent actionEvent) {
        String itemName = nameField.getText();
        if (itemName.isEmpty()  ) {
            sys_Message.setText("Please fill in all the fields.");
            return;
        }

        String newName=".";
        int zeroValue = 0;
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/inventory", "root", "momi")) {


            String updateQuery = "UPDATE Inventory SET itemName = ?, quantity = ?, price = ?, itemType = ? WHERE itemName = ?";
            try (PreparedStatement pst = connection.prepareStatement(updateQuery)) {
                pst.setString(1, newName);
                pst.setInt(2, zeroValue);
                pst.setInt(3, zeroValue);
                pst.setString(4, newName);
                //pst.setInt(5, zeroValue);
                pst.setString(5, itemName);
                // Set the itemID for which you want to update the row
                int rowsAffected = pst.executeUpdate();

                if (rowsAffected > 0) {
                    sys_Message.setText("Row updated successfully.");
                } else {
                    sys_Message.setText("Item not found or could not be updated.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            sys_Message.setText("Failed to delete record.");
        }
    }

    public void editItem(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("updateItem.fxml"));
        Parent root = fxmlLoader.load();

        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

        stage.setScene(new Scene(root));
        stage.show();
    }

    public void handleUpdate() {
        String itemName = changeField1.getText();
        String updateType = (String) UpdateTypeComboBox.getValue();
        int update = Integer.parseInt(changeField.getText());
        if (itemName.isEmpty()) {
            sys_Message.setText("Please enter the item name.");
            return;
        }
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/inventory", "root", "momi")) {
            if (Objects.equals(updateType, "quantity")) {
                String updateQuery = "UPDATE Inventory SET quantity = ? WHERE itemName = ?;";
                try (PreparedStatement pst1 = connection.prepareStatement(updateQuery)) {
                    pst1.setInt(1, update);
                    pst1.setString(2, itemName);
                    int rowsAffected = pst1.executeUpdate();

                    if (rowsAffected > 0) {
                        sys_Message.setText("Item quantity updated successfully.");
                    } else {
                        sys_Message.setText("Item not found or could not be updated.");
                    }
                }
            } else {
                String updateQuery = "UPDATE Inventory SET price = ? WHERE itemName = ?;";
                try (PreparedStatement pst1 = connection.prepareStatement(updateQuery)) {
                    pst1.setInt(1, update);
                    pst1.setString(2, itemName);
                    int rowsAffected = pst1.executeUpdate();

                    if (rowsAffected > 0) {
                        sys_Message.setText("Item price updated successfully.");
                    } else {
                        sys_Message.setText("Item not found or could not be updated.");
                    }
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    @FXML
    private Button viewButton;

    public void handleViewItem(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("viewInventory.fxml"));
        Parent root = fxmlLoader.load();

        viewInventoryController viewInventoryController = fxmlLoader.getController();
        // You can pass data to the ViewOrderController if needed

        Stage stage = (Stage) viewButton.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void gotoManagerMenu(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("managerLogin.fxml"));
        Parent root = fxmlLoader.load();

        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

        stage.setScene(new Scene(root));
        stage.show();
    }
}

