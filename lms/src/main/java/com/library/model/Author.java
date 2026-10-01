package com.library.model;

/** Row of table bookauthor. */
public class Author implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String name;
    private java.util.Date creationDate;
    private java.util.Date updationDate;
    private int bookCount;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public java.util.Date getCreationDate() { return creationDate; }
    public void setCreationDate(java.util.Date creationDate) { this.creationDate = creationDate; }

    public java.util.Date getUpdationDate() { return updationDate; }
    public void setUpdationDate(java.util.Date updationDate) { this.updationDate = updationDate; }

    public int getBookCount() { return bookCount; }
    public void setBookCount(int bookCount) { this.bookCount = bookCount; }

}
