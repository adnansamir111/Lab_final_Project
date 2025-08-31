package com.example.javaproject;

import java.sql.*;

public class DB {
    private static final String DB_URL = "jdbc:sqlite:database/student_assistant.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    /**
     * Ensure the course table has all expected columns (adds classroom_url if missing).
     */
    public static void ensureSchema() {
        try (Connection conn = getConnection();
             Statement st = conn.createStatement()) {

            // Create table if it doesn't exist
            st.executeUpdate("CREATE TABLE IF NOT EXISTS course (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "code TEXT," +
                    "title TEXT," +
                    "instructor TEXT," +
                    "credits REAL" +
                    ")");

            // Check if classroom_url column exists
            boolean hasColumn = false;
            try (ResultSet rs = st.executeQuery("PRAGMA table_info(course)")) {
                while (rs.next()) {
                    if ("classroom_url".equalsIgnoreCase(rs.getString("name"))) {
                        hasColumn = true;
                        break;
                    }
                }
            }

            // If not, add it
            if (!hasColumn) {
                st.executeUpdate("ALTER TABLE course ADD COLUMN classroom_url TEXT");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
