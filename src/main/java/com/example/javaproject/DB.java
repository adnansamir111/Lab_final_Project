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
            // Course table
            st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS course (
                  id INTEGER PRIMARY KEY AUTOINCREMENT,
                  code TEXT NOT NULL,
                  title TEXT NOT NULL,
                  instructor TEXT,
                  credits INTEGER DEFAULT 0
                );
            """);

            // Tasks table
            st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS task (
                  id INTEGER PRIMARY KEY AUTOINCREMENT,
                  course_id INTEGER,
                  title TEXT NOT NULL,
                  notes TEXT,
                  due_at TEXT, -- yyyy-MM-dd
                  status TEXT DEFAULT 'todo', -- todo, in-progress, done
                  created_at TEXT DEFAULT (datetime('now', 'localtime')),
                  updated_at TEXT DEFAULT (datetime('now', 'localtime')),
                  FOREIGN KEY(course_id) REFERENCES course(id)
                );
            """);

            // Study session table
            st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS study_session (
                  id INTEGER PRIMARY KEY AUTOINCREMENT,
                  course_id INTEGER,
                  started_at TEXT DEFAULT (datetime('now', 'localtime')),
                  ended_at TEXT,
                  duration_min INTEGER,
                  notes TEXT,
                  FOREIGN KEY(course_id) REFERENCES course(id)
                );
            """);

            // Routine events table
            st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS routine_events (
                  id INTEGER PRIMARY KEY AUTOINCREMENT,
                  course_name TEXT NOT NULL,
                  start_time TEXT NOT NULL,  -- Stored as TIME (HH:mm)
                  end_time TEXT NOT NULL,    -- Stored as TIME (HH:mm)
                  room TEXT,
                  day_of_week TEXT NOT NULL -- e.g., MONDAY, TUESDAY, etc.
                );
            """);
        }
    }
}
