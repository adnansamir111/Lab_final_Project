package com.example.javaproject.DAO;

import com.example.javaproject.all_class.Chapter;
import com.example.javaproject.DB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChapterDAO {

    // Method to insert a new chapter into the database
    public static void insert(Chapter chapter) {
        String sql = "INSERT INTO chapters (course_id, chapter_name, is_completed) VALUES (?, ?, ?)";

        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, chapter.getCourseId());
            stmt.setString(2, chapter.getChapterName());
            stmt.setBoolean(3, chapter.isCompleted());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to list all chapters for a specific course
    public static List<Chapter> listByCourse(int courseId) {
        List<Chapter> chapters = new ArrayList<>();
        String sql = "SELECT * FROM chapters WHERE course_id = ?";

        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, courseId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Chapter chapter = new Chapter(
                        rs.getInt("id"),
                        rs.getInt("course_id"),
                        rs.getString("chapter_name"),
                        rs.getBoolean("is_completed")
                );
                chapters.add(chapter);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return chapters;
    }

    // Method to update the completion status of a chapter
    public static void updateCompletionStatus(int chapterId, boolean isCompleted) {
        String sql = "UPDATE chapters SET is_completed = ? WHERE id = ?";

        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBoolean(1, isCompleted);
            stmt.setInt(2, chapterId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to delete chapters associated with a specific course
    public static void deleteByCourseId(int chapterId) {
        try (Connection conn = DB.getConnection()) {
            String sql = "DELETE FROM chapters WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, chapterId);
                int affectedRows = stmt.executeUpdate();

                if (affectedRows == 0) {
                    System.out.println("No chapter found with the given ID to delete.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
