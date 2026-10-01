package com.library.service;

import com.library.dao.BookDAO;
import com.library.dao.IssueDAO;
import com.library.dao.StudentDAO;
import com.library.model.IssueRecord;
import com.library.model.Student;
import com.library.util.BusinessException;
import com.library.util.DBConnection;
import com.library.util.DataAccessException;
import com.library.util.Validator;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Issue and return of books (PHP: issue_book.php, edit_issue_book.php).
 * Both operations run in ONE database transaction and lock the book row, so the
 * "copies available" number can never go wrong when two librarians work at once.
 */
public class IssueService {

    private final IssueDAO issueDAO = new IssueDAO();
    private final BookDAO bookDAO = new BookDAO();
    private final StudentDAO studentDAO = new StudentDAO();

    /** Issues one copy of the book (ISBN) to the student (student code). */
    public void issueBook(String studentCode, String isbn) throws BusinessException {
        studentCode = Validator.trim(studentCode);
        isbn = Validator.trim(isbn);
        if (studentCode.isEmpty()) {
            throw new BusinessException("Please select a student.");
        }
        if (isbn.isEmpty()) {
            throw new BusinessException("Please enter the book ISBN / Book ID.");
        }
        Student student = studentDAO.findByCode(studentCode);
        if (student == null) {
            throw new BusinessException("Student not found.");
        }
        if (!student.isActive()) {
            throw new BusinessException("This student account is blocked or not verified. Activate it first.");
        }
        try (Connection c = DBConnection.getConnection()) {
            c.setAutoCommit(false);
            try {
                int[] stock = bookDAO.lockAndCount(c, isbn);           // {id, quantity, copiesOut}
                if (stock == null) {
                    throw new BusinessException("Book not found. Check the ISBN / Book ID.");
                }
                if (issueDAO.activeIssueExists(c, studentCode, isbn)) {
                    throw new BusinessException("This student already has this book and has not returned it yet.");
                }
                if (stock[1] - stock[2] <= 0) {
                    throw new BusinessException("Book is currently unavailable.");
                }
                issueDAO.insert(c, studentCode, isbn);
                bookDAO.refreshIssuedFlag(c, isbn);
                c.commit();
            } catch (BusinessException | SQLException | RuntimeException e) {
                c.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Issue book failed: " + e.getMessage(), e);
        }
    }

    /** Marks an issue record as returned and stores the fine. */
    public void returnBook(int issueId, String fineText) throws BusinessException {
        String f = Validator.trim(fineText);
        if (f.isEmpty()) {
            f = "0";
        }
        if (!Validator.isNonNegativeInt(f) || f.length() > 9) {
            throw new BusinessException("Fine must be a whole number (0 or more).");
        }
        int fine = Integer.parseInt(f);
        IssueRecord rec = issueDAO.findById(issueId);
        if (rec == null) {
            throw new BusinessException("Issue record not found.");
        }
        if (rec.isReturned()) {
            throw new BusinessException("This book has already been returned.");
        }
        try (Connection c = DBConnection.getConnection()) {
            c.setAutoCommit(false);
            try {
                bookDAO.lockAndCount(c, rec.getIsbn());
                String isbn = issueDAO.markReturned(c, issueId, fine);
                if (isbn == null) {
                    throw new BusinessException("This book has already been returned.");
                }
                bookDAO.refreshIssuedFlag(c, isbn);
                c.commit();
            } catch (BusinessException | SQLException | RuntimeException e) {
                c.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Return book failed: " + e.getMessage(), e);
        }
    }
}
