package com.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.*;

public class reportController implements Initializable {


    public Label error_Message;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Call the generateReport method when the controller is initialized
        generateReportAndSaveToFile();

    }

public void generateReportAndSaveToFile() {
    System.out.println("Generating report...");

    // Clear existing items in the TableView
    // reportTableView.getItems().clear();

    try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/inventory", "root", "momi")) {
        System.out.println("Connected to the database.");

        // Query to get total quantity sold for each item from the record table
        String reportQuery = "SELECT ItemID, SUM(quantity) AS totalSold FROM record GROUP BY ItemID";

        try (PreparedStatement reportStatement = connection.prepareStatement(reportQuery);
             ResultSet resultSet = reportStatement.executeQuery()) {

            ObservableList<report> reportData = FXCollections.observableArrayList();

            while (resultSet.next()) {
                // Get item details from Inventory based on ItemID
                int itemId = resultSet.getInt("ItemID");
                String itemName = getItemNameByItemId(connection, itemId);

                // Create ItemReport object
                report itemReport = new report(itemName, resultSet.getInt("totalSold"));
                reportData.add(itemReport);
            }

            // Get all items from the Inventory table
            List<String> allItems = getAllItems(connection);

            // Identify items that are not sold
            Set<String> unsoldItems = new HashSet<>(allItems);
            for (report item : reportData) {
                unsoldItems.remove(item.getItemName());
            }

            // Add unsold items to the reportData
            unsoldItems.forEach(itemName -> reportData.add(new report(itemName, 0)));

            // Sort the reportData based on totalQuantity in descending order and then by item name
            reportData.sort(Comparator.comparingInt(report::getTotalQuantity)
                    .thenComparing(Comparator.comparing(report::getItemName))
                    .reversed());

            // Create a text file and write the report data to it
            writeReportToFile(reportData);

            System.out.println("Report generated and saved to file successfully.");
            error_Message.setText("Report has been generated");

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error executing report query: " + e.getMessage());
        }

    } catch (SQLException e) {
        e.printStackTrace();
        System.err.println("Error connecting to the database: " + e.getMessage());
    }
}

    private List<String> getAllItems(Connection connection) throws SQLException {
        // Get all items from the Inventory table
        String getAllItemsQuery = "SELECT itemName FROM Inventory";
        List<String> allItems = new ArrayList<>();

        try (PreparedStatement getAllItemsStatement = connection.prepareStatement(getAllItemsQuery);
             ResultSet resultSet = getAllItemsStatement.executeQuery()) {

            while (resultSet.next()) {
                allItems.add(resultSet.getString("itemName"));
            }
        }

        return allItems;
    }
private void writeReportToFile(ObservableList<report> reportData) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter("report.txt"))) {
        // Write all items and their quantities sold to the file
        writer.write("Sales Report:\n");
        for (report item : reportData) {
            writer.write(item.getItemName() + " - Sold: " + item.getTotalQuantity() + "\n");
        }

        // Write the least and most sold items to the file
        writer.write("\nLeast Sold Item: " + reportData.get(reportData.size() - 1).getItemName() +
                " - Sold: " + reportData.get(reportData.size() - 1).getTotalQuantity() + "\n");

        writer.write("Most Sold Item: " + reportData.get(0).getItemName() +
                " - Sold: " + reportData.get(0).getTotalQuantity() + "\n");

    } catch (IOException e) {
        e.printStackTrace();
        System.err.println("Error writing to file: " + e.getMessage());
    }
}
        private String getItemNameByItemId(Connection connection, int itemId) throws SQLException {
        // Get the itemName from the Inventory table based on ItemID
        String getItemNameQuery = "SELECT itemName FROM Inventory WHERE itemID = ?";
        try (PreparedStatement getItemNameStatement = connection.prepareStatement(getItemNameQuery)) {
            getItemNameStatement.setInt(1, itemId);
            try (ResultSet resultSet = getItemNameStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("itemName");
                } else {
                    throw new SQLException("Item not found in the Inventory.");
                }
            }
        }
    }
    public void gotomanagerMenu(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("managerLogin.fxml"));
        Parent root = fxmlLoader.load();

        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
