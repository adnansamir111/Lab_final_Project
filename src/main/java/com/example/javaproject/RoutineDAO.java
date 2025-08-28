package com.example.javaproject;

import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class RoutineDAO {

    // Insert a new routine event into the database
    public static void insert(Routine event) {
        String query = "INSERT INTO routine_events (course_name, start_time, end_time, room, day_of_week) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DB.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, event.getCourseName());
            stmt.setString(2, event.getStartTime().toString());
            stmt.setString(3, event.getEndTime().toString());
            stmt.setString(4, event.getRoom());
            stmt.setString(5, event.getDayOfWeek());  // Store the day of the week

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Retrieve routine events for a specific day (filtered by day_of_week)
    public static List<Routine> getRoutineForToDay(String dayOfWeek) {
        List<Routine> routine = new ArrayList<>();
        String query = "SELECT * FROM routine_events WHERE day_of_week = ? ORDER BY start_time";

        try (Connection conn = DB.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, dayOfWeek);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String courseName = rs.getString("course_name");
                String startTime = rs.getString("start_time");
                String endTime = rs.getString("end_time");
                String room = rs.getString("room");
                String day = rs.getString("day_of_week");

                routine.add(new Routine(courseName, LocalTime.parse(startTime), LocalTime.parse(endTime), room, day));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return routine;
    }

    // Retrieve routine events for the entire week (Monday to Sunday)
    public static List<Routine> getRoutineForWeek() {
        List<Routine> routine = new ArrayList<>();
        String query = "SELECT * FROM routine_events ORDER BY day_of_week, start_time"; // Fetch routines for all days, ordered by day and time

        try (Connection conn = DB.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String courseName = rs.getString("course_name");
                String startTime = rs.getString("start_time");
                String endTime = rs.getString("end_time");
                String room = rs.getString("room");
                String day = rs.getString("day_of_week");

                routine.add(new Routine(courseName, LocalTime.parse(startTime), LocalTime.parse(endTime), room, day));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return routine;
    }
}
