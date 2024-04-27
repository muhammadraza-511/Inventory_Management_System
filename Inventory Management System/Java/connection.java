package com.example.demo;

import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.Connection;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class connection {
    public static void main(String[] args) {
        try{

            Connection connection= DriverManager.getConnection( "jdbc:mysql://localhost:3306/inventory","root", "momi");
            Statement statement=connection.createStatement();
            ResultSet r=statement.executeQuery("select * from manager");
            while(r.next()){
                System.out.println(r.getString("ManagerID"));
                System.out.println(r.getString("name"));
                System.out.println(r.getString("Email"));
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}

