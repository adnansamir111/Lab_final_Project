package com.example.javaproject;

import java.sql.*;
import java.util.*;

public class TaskDAO {

    public static List<Task> listAll() {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM task ORDER BY due_at";
        try (Connection conn = DB.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                tasks.add(new Task(
                        rs.getInt("id"),
                        rs.getInt("course_id"),
                        rs.getString("title"),
                        rs.getString("notes"),
                        rs.getString("due_at"),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    public static void insert(Task t) {
        String sql = "INSERT INTO task(course_id, title, notes, due_at, status) VALUES(?,?,?,?,?)";
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, t.getCourseId());
            ps.setString(2, t.getTitle());
            ps.setString(3, t.getNotes());
            ps.setString(4, t.getDueAt());
            ps.setString(5, t.getStatus());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void delete(int id) {
        String sql = "DELETE FROM task WHERE id=?";
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
