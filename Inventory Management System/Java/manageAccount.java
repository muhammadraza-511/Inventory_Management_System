package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;

public class manageAccount {

    @FXML
    public HelloController controllerObj;
    public TextField changeField1;
    public TextField changeField2;

    public ComboBox UpdateComboBox;
    public Label sys_Message;


    public void managerAddAccount(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Parent root = fxmlLoader.load();

        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

        stage.setScene(new Scene(root));
        stage.show();
    }

    public void editAccount(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("updateAccount.fxml"));
        Parent root = fxmlLoader.load();

        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

        stage.setScene(new Scene(root));
        stage.show();
    }

    public void handleAccUpdate() {
        String userName = changeField1.getText();
        String updateType = (String) UpdateComboBox.getValue();
        String change = changeField2.getText();
        if (userName.isEmpty()) {
            sys_Message.setText("Please enter the user name.");
        }
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/inventory", "root", "momi")){
            if (Objects.equals(updateType, "Password")) {
                String updateQuery = "UPDATE User SET Password = ? WHERE Username = ?;";
                try (PreparedStatement pst1 = connection.prepareStatement(updateQuery)) {
                    pst1.setString(1, change);
                    pst1.setString(2, userName);
                    int rowsAffected = pst1.executeUpdate();

                    if (rowsAffected > 0) {
                        sys_Message.setText("Password updated successfully.");
                    } else {
                        sys_Message.setText("User not found or could not be updated.");
                    }
                }
            }
            else {
                String updateQuery = "UPDATE User SET Phone = ? WHERE Username = ?;";
                try (PreparedStatement pst1 = connection.prepareStatement(updateQuery)) {
                    pst1.setString(1, change);
                    pst1.setString(2, userName);
                    int rowsAffected = pst1.executeUpdate();

                    if (rowsAffected > 0) {
                        sys_Message.setText("Phone updated successfully.");
                    } else {
                        sys_Message.setText("User not found or could not be updated.");
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }


    public void handleLogOut(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("menu.fxml"));
        Parent root = fxmlLoader.load();

        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

        stage.setScene(new Scene(root));
        stage.show();
    }

    public void gotoManageAccount(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("managerLogin.fxml"));
        Parent root = fxmlLoader.load();

        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

        stage.setScene(new Scene(root));
        stage.show();
    }
    }

