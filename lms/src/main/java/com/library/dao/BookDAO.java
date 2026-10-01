package com.library.dao;

import com.library.model.Book;
import com.library.util.DBConnection;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/** Table books  (PHP: add_book.php, manage_book.php, edit_book.php, change_image.php, delete_book.php). */
public class BookDAO extends BaseDAO {

    /** issued_count = copies that are out right now (issue rows that are not returned yet). */
    private static final String SELECT =
            "SELECT b.*, bc.CategoryName, ba.AuthorName, "
            + "(SELECT COUNT(*) FROM issue_book i WHERE i.book_id = b.isbnno "
            + "   AND (i.retrunstatus IS NULL OR i.retrunstatus = 0)) AS issued_count "
            + "FROM books b JOIN bookcategory bc ON bc.id = b.catid JOIN bookauthor ba ON ba.id = b.author";

    private static Book map(ResultSet rs) throws SQLException {
        Book b = new Book();
        b.setId(rs.getInt("id"));
        b.setName(rs.getString("BookName"));
        b.setCategoryId(rs.getInt("catid"));
        b.setCategoryName(rs.getString("CategoryName"));
        b.setAuthorId(rs.getInt("author"));
        b.setAuthorName(rs.getString("AuthorName"));
        b.setIsbn(rs.getString("isbnno"));
        b.setQuantity(rs.getInt("quantity"));
        b.setPrice(rs.getBigDecimal("bookprice"));
        b.setImage(rs.getString("bookimg"));
        b.setRegDate(date(rs, "regdate"));
        b.setUpdationDate(date(rs, "updationdate"));
        b.setIssuedCount(rs.getInt("issued_count"));
        return b;
    }

    /** All books, optionally filtered by a search text (name, ISBN, author or category). */
    public List<Book> search(String q) {
        boolean filter = q != null && !q.isBlank();
        String sql = SELECT + (filter
                ? " WHERE b.BookName LIKE ? OR b.isbnno LIKE ? OR ba.AuthorName LIKE ? OR bc.CategoryName LIKE ?"
                : "") + " ORDER BY b.id";
        List<Book> list = new ArrayList<>();
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            if (filter) {
                String like = "%" + q.trim() + "%";
                for (int i = 1; i <= 4; i++) {
                    ps.setString(i, like);
                }
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
            return list;
        } catch (SQLException e) {
            throw wrap("Book search", e);
        }
    }

    public Book findById(int id) {
        return findOne(SELECT + " WHERE b.id = ?", String.valueOf(id), true);
    }

    public Book findByIsbn(String isbn) {
        return findOne(SELECT + " WHERE b.isbnno = ?", isbn, false);
    }

    private Book findOne(String sql, String value, boolean numeric) {
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            if (numeric) {
                ps.setInt(1, Integer.parseInt(value));
            } else {
                ps.setString(1, value);
            }
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        } catch (SQLException e) {
            throw wrap("Book lookup", e);
        }
    }

    public boolean isbnExists(String isbn) {
        return exists("SELECT COUNT(*) FROM books WHERE isbnno = ?", isbn);
    }

    public boolean nameExists(String name) {
        return exists("SELECT COUNT(*) FROM books WHERE BookName = ?", name);
    }

    private boolean exists(String sql, String value) {
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, value);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw wrap("Book duplicate check", e);
        }
    }

    public int insert(Book b) {
        String sql = "INSERT INTO books (BookName, catid, author, isbnno, quantity, bookprice, bookimg) VALUES (?,?,?,?,?,?,?)";
        try (Connection c = DBConnection.getConnection();
                PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, b.getName());
            ps.setInt(2, b.getCategoryId());
            ps.setInt(3, b.getAuthorId());
            ps.setString(4, b.getIsbn());
            ps.setInt(5, b.getQuantity());
            ps.setBigDecimal(6, b.getPrice());
            ps.setString(7, b.getImage());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                return keys.next() ? keys.getInt(1) : 0;
            }
        } catch (SQLException e) {
            throw wrap("Book insert", e);
        }
    }

    /** ISBN is not editable (issue_book points to it) - same as the PHP edit form. */
    public boolean update(Book b) {
        String sql = "UPDATE books SET BookName = ?, catid = ?, author = ?, bookprice = ?, quantity = ? WHERE id = ?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, b.getName());
            ps.setInt(2, b.getCategoryId());
            ps.setInt(3, b.getAuthorId());
            ps.setBigDecimal(4, b.getPrice() == null ? BigDecimal.ZERO : b.getPrice());
            ps.setInt(5, b.getQuantity());
            ps.setInt(6, b.getId());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw wrap("Book update", e);
        }
    }

    public boolean updateImage(int id, String fileName) {
        try (Connection c = DBConnection.getConnection();
                PreparedStatement ps = c.prepareStatement("UPDATE books SET bookimg = ? WHERE id = ?")) {
            ps.setString(1, fileName);
            ps.setInt(2, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw wrap("Book image update", e);
        }
    }

    public boolean delete(int id) {
        try (Connection c = DBConnection.getConnection();
                PreparedStatement ps = c.prepareStatement("DELETE FROM books WHERE id = ?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw wrap("Book delete", e);
        }
    }

    // ---- used inside the issue / return transaction ------------------------------------------

    /** Locks the book row so two librarians cannot issue the last copy at the same time. */
    public int[] lockAndCount(Connection c, String isbn) throws SQLException {
        int id;
        int qty;
        try (PreparedStatement ps = c.prepareStatement("SELECT id, quantity FROM books WHERE isbnno = ? FOR UPDATE")) {
            ps.setString(1, isbn);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                id = rs.getInt(1);
                qty = rs.getInt(2);
            }
        }
        int out = 0;
        try (PreparedStatement ps = c.prepareStatement(
                "SELECT COUNT(*) FROM issue_book WHERE book_id = ? AND (retrunstatus IS NULL OR retrunstatus = 0)")) {
            ps.setString(1, isbn);
            try (ResultSet rs = ps.executeQuery()) {
                out = rs.next() ? rs.getInt(1) : 0;
            }
        }
        return new int[] {id, qty, out};
    }

    /** Keeps the legacy books.issued flag meaningful: 1 = no copy left on the shelf. */
    public void refreshIssuedFlag(Connection c, String isbn) throws SQLException {
        String sql = "UPDATE books b SET b.issued = IF((SELECT COUNT(*) FROM issue_book i WHERE i.book_id = b.isbnno "
                + "AND (i.retrunstatus IS NULL OR i.retrunstatus = 0)) >= b.quantity, 1, 0) WHERE b.isbnno = ?";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, isbn);
            ps.executeUpdate();
        }
    }
}
