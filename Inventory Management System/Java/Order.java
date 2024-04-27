package com.example.demo;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import javafx.beans.property.*;

import java.io.IOException;


public class Order {

    private final IntegerProperty orderId;
    private final IntegerProperty customerId;
    private final StringProperty orderDate;
    private final DoubleProperty totalAmount;
    private final StringProperty orderStatus;

    public Order(int orderId, int customerId, String orderDate, double totalAmount, String orderStatus) {
        this.orderId = new SimpleIntegerProperty(orderId);
        this.customerId = new SimpleIntegerProperty(customerId);
        this.orderDate = new SimpleStringProperty(orderDate);
        this.totalAmount = new SimpleDoubleProperty(totalAmount);
        this.orderStatus = new SimpleStringProperty(orderStatus);
    }

    // Getter methods

    public int getOrderId() {
        return orderId.get();
    }

    public int getCustomerId() {
        return customerId.get();
    }

    public String getOrderDate() {
        return orderDate.get();
    }

    public double getTotalAmount() {
        return totalAmount.get();
    }

    public String getOrderStatus() {
        return orderStatus.get();
    }

    // Property methods

    public IntegerProperty orderIdProperty() {
        return orderId;
    }

    public IntegerProperty customerIdProperty() {
        return customerId;
    }

    public StringProperty orderDateProperty() {
        return orderDate;
    }

    public DoubleProperty totalAmountProperty() {
        return totalAmount;
    }

    public StringProperty orderStatusProperty() {
        return orderStatus;
    }

}