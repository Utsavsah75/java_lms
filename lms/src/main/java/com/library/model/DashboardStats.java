package com.library.model;

/** Numbers shown on the admin dashboard cards. */
public class DashboardStats implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private int books;
    private int students;
    private int issues;
    private int notReturned;
    private int authors;
    private int categories;
    private long totalFine;

    public int getBooks() { return books; }
    public void setBooks(int books) { this.books = books; }

    public int getStudents() { return students; }
    public void setStudents(int students) { this.students = students; }

    public int getIssues() { return issues; }
    public void setIssues(int issues) { this.issues = issues; }

    public int getNotReturned() { return notReturned; }
    public void setNotReturned(int notReturned) { this.notReturned = notReturned; }

    public int getAuthors() { return authors; }
    public void setAuthors(int authors) { this.authors = authors; }

    public int getCategories() { return categories; }
    public void setCategories(int categories) { this.categories = categories; }

    public long getTotalFine() { return totalFine; }
    public void setTotalFine(long totalFine) { this.totalFine = totalFine; }

}
