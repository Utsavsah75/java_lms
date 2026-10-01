package com.library.service;

import com.library.dao.AuthorDAO;
import com.library.dao.BookDAO;
import com.library.dao.CategoryDAO;
import com.library.model.Book;
import com.library.util.BusinessException;
import com.library.util.FileStorage;
import com.library.util.Validator;
import jakarta.servlet.http.Part;
import java.io.IOException;
import java.math.BigDecimal;

/** Validation and rules for books (PHP: add_book.php, edit_book.php, change_image.php, manage_book.php delete). */
public class BookService {

    private final BookDAO bookDAO = new BookDAO();
    private final CategoryDAO categoryDAO = new CategoryDAO();
    private final AuthorDAO authorDAO = new AuthorDAO();

    private Book build(String name, String categoryId, String authorId, String price, String quantity) throws BusinessException {
        name = Validator.trim(name).replaceAll("\\s+", " ");
        if (name.isEmpty() || !Validator.maxLen(name, 255)) {
            throw new BusinessException("Book name is required (maximum 255 characters).");
        }
        if (!Validator.isPositiveInt(categoryId) || categoryDAO.findById(Validator.toInt(categoryId, 0)) == null) {
            throw new BusinessException("Please select a valid category.");
        }
        if (!Validator.isPositiveInt(authorId) || authorDAO.findById(Validator.toInt(authorId, 0)) == null) {
            throw new BusinessException("Please select a valid author.");
        }
        if (!Validator.isMoney(Validator.trim(price))) {
            throw new BusinessException("Price must be a number, for example 250 or 250.50.");
        }
        if (!Validator.isPositiveInt(quantity)) {
            throw new BusinessException("Quantity must be a whole number greater than 0.");
        }
        Book b = new Book();
        b.setName(name);
        b.setCategoryId(Validator.toInt(categoryId, 0));
        b.setAuthorId(Validator.toInt(authorId, 0));
        b.setPrice(new BigDecimal(price.trim()));
        b.setQuantity(Validator.toInt(quantity, 0));
        return b;
    }

    public void addBook(String name, String categoryId, String authorId, String isbn, String price, String quantity, Part picture)
            throws BusinessException, IOException {
        Book b = build(name, categoryId, authorId, price, quantity);
        isbn = Validator.trim(isbn);
        if (!Validator.isIsbn(isbn)) {
            throw new BusinessException("ISBN / Book ID is required (letters, numbers, - . _ only, max 25 characters).");
        }
        if (bookDAO.isbnExists(isbn)) {
            throw new BusinessException("A book with this ISBN / Book ID already exists.");
        }
        if (picture == null || picture.getSize() == 0) {
            throw new BusinessException("Please choose a book cover picture.");
        }
        String file = FileStorage.saveImage(picture);
        b.setIsbn(isbn);
        b.setImage(file);
        try {
            bookDAO.insert(b);
        } catch (RuntimeException e) {
            FileStorage.deleteQuietly(file);
            throw e;
        }
    }

    public void updateBook(int id, String name, String categoryId, String authorId, String price, String quantity)
            throws BusinessException {
        Book existing = bookDAO.findById(id);
        if (existing == null) {
            throw new BusinessException("Book not found.");
        }
        Book b = build(name, categoryId, authorId, price, quantity);
        if (b.getQuantity() < existing.getIssuedCount()) {
            throw new BusinessException("Quantity cannot be lower than the " + existing.getIssuedCount()
                    + " copies that are currently issued.");
        }
        b.setId(id);
        bookDAO.update(b);
    }

    public void changeImage(int id, Part picture) throws BusinessException, IOException {
        Book existing = bookDAO.findById(id);
        if (existing == null) {
            throw new BusinessException("Book not found.");
        }
        if (picture == null || picture.getSize() == 0) {
            throw new BusinessException("Please select a file.");
        }
        String file = FileStorage.saveImage(picture);
        bookDAO.updateImage(id, file);
        // keep bundled sample pictures shared between books; only remove files this app created
        if (existing.getImage() != null && existing.getImage().matches("[0-9a-f]{32}\\.(jpg|jpeg|png|gif)")) {
            FileStorage.deleteQuietly(existing.getImage());
        }
    }

    public void deleteBook(int id) throws BusinessException {
        Book b = bookDAO.findById(id);
        if (b == null) {
            throw new BusinessException("Book not found.");
        }
        if (b.getIssuedCount() > 0) {
            throw new BusinessException("Unable to delete the book: " + b.getIssuedCount()
                    + " copy(ies) are currently issued to students.");
        }
        bookDAO.delete(id);
    }
}
