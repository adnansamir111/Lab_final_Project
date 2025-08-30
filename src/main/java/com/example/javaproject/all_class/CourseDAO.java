package com.example.javaproject.all_class;

import com.example.javaproject.DB;

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

    public static int getCourseIdByCode(String courseCode) {
        String sql = "SELECT id FROM course WHERE code = ?";  // SQL query to get the course_id by course_code
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, courseCode);  // Set the course_code parameter in the SQL query
            ResultSet rs = ps.executeQuery();  // Execute the query

            if (rs.next()) {
                return rs.getInt("id");  // Return the course_id (primary key)
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;  // Return -1 if no matching course found
    }
  /// getting all courses name in the task explorer
    public static List<String> getAllCourseNames() {
        List<String> courseNames = new ArrayList<>();
        String sql = "SELECT code FROM course ORDER BY code";  // Fetch all course codes

        try (Connection conn = DB.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                courseNames.add(rs.getString("code"));  // Add course code to list
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courseNames;
    }

    /// fetching course_name(code) by using the course id(primary key)
    public static String getCourseNameById(int courseId) {
        String courseName = "";
        String sql = "SELECT title FROM course WHERE id = ?";  // Fetch the course title using course_id
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, courseId);  // Set the course_id parameter in the SQL query
            ResultSet rs = ps.executeQuery();  // Execute the query

            if (rs.next()) {
                courseName = rs.getString("title");  // Get the course title (name)
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courseName;  // Return the course title (name)
    }




}
