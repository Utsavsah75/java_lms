package com.library.dao;

import com.library.model.Category;
import com.library.util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/** Table bookcategory  (PHP: add_category.php, manage_category.php, edit_category.php). */
public class CategoryDAO extends BaseDAO {

    private static Category map(ResultSet rs) throws SQLException {
        Category c = new Category();
        c.setId(rs.getInt("id"));
        c.setName(rs.getString("CategoryName"));
        c.setStatus(rs.getInt("Status"));
        c.setCreationDate(date(rs, "CreationDate"));
        c.setUpdationDate(date(rs, "UpdationDate"));
        c.setBookCount(rs.getInt("book_count"));
        return c;
    }

    private static final String SELECT = "SELECT bc.*, (SELECT COUNT(*) FROM books b WHERE b.catid = bc.id) AS book_count FROM bookcategory bc";

    public List<Category> findAll() {
        return query(SELECT + " ORDER BY bc.id");
    }

    /** Only active categories - used by the Add/Edit Book drop-downs. */
    public List<Category> findActive() {
        return query(SELECT + " WHERE bc.Status = 1 ORDER BY bc.CategoryName");
    }

    private List<Category> query(String sql) {
        List<Category> list = new ArrayList<>();
        try (Connection c = DBConnection.getConnection();
                PreparedStatement ps = c.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(map(rs));
            }
            return list;
        } catch (SQLException e) {
            throw wrap("Category list", e);
        }
    }

    public Category findById(int id) {
        try (Connection c = DBConnection.getConnection();
                PreparedStatement ps = c.prepareStatement(SELECT + " WHERE bc.id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        } catch (SQLException e) {
            throw wrap("Category lookup", e);
        }
    }

    public boolean nameExists(String name, int exceptId) {
        String sql = "SELECT COUNT(*) FROM bookcategory WHERE LOWER(TRIM(CategoryName)) = LOWER(?) AND id <> ?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, name.trim());
            ps.setInt(2, exceptId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw wrap("Category duplicate check", e);
        }
    }

    public boolean insert(String name, int status) {
        try (Connection c = DBConnection.getConnection();
                PreparedStatement ps = c.prepareStatement("INSERT INTO bookcategory (CategoryName, Status) VALUES (?, ?)")) {
            ps.setString(1, name);
            ps.setInt(2, status);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw wrap("Category insert", e);
        }
    }

    public boolean update(int id, String name, int status) {
        try (Connection c = DBConnection.getConnection();
                PreparedStatement ps = c.prepareStatement("UPDATE bookcategory SET CategoryName = ?, Status = ? WHERE id = ?")) {
            ps.setString(1, name);
            ps.setInt(2, status);
            ps.setInt(3, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw wrap("Category update", e);
        }
    }

    public boolean setStatus(int id, int status) {
        try (Connection c = DBConnection.getConnection();
                PreparedStatement ps = c.prepareStatement("UPDATE bookcategory SET Status = ? WHERE id = ?")) {
            ps.setInt(1, status);
            ps.setInt(2, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw wrap("Category status", e);
        }
    }

    public boolean delete(int id) {
        try (Connection c = DBConnection.getConnection();
                PreparedStatement ps = c.prepareStatement("DELETE FROM bookcategory WHERE id = ?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw wrap("Category delete", e);
        }
    }
}
