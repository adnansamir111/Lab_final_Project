package com.example.javaproject.all_class;

import com.example.javaproject.DB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO {

    // --- Existing migration for marks (kept) ---
    private static void ensureMarkColumns() {
        final String[] alters = new String[] {
                "ALTER TABLE course ADD COLUMN quiz1 REAL",
                "ALTER TABLE course ADD COLUMN quiz2 REAL",
                "ALTER TABLE course ADD COLUMN quiz3 REAL",
                "ALTER TABLE course ADD COLUMN quiz4 REAL",
                "ALTER TABLE course ADD COLUMN mid REAL",
                "ALTER TABLE course ADD COLUMN \"final\" REAL"
        };
        try (Connection conn = DB.getConnection();
             Statement st = conn.createStatement()) {

            // ensure base table (defensive)
            st.executeUpdate("CREATE TABLE IF NOT EXISTS course (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "code TEXT, title TEXT, instructor TEXT, credits REAL" +
                    ")");

            // add columns if missing (safe ALTER)
            for (String sql : alters) {
                try {
                    st.executeUpdate(sql);
                } catch (SQLException ignored) { /* already exists */ }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // --- NEW: migration for classroom_url (kept minimal and safe) ---
    private static void ensureClassroomColumn() {
        try (Connection conn = DB.getConnection();
             Statement st = conn.createStatement()) {
            // ensure table exists
            st.executeUpdate("CREATE TABLE IF NOT EXISTS course (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "code TEXT, title TEXT, instructor TEXT, credits REAL" +
                    ")");

            boolean hasColumn = false;
            try (ResultSet rs = st.executeQuery("PRAGMA table_info(course)")) {
                while (rs.next()) {
                    if ("classroom_url".equalsIgnoreCase(rs.getString("name"))) {
                        hasColumn = true;
                        break;
                    }
                }
            }
            if (!hasColumn) {
                st.executeUpdate("ALTER TABLE course ADD COLUMN classroom_url TEXT");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ---------- CRUD / Queries ----------

    public static List<Course> listAll() {
        ensureMarkColumns();
        ensureClassroomColumn();

        List<Course> courses = new ArrayList<>();
        String sql =
                "SELECT id, code, title, instructor, credits, " +
                        "       quiz1, quiz2, quiz3, quiz4, mid, \"final\" AS fin, " +
                        "       classroom_url " +
                        "FROM course " +
                        "ORDER BY code";

        try (Connection conn = DB.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                int id           = rs.getInt("id");
                String code      = rs.getString("code");
                String title     = rs.getString("title");
                String instr     = rs.getString("instructor");
                double credits   = rs.getDouble("credits");

                Course c = new Course(id, code, title, instr, credits);

                // marks (defensive null → 0.0)
                c.setQuiz1(safeGetDouble(rs, "quiz1"));
                c.setQuiz2(safeGetDouble(rs, "quiz2"));
                c.setQuiz3(safeGetDouble(rs, "quiz3"));
                c.setQuiz4(safeGetDouble(rs, "quiz4"));
                c.setMid(safeGetDouble(rs, "mid"));
                c.setFin(safeGetDouble(rs, "fin"));

                // classroom link
                c.setClassroomUrl(rs.getString("classroom_url"));

                courses.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;
    }

    public static void insert(Course c) {
        ensureMarkColumns();
        ensureClassroomColumn();

        String sql =
                "INSERT INTO course (code, title, instructor, credits, " +
                        "                    quiz1, quiz2, quiz3, quiz4, mid, \"final\", classroom_url) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, c.getCode());
            ps.setString(2, c.getTitle());
            ps.setString(3, c.getInstructor());
            ps.setDouble(4, c.getCredits());

            ps.setObject(5,  nullToDouble(c.getQuiz1()));
            ps.setObject(6,  nullToDouble(c.getQuiz2()));
            ps.setObject(7,  nullToDouble(c.getQuiz3()));
            ps.setObject(8,  nullToDouble(c.getQuiz4()));
            ps.setObject(9,  nullToDouble(c.getMid()));
            ps.setObject(10, nullToDouble(c.getFin()));

            String url = c.getClassroomUrl();
            if (url == null || url.isBlank()) ps.setNull(11, Types.VARCHAR);
            else ps.setString(11, url.trim());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** Update only marks — kept to match your GradeSheetController usage. */
    public static void updateMarks(int id, Double q1, Double q2, Double q3, Double q4, Double mid, Double fin) {
        ensureMarkColumns();
        String sql = "UPDATE course SET quiz1=?, quiz2=?, quiz3=?, quiz4=?, mid=?, \"final\"=? WHERE id=?";
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            setNullableDouble(ps, 1, q1);
            setNullableDouble(ps, 2, q2);
            setNullableDouble(ps, 3, q3);
            setNullableDouble(ps, 4, q4);
            setNullableDouble(ps, 5, mid);
            setNullableDouble(ps, 6, fin);
            ps.setInt(7, id);

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** NEW: Update classroom link only. */
    public static void updateClassroomUrl(int id, String url) {
        ensureClassroomColumn();
        String sql = "UPDATE course SET classroom_url = ? WHERE id = ?";
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (url == null || url.isBlank()) {
                ps.setNull(1, Types.VARCHAR);
            } else {
                ps.setString(1, url.trim());
            }
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void delete(int id) {
        String sql = "DELETE FROM course WHERE id = ?";
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // -------- Helper lookups (used in TaskExplorer, etc.) --------

    public static List<String> getAllCourseNames() {
        List<String> names = new ArrayList<>();
        String sql = "SELECT code FROM course ORDER BY code";
        try (Connection conn = DB.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                names.add(rs.getString("code"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return names;
    }

    public static int getCourseIdByCode(String code) {
        String sql = "SELECT id FROM course WHERE code = ?";
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, code);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static String getCourseNameById(int courseId) {
        String name = "";
        String sql = "SELECT title FROM course WHERE id = ?";
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, courseId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) name = rs.getString("title");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return name;
    }

    // -------- Small utils --------

    private static double safeGetDouble(ResultSet rs, String col) throws SQLException {
        double v = rs.getDouble(col);
        if (rs.wasNull()) return 0.0;
        return v;
    }

    private static void setNullableDouble(PreparedStatement ps, int index, Double value) throws SQLException {
        if (value == null) ps.setNull(index, Types.REAL);
        else ps.setDouble(index, value);
    }

    private static Double nullToDouble(double v) {
        // In your model, marks are primitive doubles (default 0).
        // Keep existing behavior: store 0.0 by default.
        return v;
    }
}
