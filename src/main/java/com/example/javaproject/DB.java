package com.example.javaproject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public final class DB {

    private static Connection conn;

    private DB() {}

    // Singleton connection (reuse across app)
    public static synchronized Connection getConnection() {
        if (conn == null) {
            try {

                String url = "jdbc:sqlite:./database/student_assistant.db";

                // Explicitly load driver (safe in modular projects)
                Class.forName("org.sqlite.JDBC");

                conn = DriverManager.getConnection(url);
                createSchema();
                System.out.println("Connected to SQLite DB: " + url);
            } catch (Exception e) {
                throw new RuntimeException("Database connection failed", e);
            }
        }
        return conn;
    }

    // Create tables if they don’t exist
    private static void createSchema() throws SQLException {
        try (Statement st = conn.createStatement()) {

            // Courses
            st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS course (
                  id INTEGER PRIMARY KEY AUTOINCREMENT,
                  code TEXT NOT NULL,
                  title TEXT NOT NULL,
                  instructor TEXT,
                  credits INTEGER DEFAULT 0
                );
            """);

            // Tasks
            st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS task (
                  id INTEGER PRIMARY KEY AUTOINCREMENT,
                  course_id INTEGER,
                  title TEXT NOT NULL,
                  notes TEXT,
                  due_at TEXT, -- yyyy-MM-dd
                  status TEXT DEFAULT 'todo', -- todo, in-progress, done
                  created_at TEXT DEFAULT (datetime('now')),
                  updated_at TEXT DEFAULT (datetime('now')),
                  FOREIGN KEY(course_id) REFERENCES course(id)
                );
            """);

            // Study sessions
            st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS study_session (
                  id INTEGER PRIMARY KEY AUTOINCREMENT,
                  course_id INTEGER,
                  started_at TEXT,
                  ended_at TEXT,
                  duration_min INTEGER,
                  notes TEXT,
                  FOREIGN KEY(course_id) REFERENCES course(id)
                );
            """);
        }
    }
}

