package com.example.javaproject;

import java.sql.*;

public final class DB {

    private DB() {}

    public static Connection getConnection() {
        try {
            String url = "jdbc:sqlite:./database/student_assistant.db";
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection(url);

            createSchema(conn); // pass connection
            return conn;
        } catch (Exception e) {
            throw new RuntimeException("Database connection failed", e);
        }
    }

    private static void createSchema(Connection conn) throws SQLException {
        try (Statement st = conn.createStatement()) {
            st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS course (
                  id INTEGER PRIMARY KEY AUTOINCREMENT,
                  code TEXT NOT NULL,
                  title TEXT NOT NULL,
                  instructor TEXT,
                  credits INTEGER DEFAULT 0
                );
            """);

            st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS task (
                  id INTEGER PRIMARY KEY AUTOINCREMENT,
                  course_id INTEGER,
                  title TEXT NOT NULL,
                  notes TEXT,
                  due_at TEXT,
                  status TEXT DEFAULT 'todo',
                  created_at TEXT DEFAULT (datetime('now')),
                  updated_at TEXT DEFAULT (datetime('now')),
                  FOREIGN KEY(course_id) REFERENCES course(id)
                );
            """);

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
