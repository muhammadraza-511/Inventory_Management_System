package com.example.demo;

import javafx.application.Application;
import javafx.stage.Stage;

public class report {
    private final String itemName;
    private final int totalQuantity;

    public report(String itemName, int totalQuantity) {
        this.itemName = itemName;
        this.totalQuantity = totalQuantity;
    }

    public String getItemName() {
        return itemName;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }
}

