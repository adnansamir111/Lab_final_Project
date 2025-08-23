package com.example.javaproject;

import java.sql.*;
import java.util.*;

public class CourseDAO {

    public static List<Course> listAll() {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM course ORDER BY code";
        try (Connection conn = DB.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                courses.add(new Course(
                        rs.getInt("id"),
                        rs.getString("code"),
                        rs.getString("title"),
                        rs.getString("instructor"),
                        rs.getInt("credits")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;
    }

    public static void insert(Course c) {
        String sql = "INSERT INTO course(code, title, instructor, credits) VALUES (?,?,?,?)";
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getCode());
            ps.setString(2, c.getTitle());
            ps.setString(3, c.getInstructor());
            ps.setInt(4, c.getCredits());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void delete(int id) {
        String sql = "DELETE FROM course WHERE id=?";
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
