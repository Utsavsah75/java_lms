package com.library.dao;

import com.library.model.Author;
import com.library.util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/** Table bookauthor  (PHP: add_author.php, manage_author.php, edit_author.php). */
public class AuthorDAO extends BaseDAO {

    private static final String SELECT = "SELECT ba.*, (SELECT COUNT(*) FROM books b WHERE b.author = ba.id) AS book_count FROM bookauthor ba";

    private static Author map(ResultSet rs) throws SQLException {
        Author a = new Author();
        a.setId(rs.getInt("id"));
        a.setName(rs.getString("AuthorName"));
        a.setCreationDate(date(rs, "creationDate"));
        a.setUpdationDate(date(rs, "UpdationDate"));
        a.setBookCount(rs.getInt("book_count"));
        return a;
    }

    public List<Author> findAll() {
        List<Author> list = new ArrayList<>();
        try (Connection c = DBConnection.getConnection();
                PreparedStatement ps = c.prepareStatement(SELECT + " ORDER BY ba.id");
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(map(rs));
            }
            return list;
        } catch (SQLException e) {
            throw wrap("Author list", e);
        }
    }

    public Author findById(int id) {
        try (Connection c = DBConnection.getConnection();
                PreparedStatement ps = c.prepareStatement(SELECT + " WHERE ba.id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        } catch (SQLException e) {
            throw wrap("Author lookup", e);
        }
    }

    public boolean nameExists(String name, int exceptId) {
        String sql = "SELECT COUNT(*) FROM bookauthor WHERE LOWER(TRIM(AuthorName)) = LOWER(?) AND id <> ?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, name.trim());
            ps.setInt(2, exceptId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw wrap("Author duplicate check", e);
        }
    }

    public boolean insert(String name) {
        try (Connection c = DBConnection.getConnection();
                PreparedStatement ps = c.prepareStatement("INSERT INTO bookauthor (AuthorName) VALUES (?)")) {
            ps.setString(1, name);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw wrap("Author insert", e);
        }
    }

    public boolean update(int id, String name) {
        try (Connection c = DBConnection.getConnection();
                PreparedStatement ps = c.prepareStatement("UPDATE bookauthor SET AuthorName = ? WHERE id = ?")) {
            ps.setString(1, name);
            ps.setInt(2, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw wrap("Author update", e);
        }
    }

    public boolean delete(int id) {
        try (Connection c = DBConnection.getConnection();
                PreparedStatement ps = c.prepareStatement("DELETE FROM bookauthor WHERE id = ?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw wrap("Author delete", e);
        }
    }
}
