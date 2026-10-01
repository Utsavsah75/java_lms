package com.library.dao;

import com.library.model.DashboardStats;
import com.library.util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** Numbers for the dashboards  (PHP: dashboard.php, user_dashboard.php). */
public class StatsDAO extends BaseDAO {

    public DashboardStats adminStats() {
        DashboardStats s = new DashboardStats();
        try (Connection c = DBConnection.getConnection()) {
            s.setBooks(count(c, "SELECT COUNT(*) FROM books", null));
            s.setStudents(count(c, "SELECT COUNT(*) FROM students", null));
            s.setIssues(count(c, "SELECT COUNT(*) FROM issue_book", null));
            s.setNotReturned(count(c, "SELECT COUNT(*) FROM issue_book WHERE retrunstatus IS NULL OR retrunstatus = 0", null));
            s.setAuthors(count(c, "SELECT COUNT(*) FROM bookauthor", null));
            s.setCategories(count(c, "SELECT COUNT(*) FROM bookcategory", null));
            s.setTotalFine(count(c, "SELECT COALESCE(SUM(fine), 0) FROM issue_book", null));
            return s;
        } catch (SQLException e) {
            throw wrap("Dashboard statistics", e);
        }
    }

    /** Student dashboard: total books in the library + the books this student has not returned yet. */
    public DashboardStats studentStats(String studentCode) {
        DashboardStats s = new DashboardStats();
        try (Connection c = DBConnection.getConnection()) {
            s.setBooks(count(c, "SELECT COUNT(*) FROM books", null));
            s.setIssues(count(c, "SELECT COUNT(*) FROM issue_book WHERE studentcode = ?", studentCode));
            s.setNotReturned(count(c,
                    "SELECT COUNT(*) FROM issue_book WHERE studentcode = ? AND (retrunstatus IS NULL OR retrunstatus = 0)",
                    studentCode));
            return s;
        } catch (SQLException e) {
            throw wrap("Student statistics", e);
        }
    }

    private static int count(Connection c, String sql, String arg) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            if (arg != null) {
                ps.setString(1, arg);
            }
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }
}
