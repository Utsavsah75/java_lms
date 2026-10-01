package com.library.controller;

import com.library.dao.AuthorDAO;
import com.library.dao.BookDAO;
import com.library.dao.CategoryDAO;
import com.library.model.Book;
import com.library.service.BookService;
import com.library.util.BusinessException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * add_book.php, manage_book.php, edit_book.php, change_image.php, delete_book.php
 *   GET /admin/books (?q= search)   GET+POST /admin/books/add   GET+POST /admin/books/edit?id=
 *   GET+POST /admin/books/image?id=   POST /admin/books/delete
 */
@WebServlet(name = "BookServlet", urlPatterns = {"/admin/books", "/admin/books/add", "/admin/books/edit", "/admin/books/image",
        "/admin/books/delete"})
@MultipartConfig(maxFileSize = 2 * 1024 * 1024, maxRequestSize = 3 * 1024 * 1024)
public class BookServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;
    private final BookDAO bookDAO = new BookDAO();
    private final CategoryDAO categoryDAO = new CategoryDAO();
    private final AuthorDAO authorDAO = new AuthorDAO();
    private final BookService service = new BookService();

    private void lists(HttpServletRequest req) {
        req.setAttribute("categories", categoryDAO.findAll());
        req.setAttribute("authors", authorDAO.findAll());
    }

    @Override
    protected void get(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        switch (req.getServletPath()) {
            case "/admin/books":
                String q = param(req, "q");
                req.setAttribute("q", q);
                req.setAttribute("books", bookDAO.search(q));
                view(req, resp, "admin/books");
                break;
            case "/admin/books/add":
                lists(req);
                view(req, resp, "admin/book-add");
                break;
            case "/admin/books/edit":
            case "/admin/books/image":
                Book b = bookDAO.findById(intParam(req, "id"));
                if (b == null) {
                    error(req, "Book not found.");
                    redirect(req, resp, "/admin/books");
                    return;
                }
                req.setAttribute("book", b);
                if (req.getServletPath().endsWith("edit")) {
                    lists(req);
                    view(req, resp, "admin/book-edit");
                } else {
                    view(req, resp, "admin/book-image");
                }
                break;
            default:
                resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        }
    }

    @Override
    protected void post(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        int id = intParam(req, "id");
        switch (req.getServletPath()) {
            case "/admin/books/add":
                try {
                    service.addBook(param(req, "bookname"), param(req, "category"), param(req, "author"), param(req, "isbnno"),
                            param(req, "price"), param(req, "quantity"), req.getPart("bookpic"));
                    success(req, "Book added successfully.");
                    redirect(req, resp, "/admin/books/add");
                } catch (BusinessException e) {
                    lists(req);
                    req.setAttribute("flashError", e.getMessage());
                    view(req, resp, "admin/book-add");
                }
                break;
            case "/admin/books/edit":
                try {
                    service.updateBook(id, param(req, "bookname"), param(req, "catid"), param(req, "authorid"),
                            param(req, "bookprice"), param(req, "quantity"));
                    success(req, "Book updated successfully.");
                    redirect(req, resp, "/admin/books");
                } catch (BusinessException e) {
                    lists(req);
                    req.setAttribute("book", bookDAO.findById(id));
                    req.setAttribute("flashError", e.getMessage());
                    view(req, resp, "admin/book-edit");
                }
                break;
            case "/admin/books/image":
                try {
                    service.changeImage(id, req.getPart("bookpic"));
                    success(req, "Book image has been updated successfully.");
                    redirect(req, resp, "/admin/books");
                } catch (BusinessException e) {
                    req.setAttribute("book", bookDAO.findById(id));
                    req.setAttribute("flashError", e.getMessage());
                    view(req, resp, "admin/book-image");
                }
                break;
            case "/admin/books/delete":
                try {
                    service.deleteBook(id);
                    success(req, "Book deleted successfully at Number " + id + ".");
                } catch (BusinessException e) {
                    error(req, e.getMessage());
                }
                redirect(req, resp, "/admin/books");
                break;
            default:
                resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        }
    }
}
