package com.library.model;

/** Row of table bookcategory. */
public class Category implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String name;
    private int status;
    private java.util.Date creationDate;
    private java.util.Date updationDate;
    private int bookCount;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    public java.util.Date getCreationDate() { return creationDate; }
    public void setCreationDate(java.util.Date creationDate) { this.creationDate = creationDate; }

    public java.util.Date getUpdationDate() { return updationDate; }
    public void setUpdationDate(java.util.Date updationDate) { this.updationDate = updationDate; }

    public int getBookCount() { return bookCount; }
    public void setBookCount(int bookCount) { this.bookCount = bookCount; }

    public boolean isActive() { return status == 1; }

}
