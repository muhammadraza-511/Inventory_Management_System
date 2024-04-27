package com.example.demo;

import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.stage.Stage;
import javafx.beans.property.*;

public  class Inventory {
    private final IntegerProperty itemID ;
    private final StringProperty itemName ;
    private final IntegerProperty quantity ;
    private final IntegerProperty price ;
    private final StringProperty itemType;
    private final IntegerProperty supplierID ;
    public Inventory(int itemID, String itemName, int quantity, int price, String itemType, int supplierID) {
        this.itemID=new SimpleIntegerProperty(itemID);
        this.itemName=new SimpleStringProperty(itemName);
        this.quantity=new SimpleIntegerProperty(quantity);
        this.price=new SimpleIntegerProperty(price);
        this.itemType=new SimpleStringProperty(itemType);
        this.supplierID=new SimpleIntegerProperty(supplierID);
    }

    public int getItemID() {
        return itemID.get();
    }

    public IntegerProperty itemIDProperty() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID.set(itemID);
    }

    public String getItemName() {
        return itemName.get();
    }

    public StringProperty itemNameProperty() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName.set(itemName);
    }

    public int getQuantity() {
        return quantity.get();
    }

    public IntegerProperty quantityProperty() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
    }

    public int getPrice() {
        return price.get();
    }

    public IntegerProperty priceProperty() {
        return price;
    }

    public void setPrice(int price) {
        this.price.set(price);
    }

    public String getItemType() {
        return itemType.get();
    }

    public StringProperty itemTypeProperty() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType.set(itemType);
    }

    public int getSupplierID() {
        return supplierID.get();
    }

    public IntegerProperty supplierIDProperty() {
        return supplierID;
    }

    public void setSupplierID(int supplierID) {
        this.supplierID.set(supplierID);
    }
}