package com.library.model;

/** Row of table issue_book joined with student and book. */
public class IssueRecord implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String studentCode;
    private String studentName;
    private String studentEmail;
    private String studentPhone;
    private int bookId;
    private String isbn;
    private String bookName;
    private String bookImage;
    private java.util.Date issueDate;
    private java.util.Date returnDate;
    private int returnStatus;
    private int fine;
    private java.util.Date dueDate;
    private int overdueDays;
    private int suggestedFine;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getStudentCode() { return studentCode; }
    public void setStudentCode(String studentCode) { this.studentCode = studentCode; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getStudentEmail() { return studentEmail; }
    public void setStudentEmail(String studentEmail) { this.studentEmail = studentEmail; }

    public String getStudentPhone() { return studentPhone; }
    public void setStudentPhone(String studentPhone) { this.studentPhone = studentPhone; }

    public int getBookId() { return bookId; }
    public void setBookId(int bookId) { this.bookId = bookId; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public String getBookName() { return bookName; }
    public void setBookName(String bookName) { this.bookName = bookName; }

    public String getBookImage() { return bookImage; }
    public void setBookImage(String bookImage) { this.bookImage = bookImage; }

    public java.util.Date getIssueDate() { return issueDate; }
    public void setIssueDate(java.util.Date issueDate) { this.issueDate = issueDate; }

    public java.util.Date getReturnDate() { return returnDate; }
    public void setReturnDate(java.util.Date returnDate) { this.returnDate = returnDate; }

    public int getReturnStatus() { return returnStatus; }
    public void setReturnStatus(int returnStatus) { this.returnStatus = returnStatus; }

    public int getFine() { return fine; }
    public void setFine(int fine) { this.fine = fine; }

    public java.util.Date getDueDate() { return dueDate; }
    public void setDueDate(java.util.Date dueDate) { this.dueDate = dueDate; }

    public int getOverdueDays() { return overdueDays; }
    public void setOverdueDays(int overdueDays) { this.overdueDays = overdueDays; }

    public int getSuggestedFine() { return suggestedFine; }
    public void setSuggestedFine(int suggestedFine) { this.suggestedFine = suggestedFine; }

    /** retrunstatus = 1 is the truth (issue_book.returndate has ON UPDATE CURRENT_TIMESTAMP, so it can change by accident). */
    public boolean isReturned() { return returnStatus == 1; }

}
