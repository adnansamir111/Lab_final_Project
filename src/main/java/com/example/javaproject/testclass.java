package com.example.javaproject;

import java.sql.Connection;
import java.sql.SQLException;

public class testclass {
    public static void main(String[] args) {
        // Optional: ensure schema (safe no-op if already set up)
        DB.ensureSchema();

        try (Connection conn = DB.getConnection()) {
            System.out.println("DB created and connected successfully!");
        } catch (SQLException e) {
            System.err.println("Failed to connect to DB: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
