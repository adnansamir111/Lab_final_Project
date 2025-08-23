package com.example.javaproject;

import java.sql.*;
import java.util.*;

public class StudySessionDAO {

    public static List<StudySession> listByCourse(int courseId) {
        List<StudySession> sessions = new ArrayList<>();
        String sql = "SELECT * FROM study_session WHERE course_id=? ORDER BY started_at DESC";
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, courseId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                sessions.add(new StudySession(
                        rs.getInt("id"),
                        rs.getInt("course_id"),
                        rs.getString("started_at"),
                        rs.getString("ended_at"),
                        rs.getInt("duration_min"),
                        rs.getString("notes")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sessions;
    }

    public static void insert(StudySession s) {
        String sql = "INSERT INTO study_session(course_id, started_at, ended_at, duration_min, notes) VALUES(?,?,?,?,?)";
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, s.getCourseId());
            ps.setString(2, s.getStartedAt());
            ps.setString(3, s.getEndedAt());
            ps.setInt(4, s.getDurationMin());
            ps.setString(5, s.getNotes());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

