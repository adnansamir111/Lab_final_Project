package com.example.javaproject.DAO;

import com.example.javaproject.DB;
import com.example.javaproject.all_class.Routine;

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
            stmt.setString(2, event.getStartTime().toString()); // "HH:mm"
            stmt.setString(3, event.getEndTime().toString());   // "HH:mm"
            stmt.setString(4, event.getRoom());
            stmt.setString(5, event.getDayOfWeek());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Retrieve routine events for a given day
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

    // Retrieve routine events for the entire week
    public static List<Routine> getRoutineForWeek() {
        List<Routine> routine = new ArrayList<>();
        // If you need strict Mon..Sun ordering, adjust ORDER BY with a CASE expression.
        String query = "SELECT * FROM routine_events ORDER BY day_of_week, start_time";

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

    /**
     * Delete exactly ONE matching row for the provided routine values.
     * This avoids deleting duplicates by:
     *   1) SELECTing a single id that matches the composite fields
     *   2) DELETEing by that id
     * Works without adding 'id' to Routine model.
     */
    public static int deleteOneMatching(Routine e) {
        final String selectOne =
                "SELECT id FROM routine_events " +
                        "WHERE course_name = ? " +
                        "  AND start_time  = ? " +
                        "  AND end_time    = ? " +
                        "  AND (room = ? OR (room IS NULL AND ? IS NULL)) " +
                        "  AND day_of_week = ? " +
                        "LIMIT 1";

        try (Connection conn = DB.getConnection();
             PreparedStatement sel = conn.prepareStatement(selectOne)) {

            sel.setString(1, e.getCourseName());
            sel.setString(2, e.getStartTime().toString());
            sel.setString(3, e.getEndTime().toString());
            sel.setString(4, e.getRoom());
            sel.setString(5, e.getRoom());
            sel.setString(6, e.getDayOfWeek());

            Integer idToDelete = null;
            try (ResultSet rs = sel.executeQuery()) {
                if (rs.next()) idToDelete = rs.getInt("id");
            }

            if (idToDelete == null) return 0;

            try (PreparedStatement del = conn.prepareStatement("DELETE FROM routine_events WHERE id = ?")) {
                del.setInt(1, idToDelete);
                return del.executeUpdate(); // 0 or 1
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return 0;
        }
    }
}
