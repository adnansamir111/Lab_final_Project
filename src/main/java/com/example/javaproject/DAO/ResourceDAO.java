package com.example.javaproject.DAO;

import com.example.javaproject.DB;
import com.example.javaproject.all_class.Resource;

import java.sql.*;
import java.util.*;

public class ResourceDAO {

    // Insert a new resource into the database
    public static void insert(Resource resource) {
        String sql = "INSERT INTO resources (course_id, topic, video_link) VALUES (?, ?, ?)";
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, resource.getCourseId());  // Use course_id for foreign key
            ps.setString(2, resource.getTopic());  // Set topic name
            ps.setString(3, resource.getVideoLink());  // Set video link
            ps.executeUpdate();  // Execute the insert operation
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Retrieve a list of resources for a specific course id
    public static List<Resource> listByCourse(int courseId) {
        List<Resource> resources = new ArrayList<>();
        String sql = "SELECT * FROM resources WHERE course_id = ?";
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, courseId);  // Query by course_id (foreign key)
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // Map each row to a Resource object
                resources.add(new Resource(
                        rs.getInt("id"),
                        rs.getInt("course_id"),  // Use course_id (foreign key)
                        rs.getString("topic"),
                        rs.getString("video_link")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resources;  // Return the list of resources
    }
}
