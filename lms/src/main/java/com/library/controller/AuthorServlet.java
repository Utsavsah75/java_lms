package com.library.controller;

import com.library.dao.AuthorDAO;
import com.library.model.Author;
import com.library.service.CatalogService;
import com.library.util.BusinessException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/** add_author.php, manage_author.php, edit_author.php  (same URL layout as CategoryServlet). */
@WebServlet(name = "AuthorServlet", urlPatterns = {"/admin/authors", "/admin/authors/add", "/admin/authors/edit", "/admin/authors/delete"})
public class AuthorServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;
    private final AuthorDAO dao = new AuthorDAO();
    private final CatalogService service = new CatalogService();

    @Override
    protected void get(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        switch (req.getServletPath()) {
            case "/admin/authors/add":
                view(req, resp, "admin/author-form");
                break;
            case "/admin/authors/edit":
                Author a = dao.findById(intParam(req, "id"));
                if (a == null) {
                    error(req, "Author not found.");
                    redirect(req, resp, "/admin/authors");
                    return;
                }
                req.setAttribute("author", a);
                view(req, resp, "admin/author-form");
                break;
            case "/admin/authors":
                req.setAttribute("authors", dao.findAll());
                view(req, resp, "admin/authors");
                break;
            default:
                resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        }
    }

    @Override
    protected void post(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        switch (req.getServletPath()) {
            case "/admin/authors/add":
                try {
                    service.addAuthor(param(req, "author"));
                    success(req, "Author added successfully.");
                    redirect(req, resp, "/admin/authors/add");
                } catch (BusinessException e) {
                    req.setAttribute("flashError", e.getMessage());
                    view(req, resp, "admin/author-form");
                }
                break;
            case "/admin/authors/edit":
                int id = intParam(req, "id");
                try {
                    service.updateAuthor(id, param(req, "author"));
                    success(req, "Author updated successfully at Number " + id + ".");
                    redirect(req, resp, "/admin/authors");
                } catch (BusinessException e) {
                    req.setAttribute("author", dao.findById(id));
                    req.setAttribute("flashError", e.getMessage());
                    view(req, resp, "admin/author-form");
                }
                break;
            case "/admin/authors/delete":
                int did = intParam(req, "id");
                try {
                    service.deleteAuthor(did);
                    success(req, "Author deleted successfully at Number " + did + ".");
                } catch (BusinessException e) {
                    error(req, e.getMessage());
                }
                redirect(req, resp, "/admin/authors");
                break;
            default:
                resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        }
    }
}
