package com.example.javaproject.DAO;

import com.example.javaproject.DB;
import com.example.javaproject.all_class.Course;

import java.sql.*;
import java.util.*;

public class CourseDAO {

    // --- One-time, minimal migration: add mark columns if they don't exist ---
    // Called at the start of listAll()/insert()/updateMarks() to keep it transparent.
    private static void ensureMarkColumns() {
        final String[] alters = new String[] {
                "ALTER TABLE course ADD COLUMN quiz1 REAL",
                "ALTER TABLE course ADD COLUMN quiz2 REAL",
                "ALTER TABLE course ADD COLUMN quiz3 REAL",
                "ALTER TABLE course ADD COLUMN quiz4 REAL",
                "ALTER TABLE course ADD COLUMN mid   REAL",
                // "final" is a keyword in Java, but OK in SQLite; quote it in UPDATE statements just in case.
                "ALTER TABLE course ADD COLUMN \"final\" REAL"
        };
        try (Connection conn = DB.getConnection();
             Statement st = conn.createStatement()) {
            for (String sql : alters) {
                try { st.executeUpdate(sql); } catch (SQLException ignored) { /* column already exists */ }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Course> listAll() {
        ensureMarkColumns();
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT id, code, title, instructor, credits, " +
                "quiz1, quiz2, quiz3, quiz4, mid, \"final\" AS fin " +
                "FROM course ORDER BY code";
        try (Connection conn = DB.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                int id          = rs.getInt("id");
                String code     = rs.getString("code");
                String title    = rs.getString("title");
                String instr    = rs.getString("instructor");
                double  credits     = rs.getDouble("credits");

                // Use wrapper reads so null stays null (otherwise getDouble→0.0)
                Double q1 = (rs.getObject("quiz1") != null) ? rs.getDouble("quiz1") : null;
                Double q2 = (rs.getObject("quiz2") != null) ? rs.getDouble("quiz2") : null;
                Double q3 = (rs.getObject("quiz3") != null) ? rs.getDouble("quiz3") : null;
                Double q4 = (rs.getObject("quiz4") != null) ? rs.getDouble("quiz4") : null;
                Double mid= (rs.getObject("mid")   != null) ? rs.getDouble("mid")   : null;
                Double fin= (rs.getObject("fin")   != null) ? rs.getDouble("fin")   : null;

                Course c = new Course(id, code, title, instr, credits);
                if (q1 != null) c.setQuiz1(q1);
                if (q2 != null) c.setQuiz2(q2);
                if (q3 != null) c.setQuiz3(q3);
                if (q4 != null) c.setQuiz4(q4);
                if (mid!= null) c.setMid(mid);
                if (fin!= null) c.setFin(fin);

                courses.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;
    }

    public static void insert(Course c) {
        ensureMarkColumns();
        // Keep original minimal insert; marks are optional (NULL by default)
        String sql = "INSERT INTO course (code, title, instructor, credits, quiz1, quiz2, quiz3, quiz4, mid, \"final\") " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getCode());
            ps.setString(2, c.getTitle());
            ps.setString(3, c.getInstructor());
            ps.setDouble(4, c.getCredits());
            // allow nulls for marks if not set (use setObject)
            ps.setObject(5,  c.quiz1Property().get(), Types.REAL);
            ps.setObject(6,  c.quiz2Property().get(), Types.REAL);
            ps.setObject(7,  c.quiz3Property().get(), Types.REAL);
            ps.setObject(8,  c.quiz4Property().get(), Types.REAL);
            ps.setObject(9,  c.midProperty().get(),   Types.REAL);
            ps.setObject(10, c.finProperty().get(),   Types.REAL);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Minimal helper to update marks later from Gradesheet (course row already exists)
    public static void updateMarks(int courseId,
                                   Double quiz1, Double quiz2, Double quiz3, Double quiz4,
                                   Double mid, Double fin) {
        ensureMarkColumns();
        String sql = "UPDATE course SET " +
                "quiz1=?, quiz2=?, quiz3=?, quiz4=?, mid=?, \"final\"=? " +
                "WHERE id=?";
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (quiz1 == null) ps.setNull(1, Types.REAL); else ps.setDouble(1, quiz1);
            if (quiz2 == null) ps.setNull(2, Types.REAL); else ps.setDouble(2, quiz2);
            if (quiz3 == null) ps.setNull(3, Types.REAL); else ps.setDouble(3, quiz3);
            if (quiz4 == null) ps.setNull(4, Types.REAL); else ps.setDouble(4, quiz4);
            if (mid   == null) ps.setNull(5, Types.REAL); else ps.setDouble(5, mid);
            if (fin   == null) ps.setNull(6, Types.REAL); else ps.setDouble(6, fin);
            ps.setInt(7, courseId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void delete(int id) {
        ensureMarkColumns(); // harmless
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
