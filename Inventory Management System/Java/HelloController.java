package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.Label;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;


public class HelloController {


    public TextField phoneField;
    public TextField userNameField;
    public javafx.scene.control.Label sys_Message;
    public Button loginButton;
    public TextField itemNameField;
    public TextField itemQuantityField;
    public ComboBox OrderTypeComboBox;
    public javafx.scene.control.Label error_Message;
    public javafx.scene.control.Label system_Message;
    public ComboBox StatusComboBox;
    public TextField OrderIDField;



    @FXML
    private TextField tfTitle;
    @FXML
    private TextField nameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ComboBox<String> applicationTypeComboBox;

    @FXML
    private Button registerButton;
    private Label totalAmountField;


    public void LoginPage() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("menu.fxml"));
        Parent root = fxmlLoader.load();

        Stage stage = (Stage) loginButton.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void handleLogOut(ActionEvent actionEvent) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("menu.fxml"));
        Parent root = fxmlLoader.load();

        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

        stage.setScene(new Scene(root));
        stage.show();
    }



    @FXML
    public void doRegister () throws IOException
    {

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Parent root = fxmlLoader.load();

        Stage stage =(Stage) registerButton.getScene().getWindow();
        stage.setScene(new Scene(root));



    }
    // for Signing in the system
    public void handleSignup(){
        String name= nameField.getText();
        String username=userNameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String phone =phoneField.getText();
        String userType = applicationTypeComboBox.getValue();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || userType == null|| phone.isEmpty()) {
            sys_Message.setText("Please fill in all the fields.");
            return;
        }
        try (Connection connection= DriverManager.getConnection( "jdbc:mysql://localhost:3306/inventory","root", "momi");
        ) {

            String insertQuery = "INSERT INTO User (UserType, Name,Username,  Email, Password,  Phone) VALUES (?, ?, ?, ?,?,?);";
            try (PreparedStatement pst1 = connection.prepareStatement(insertQuery)) {
                pst1.setString(1, userType);
                pst1.setString(2, name);
                pst1.setString(3, username);
                pst1.setString(4, email);
                pst1.setString(5, password);
                pst1.setString(6, phone);
                pst1.executeUpdate();
                sys_Message.setText("Registration Successful.");
            }


        } catch (SQLException e) {
            e.printStackTrace();
            sys_Message.setText("Registration Failed.");
        }

    }

    public void handlelogin() {
        String username = userNameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            sys_Message.setText("Please fill in all the fields.");
            return;
        }


        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/inventory", "root", "momi")) {
            String loginQuery = "SELECT * FROM User WHERE Username = ? AND Password = ?";
            try (PreparedStatement loginStatement = connection.prepareStatement(loginQuery)) {
                loginStatement.setString(1, username);
                loginStatement.setString(2, password);

                try (ResultSet loginResult = loginStatement.executeQuery()) {
                    if (loginResult.next()) {
                        // Login successful
                        String userType = loginResult.getString("UserType");
                        sys_Message.setText("");

                        // Redirect to the user page based on userType
                        if ("Manager".equals(userType)) {

                                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("managerLogin.fxml"));
                                Parent root = fxmlLoader.load();

                                // Set the controller for the loaded FXML
                               inventoryController inventoryController = fxmlLoader.getController();

                                // Rest of your existing code
                                Stage stage = (Stage) userNameField.getScene().getWindow();
                                stage.setScene(new Scene(root));

                        }
                        else if ("Staff".equals(userType)) {
                            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("StaffLogin.fxml"));
                            Parent root = fxmlLoader.load();

                            Stage stage = (Stage) userNameField.getScene().getWindow();
                            stage.setScene(new Scene(root));
                        }
                        else if ("Customer".equals(userType)) {

                            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("customerLogin.fxml"));
                            Parent root = fxmlLoader.load();
                            Stage stage = (Stage) userNameField.getScene().getWindow();
                            stage.setScene(new Scene(root));


                        }
                        else {

                            sys_Message.setText("Invalid username or password. Please try again.");
                        }
                    } else {
                        // Login failed
                        sys_Message.setText("Invalid username or password. Please try again.");
                    }
                }
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            System.out.println("Login failed. Please try again.");
        }
    }


    public void DoOrder(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("placeOrder.fxml"));
        Parent root = fxmlLoader.load();

        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

        stage.setScene(new Scene(root));
        stage.show();
    }

    public void handleOrder(ActionEvent actionEvent) {

        String customerUsername = userNameField.getText();
        LocalDate orderDate = LocalDate.now(); // Automatically set to the current date
        String itemName = itemNameField.getText();
        int itemQuantity;

        try {
            itemQuantity = Integer.parseInt(itemQuantityField.getText());
        } catch (NumberFormatException e) {
            error_Message.setText("Invalid quantity. Please enter a valid number.");
            return;
        }

        if (customerUsername.isEmpty() || itemName.isEmpty()) {
            error_Message.setText("Please fill in all the fields.");
            return;
        }

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/inventory", "root", "momi")) {
            // Get the customer ID based on the username
            int customerId = getCustomerIdByUsername(connection, customerUsername);

            if (customerId == -1) {
                error_Message.setText("Customer not found.");
                return;
            }

            // Check if the item exists in the Inventory
            if (!isItemExists(connection, itemName)) {
                error_Message.setText("Item not found in the inventory.");
                return;
            }

            // Calculate total amount based on items in the order
            BigDecimal totalAmount = calculateTotalAmount(connection,  itemName, itemQuantity);
            if (totalAmount == null) {
                error_Message.setText("Failed to calculate total amount.");
                return;
            }
            decrementItemQuantity(connection, itemName, itemQuantity);
            // Insert order data with "Pending" status and current timestamp
            String insertOrderQuery = "INSERT INTO Orders (CustomerID, OrderDate, TotalAmount, OrderStatus) VALUES (?, ?, ?, 'Pending')";
            try (PreparedStatement orderStatement = connection.prepareStatement(insertOrderQuery, Statement.RETURN_GENERATED_KEYS)) {
                orderStatement.setInt(1, customerId);
                orderStatement.setDate(2, Date.valueOf(orderDate));
                orderStatement.setBigDecimal(3, totalAmount);

                int affectedRows = orderStatement.executeUpdate();
                if (affectedRows > 0) {
                    error_Message.setText("Order placed successfully.");
                    insertRecordData(connection, itemName, itemQuantity);
                    // Retrieve the generated order ID
//                    try (ResultSet generatedKeys = orderStatement.getGeneratedKeys()) {
//                        if (generatedKeys.next()) {
//                            int orderId = generatedKeys.getInt(1);
//                            System.out.println("Generated Order ID: " + orderId);
//                            // You can use the orderId as needed
//                        } else {
//                            System.out.println("Failed to retrieve generated order ID.");
//                        }
//                    }
                } else {
                    error_Message.setText("Failed to place order.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            error_Message.setText("Failed to place order.");
        }
    }
    private void insertRecordData(Connection connection, String itemName, int itemQuantity) throws SQLException {
        // Insert data into the 'record' table
        String insertRecordQuery = "INSERT INTO record (ItemID, quantity) VALUES (?, ?)";
        try (PreparedStatement recordStatement = connection.prepareStatement(insertRecordQuery)) {
            // Get the ItemID from the Inventory table based on the item name
            int itemId = getItemIdByItemName(connection, itemName);

            // Set parameters for the 'record' table insertion
            recordStatement.setInt(1, itemId);
            recordStatement.setInt(2, itemQuantity);

            // Execute the insertion
            recordStatement.executeUpdate();
        }
    }
    private int getItemIdByItemName(Connection connection, String itemName) throws SQLException {
        // Get the ItemID from the Inventory table based on the item name
        String getItemIdQuery = "SELECT itemID FROM Inventory WHERE itemName = ?";
        try (PreparedStatement getItemIdStatement = connection.prepareStatement(getItemIdQuery)) {
            getItemIdStatement.setString(1, itemName);
            try (ResultSet resultSet = getItemIdStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("itemID");
                } else {
                    throw new SQLException("Item not found in the Inventory.");
                }
            }
        }
    }
    private int getCustomerIdByUsername(Connection connection, String username) throws SQLException {
        String customerIdQuery = "SELECT UserID FROM User WHERE Username = ?";
        try (PreparedStatement customerIdStatement = connection.prepareStatement(customerIdQuery)) {
            customerIdStatement.setString(1, username);
            try (ResultSet customerIdResult = customerIdStatement.executeQuery()) {
                if (customerIdResult.next()) {
                    return customerIdResult.getInt("UserID");
                }
            }
        }
        return -1; // Return -1 if the customer is not found
    }
    private boolean isItemExists(Connection connection, String itemName) throws SQLException {
        String checkItemQuery = "SELECT COUNT(*) FROM Inventory WHERE itemName = ?";
        try (PreparedStatement checkItemStatement = connection.prepareStatement(checkItemQuery)) {
            checkItemStatement.setString(1, itemName);

            try (ResultSet checkItemResult = checkItemStatement.executeQuery()) {
                return checkItemResult.next() && checkItemResult.getInt(1) > 0;
            }
        }
    }
    private BigDecimal calculateTotalAmount(Connection connection, String itemName, int itemQuantity) throws SQLException {
        String calculateTotalAmountQuery = "SELECT SUM(i.price * ?) " +
                "FROM Inventory i " +
                "WHERE i.itemName = ?";

        try (PreparedStatement totalAmountStatement = connection.prepareStatement(calculateTotalAmountQuery)) {
            totalAmountStatement.setInt(1, itemQuantity);
            totalAmountStatement.setString(2, itemName);

            try (ResultSet totalAmountResult = totalAmountStatement.executeQuery()) {
                if (totalAmountResult.next()) {
                    return totalAmountResult.getBigDecimal(1);
                }
            }
        }

        return null;
    }
    private void decrementItemQuantity(Connection connection, String itemName, int orderedQuantity) throws SQLException {
        // Check if the item exists in the Inventory
        if (isItemExists(connection, itemName)) {
            // Decrement the quantity of the ordered item
            String updateQuantityQuery = "UPDATE Inventory SET quantity = quantity - ? WHERE itemName = ?";
            try (PreparedStatement updateQuantityStatement = connection.prepareStatement(updateQuantityQuery)) {
                updateQuantityStatement.setInt(1, orderedQuantity);
                updateQuantityStatement.setString(2, itemName);

                int rowsAffected = updateQuantityStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Quantity decremented for item: " + itemName);
                } else {
                    System.out.println("Failed to update quantity for item: " + itemName);
                }
            }
        } else {
            System.out.println("Item not found in the inventory.");
        }
    }
    public void processOrder(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("processOrder.fxml"));
        Parent root = fxmlLoader.load();

        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

        stage.setScene(new Scene(root));
        stage.show();
    }

    public void handleProcessOrder(ActionEvent actionEvent) {
        String orderId = OrderIDField.getText();
        String status = (String) StatusComboBox.getValue();

        if (orderId.isEmpty() || status == null) {
            error_Message.setText("Failed to prcess order.");
            return;
        }

        // Perform the order processing logic here
        updateOrderStatus(orderId, status);

        // Optionally, you can show a success message or perform other actions
        System.out.println("Order processed successfully!");
    }
    private void updateOrderStatus(String orderId, String status) {
        // Implement the logic to update the order status in the database
        String jdbcUrl = "jdbc:mysql://localhost:3306/inventory";
        String username = "root";
        String password = "momi";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
            String updateQuery = "UPDATE Orders SET OrderStatus = ? WHERE OrderID = ?";
            try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                updateStatement.setString(1, status);
                updateStatement.setInt(2, Integer.parseInt(orderId));
                int rowsAffected = updateStatement.executeUpdate();

                if (rowsAffected > 0) {
                    error_Message.setText("Failed to process order.");
                } else {
                    System.out.println("Failed to update order status. Order ID not found.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error updating order status.");
        }
    }
    @FXML
    private Button viewOrderButton;
    @FXML
    public void handleViewOrder() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("viewOrders.fxml"));
        Parent root = fxmlLoader.load();

        OrderController OrderController = fxmlLoader.getController();
        // You can pass data to the ViewOrderController if needed

        Stage stage = (Stage) viewOrderButton.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void gotoStaffMenu(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("StaffLogin.fxml"));
        Parent root = fxmlLoader.load();

        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

        stage.setScene(new Scene(root));
        stage.show();
    }
    @FXML
    private Button viewButton;
    public void handleViewItems(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("viewInventory.fxml"));
        Parent root = fxmlLoader.load();

        viewInventoryController viewInventoryController = fxmlLoader.getController();
        // You can pass data to the ViewOrderController if needed

        Stage stage = (Stage) viewButton.getScene().getWindow();
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

    public void handleViewItemsforCustomer(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("viewInventoryCustomer.fxml"));
        Parent root = fxmlLoader.load();

        viewInventoryController viewInventoryController = fxmlLoader.getController();
        // You can pass data to the ViewOrderController if needed

        Stage stage = (Stage) viewButton.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}

