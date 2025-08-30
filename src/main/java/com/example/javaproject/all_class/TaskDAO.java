package com.example.javaproject.all_class;

import com.example.javaproject.DB;

import java.sql.*;
import java.util.*;

public class TaskDAO {

    // ==================== List All ====================
    public static List<Task> listAll() {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM task ORDER BY due_at";
        try (Connection conn = DB.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                tasks.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    // ==================== Upcoming Tasks ====================
    public static List<Task> getUpcomingTasks(int days) {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM task " +
                "WHERE DATE(due_at) BETWEEN DATE('now') AND DATE('now', '+' || ? || ' days') " +
                "ORDER BY due_at";
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, days);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                tasks.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    // ==================== Notifications ====================
    public static List<Task> getDueReminders() {
        List<Task> tasks = new ArrayList<>();
        String sql = """
            SELECT * FROM task
            WHERE (DATE(due_at) = DATE('now') AND seen_dayof = 0)
               OR (DATE(due_at) = DATE('now', '+3 days') AND seen_3days = 0)
            ORDER BY due_at
        """;
        try (Connection conn = DB.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                tasks.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    // ==================== Mark Notification Seen ====================
    public static void markAsSeen(int id, boolean isThreeDays) {
        String sql = isThreeDays ?
                "UPDATE task SET seen_3days = 1 WHERE id=?" :
                "UPDATE task SET seen_dayof = 1 WHERE id=?";
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ==================== Insert ====================
    public static void insert(Task t) {
        String sql = "INSERT INTO task(course_id, title, notes, due_at, status, seen_3days, seen_dayof, completed_at) VALUES(?,?,?,?,?,?,?,?)";
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, t.getCourseId());
            ps.setString(2, t.getTitle());
            ps.setString(3, t.getNotes());
            ps.setString(4, t.getDueAt());
            ps.setString(5, t.getStatus());
            ps.setInt(6, t.getSeen3Days());
            ps.setInt(7, t.getSeenDayOf());
            ps.setString(8, t.getCompletedAt());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ==================== Delete ====================
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

    // ==================== List by Course ====================
    public static List<Task> listByCourse(int courseId) {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM task WHERE course_id = ? ORDER BY due_at";
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, courseId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                tasks.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    // ==================== List by Date ====================
    public static List<Task> listByDate(String date) {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM task WHERE DATE(due_at) = DATE(?) ORDER BY due_at";
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, date);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                tasks.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    // ==================== List by Date Range ====================
    public static List<Task> listByDateRange(String start, String end) {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM task WHERE DATE(due_at) BETWEEN DATE(?) AND DATE(?) ORDER BY due_at";
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, start);
            ps.setString(2, end);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                tasks.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    // ==================== List by Course and Date Range ====================
    public static List<Task> listByCourseAndDateRange(int courseId, String start, String end) {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM task WHERE course_id = ? AND DATE(due_at) BETWEEN DATE(?) AND DATE(?) ORDER BY due_at";
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, courseId);
            ps.setString(2, start);
            ps.setString(3, end);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                tasks.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    // ==================== Helper Method to Map ResultSet to Task ====================
    private static Task mapResultSet(ResultSet rs) throws SQLException {
        return new Task(
                rs.getInt("id"),
                rs.getInt("course_id"),
                rs.getString("title"),
                rs.getString("notes"),
                rs.getString("due_at"),
                rs.getString("status"),
                rs.getInt("seen_3days"),
                rs.getInt("seen_dayof"),
                rs.getString("completed_at")
        );
    }
    //update
    public static void update(Task t) {
        String sql = "UPDATE task SET title=?, notes=?, due_at=?, status=? WHERE id=?";
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, t.getTitle());
            ps.setString(2, t.getNotes());
            ps.setString(3, t.getDueAt());
            ps.setString(4, t.getStatus());
            ps.setInt(5, t.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
