package com.example.javaproject;

import java.sql.*;

public final class DB {

    private DB() {}

    public static Connection getConnection() {
        try {
            String url = "jdbc:sqlite:./database/student_assistant.db";
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection(url);
            //ENABLE FOREIGN KEY
            try (Statement st = conn.createStatement()) {
                st.executeUpdate("PRAGMA foreign_keys = ON;");
            }

            createSchema(conn); // Pass connection to schema creation method
            return conn;
        } catch (Exception e) {
            throw new RuntimeException("Database connection failed", e);
        }
    }

    private static void createSchema(Connection conn) throws SQLException {
        try (Statement st = conn.createStatement()) {

            // Course Table
            st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS course (
                  id INTEGER PRIMARY KEY AUTOINCREMENT,
                  code TEXT NOT NULL,
                  title TEXT NOT NULL,
                  instructor TEXT,
                  credits REAL 
                );
            """);

            // Tasks Table with Cascading Delete + notification fields
            st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS task (
                  id INTEGER PRIMARY KEY AUTOINCREMENT,
                  course_id INTEGER,
                  title TEXT NOT NULL,
                  notes TEXT,
                  due_at TEXT, -- yyyy-MM-dd
                  status TEXT DEFAULT 'todo', -- todo, in-progress, done
                  seen_3days INTEGER DEFAULT 0, -- reminder shown 3 days before
                  seen_dayof INTEGER DEFAULT 0, -- reminder shown on due date
                  completed_at TEXT, -- yyyy-MM-dd
                  created_at TEXT DEFAULT (datetime('now', 'localtime')),
                  updated_at TEXT DEFAULT (datetime('now', 'localtime')),
                  FOREIGN KEY(course_id) REFERENCES course(id) ON DELETE CASCADE
                );
            """);

            // If task table already exists, try adding missing columns
            try { st.executeUpdate("ALTER TABLE task ADD COLUMN seen_3days INTEGER DEFAULT 0"); } catch (SQLException ignored) {}
            try { st.executeUpdate("ALTER TABLE task ADD COLUMN seen_dayof INTEGER DEFAULT 0"); } catch (SQLException ignored) {}
            try { st.executeUpdate("ALTER TABLE task ADD COLUMN completed_at TEXT"); } catch (SQLException ignored) {}

            // Study Session Table with Cascading Delete
            st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS study_session (
                  id INTEGER PRIMARY KEY AUTOINCREMENT,
                  course_id INTEGER,
                  started_at TEXT DEFAULT (datetime('now', 'localtime')),
                  ended_at TEXT,
                  duration_min INTEGER,
                  notes TEXT,
                  FOREIGN KEY(course_id) REFERENCES course(id) ON DELETE CASCADE  -- Cascading delete
                );
            """);

            // Routine Events Table (No Cascading for now)
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


            // Resource Table for storing resources (topic, video link) related to courses
            st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS resources (
                  id INTEGER PRIMARY KEY AUTOINCREMENT,
                  course_id INTEGER NOT NULL,  -- Reference to course.id
                  topic TEXT NOT NULL,
                  video_link TEXT NOT NULL,
                  FOREIGN KEY (course_id) REFERENCES course(id) ON DELETE CASCADE  -- Foreign key referencing course.id
                );
            """);

            // Chapter Table for storing chapters related to courses
            st.executeUpdate("""
            CREATE TABLE IF NOT EXISTS chapters (
              id INTEGER PRIMARY KEY AUTOINCREMENT,
              course_id INTEGER NOT NULL,  -- Reference to course.id
              chapter_name TEXT NOT NULL,
              is_completed BOOLEAN DEFAULT FALSE,  -- Mark if chapter is completed
              FOREIGN KEY (course_id) REFERENCES course(id) ON DELETE CASCADE  -- Foreign key referencing course.id
            );
        """);

        }
    }
}
