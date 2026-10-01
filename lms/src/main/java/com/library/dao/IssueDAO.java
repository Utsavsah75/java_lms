package com.library.dao;

import com.library.model.IssueRecord;
import com.library.util.AppConfig;
import com.library.util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Table issue_book  (PHP: issue_book.php, manage_issue_book.php, edit_issue_book.php,
 * students_detail.php, borrow_book.php).
 * Note: issue_book.book_id holds the book ISBN (foreign key to books.isbnno), not books.id.
 */
public class IssueDAO extends BaseDAO {

    private static final String SELECT =
            "SELECT i.id, i.studentcode, i.book_id, i.issue_date, i.returndate, i.retrunstatus, i.fine, "
            + "s.std_name, s.email, s.phone, b.id AS bid, b.BookName, b.bookimg "
            + "FROM issue_book i JOIN students s ON s.studentcode = i.studentcode JOIN books b ON b.isbnno = i.book_id";

    private static final int LOAN_DAYS = AppConfig.getInt("loan.days", 14);
    private static final int FINE_PER_DAY = AppConfig.getInt("fine.per.day", 5);

    private static IssueRecord map(ResultSet rs) throws SQLException {
        IssueRecord r = new IssueRecord();
        r.setId(rs.getInt("id"));
        r.setStudentCode(rs.getString("studentcode"));
        r.setStudentName(rs.getString("std_name"));
        r.setStudentEmail(rs.getString("email"));
        r.setStudentPhone(rs.getString("phone"));
        r.setBookId(rs.getInt("bid"));
        r.setIsbn(rs.getString("book_id"));
        r.setBookName(rs.getString("BookName"));
        r.setBookImage(rs.getString("bookimg"));
        r.setIssueDate(date(rs, "issue_date"));
        r.setReturnDate(date(rs, "returndate"));
        r.setReturnStatus(rs.getInt("retrunstatus"));
        r.setFine(rs.getInt("fine"));
        fillDueInfo(r);
        return r;
    }

    /** Due date = issue date + loan.days. Suggested fine = late days x fine.per.day (only for books still out). */
    private static void fillDueInfo(IssueRecord r) {
        if (r.getIssueDate() == null) {
            return;
        }
        LocalDate issued = toLocal(r.getIssueDate());
        LocalDate due = issued.plusDays(LOAN_DAYS);
        r.setDueDate(Date.from(due.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        LocalDate end = r.isReturned() && r.getReturnDate() != null ? toLocal(r.getReturnDate()) : LocalDate.now();
        int late = (int) Math.max(0, ChronoUnit.DAYS.between(due, end));
        r.setOverdueDays(late);
        r.setSuggestedFine(r.isReturned() ? r.getFine() : late * FINE_PER_DAY);
    }

    private static LocalDate toLocal(Date d) {
        return new java.sql.Timestamp(d.getTime()).toLocalDateTime().toLocalDate();
    }

    /** All records, newest first, optionally between two dates (inclusive) on the issue date. */
    public List<IssueRecord> findAll(String from, String to) {
        StringBuilder sql = new StringBuilder(SELECT);
        List<String> args = new ArrayList<>();
        if (from != null && !from.isEmpty()) {
            sql.append(args.isEmpty() ? " WHERE" : " AND").append(" DATE(i.issue_date) >= ?");
            args.add(from);
        }
        if (to != null && !to.isEmpty()) {
            sql.append(args.isEmpty() ? " WHERE" : " AND").append(" DATE(i.issue_date) <= ?");
            args.add(to);
        }
        sql.append(" ORDER BY i.id DESC");
        return run(sql.toString(), args);
    }

    public List<IssueRecord> findByStudent(String studentCode) {
        return run(SELECT + " WHERE i.studentcode = ? ORDER BY i.id DESC", List.of(studentCode));
    }

    private List<IssueRecord> run(String sql, List<String> args) {
        List<IssueRecord> list = new ArrayList<>();
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            for (int i = 0; i < args.size(); i++) {
                ps.setString(i + 1, args.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
            return list;
        } catch (SQLException e) {
            throw wrap("Issue list", e);
        }
    }

    public IssueRecord findById(int id) {
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(SELECT + " WHERE i.id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        } catch (SQLException e) {
            throw wrap("Issue lookup", e);
        }
    }

    // ---- transaction parts (the connection is owned by IssueService) -------------------------

    public boolean activeIssueExists(Connection c, String studentCode, String isbn) throws SQLException {
        String sql = "SELECT COUNT(*) FROM issue_book WHERE studentcode = ? AND book_id = ? AND (retrunstatus IS NULL OR retrunstatus = 0)";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, studentCode);
            ps.setString(2, isbn);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    public void insert(Connection c, String studentCode, String isbn) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement("INSERT INTO issue_book (studentcode, book_id) VALUES (?, ?)")) {
            ps.setString(1, studentCode);
            ps.setString(2, isbn);
            ps.executeUpdate();
        }
    }

    /** Returns the ISBN of the returned book, or null when the record does not exist / was already returned. */
    public String markReturned(Connection c, int issueId, int fine) throws SQLException {
        String isbn;
        try (PreparedStatement ps = c.prepareStatement(
                "SELECT book_id FROM issue_book WHERE id = ? AND (retrunstatus IS NULL OR retrunstatus = 0) FOR UPDATE")) {
            ps.setInt(1, issueId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                isbn = rs.getString(1);
            }
        }
        try (PreparedStatement ps = c.prepareStatement(
                "UPDATE issue_book SET returndate = NOW(), fine = ?, retrunstatus = 1 WHERE id = ?")) {
            ps.setInt(1, fine);
            ps.setInt(2, issueId);
            ps.executeUpdate();
        }
        return isbn;
    }
}
