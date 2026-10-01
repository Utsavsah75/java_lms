package com.library.model;

/** Row of table books joined with its category and author. */
public class Book implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String name;
    private int categoryId;
    private String categoryName;
    private int authorId;
    private String authorName;
    private String isbn;
    private int quantity;
    private java.math.BigDecimal price;
    private String image;
    private java.util.Date regDate;
    private java.util.Date updationDate;
    private int issuedCount;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public int getAuthorId() { return authorId; }
    public void setAuthorId(int authorId) { this.authorId = authorId; }

    public String getAuthorName() { return authorName; }
    public void setAuthorName(String authorName) { this.authorName = authorName; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public java.math.BigDecimal getPrice() { return price; }
    public void setPrice(java.math.BigDecimal price) { this.price = price; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public java.util.Date getRegDate() { return regDate; }
    public void setRegDate(java.util.Date regDate) { this.regDate = regDate; }

    public java.util.Date getUpdationDate() { return updationDate; }
    public void setUpdationDate(java.util.Date updationDate) { this.updationDate = updationDate; }

    public int getIssuedCount() { return issuedCount; }
    public void setIssuedCount(int issuedCount) { this.issuedCount = issuedCount; }

    public int getCopiesAvailable() { return Math.max(0, quantity - issuedCount); }
    public boolean isInStock() { return getCopiesAvailable() > 0; }

}
