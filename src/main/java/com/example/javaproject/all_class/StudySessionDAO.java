package com.example.javaproject.all_class;

import com.example.javaproject.DB;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;

/**
 * Data access object for StudySession. Provides methods to insert,
 * delete, and query study sessions, including weekly totals per course.
 */
public class StudySessionDAO {

    /** Return all study sessions for a given course. */
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
                        rs.getString("notes")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sessions;
    }

    /** Insert a new study session. */
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

    /** Delete a study session by ID. */
    public static void delete(int sessionId) {
        String sql = "DELETE FROM study_session WHERE id = ?";
        try (Connection conn = DB.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, sessionId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Aggregate total minutes spent studying per course within a date range.
     * Used when the user selects "ALL" courses on the analytics page.
     */
    public static Map<String, Integer> getWeeklyStudyTime(LocalDate startDate, LocalDate endDate) {
        Map<String, Integer> results = new LinkedHashMap<>();
        String sql = """
                SELECT DATE(substr(started_at,1,10)) AS study_date,
                       COALESCE(SUM(duration_min),0) AS total_minutes
                FROM study_session
                WHERE DATE(substr(started_at,1,10)) BETWEEN DATE(?) AND DATE(?)
                GROUP BY study_date
                ORDER BY study_date
        """;
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, startDate.toString());
            ps.setString(2, endDate.toString());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    results.put(rs.getString("study_date"), rs.getInt("total_minutes"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    /**
     * Aggregate total minutes spent studying **a specific course** within a date
     * range.
     * Used when the user selects an individual course from the dropdown on the
     * analytics page.
     *
     * @param courseId  the ID of the course to filter by
     * @param startDate the beginning of the date range (inclusive)
     * @param endDate   the end of the date range (inclusive)
     * @return a map (single entry) of course title to total minutes
     */
    public static Map<String, Integer> getWeeklyStudyTimeForCourse(int courseId,
            LocalDate startDate, LocalDate endDate) {
        Map<String, Integer> results = new LinkedHashMap<>();
        String sql = """
                SELECT DATE(substr(started_at,1,10)) AS study_date,
                       COALESCE(SUM(duration_min),0) AS total_minutes
                FROM study_session
                WHERE course_id = ?
                  AND DATE(substr(started_at,1,10)) BETWEEN DATE(?) AND DATE(?)
                GROUP BY study_date
                ORDER BY study_date
        """;
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, courseId);
            ps.setString(2, startDate.toString());
            ps.setString(3, endDate.toString());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    results.put(rs.getString("study_date"), rs.getInt("total_minutes"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }
}
