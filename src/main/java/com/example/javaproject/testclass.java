package com.example.javaproject;
import java.sql.*;

public class testclass {
    public static void main(String[] args) {
        Connection conn = DB.getConnection();
        System.out.println("DB created and connected successfully!");
    }
    }


