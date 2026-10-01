package com.library.controller;

import com.library.dao.CategoryDAO;
import com.library.model.Category;
import com.library.service.CatalogService;
import com.library.util.BusinessException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * add_category.php, manage_category.php, edit_category.php
 *   GET  /admin/categories          list             POST /admin/categories/delete   delete
 *   GET  /admin/categories/add      form             POST /admin/categories/add      create
 *   GET  /admin/categories/edit     form (?id=)      POST /admin/categories/edit     update
 *                                                    POST /admin/categories/status   activate / deactivate
 */
@WebServlet(name = "CategoryServlet", urlPatterns = {"/admin/categories", "/admin/categories/add", "/admin/categories/edit",
        "/admin/categories/delete", "/admin/categories/status"})
public class CategoryServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;
    private final CategoryDAO dao = new CategoryDAO();
    private final CatalogService service = new CatalogService();

    @Override
    protected void get(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        switch (req.getServletPath()) {
            case "/admin/categories/add":
                view(req, resp, "admin/category-form");
                break;
            case "/admin/categories/edit":
                Category c = dao.findById(intParam(req, "id"));
                if (c == null) {
                    error(req, "Category not found.");
                    redirect(req, resp, "/admin/categories");
                    return;
                }
                req.setAttribute("category", c);
                view(req, resp, "admin/category-form");
                break;
            case "/admin/categories":
                req.setAttribute("categories", dao.findAll());
                view(req, resp, "admin/categories");
                break;
            default:
                resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        }
    }

    @Override
    protected void post(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        switch (req.getServletPath()) {
            case "/admin/categories/add":
                try {
                    service.addCategory(param(req, "category"), param(req, "status"));
                    success(req, "Category added successfully.");
                    redirect(req, resp, "/admin/categories/add");
                } catch (BusinessException e) {
                    req.setAttribute("flashError", e.getMessage());
                    view(req, resp, "admin/category-form");
                }
                break;
            case "/admin/categories/edit":
                int id = intParam(req, "id");
                try {
                    service.updateCategory(id, param(req, "category"), param(req, "status"));
                    success(req, "Category with ID " + id + " updated successfully.");
                    redirect(req, resp, "/admin/categories");
                } catch (BusinessException e) {
                    req.setAttribute("category", dao.findById(id));
                    req.setAttribute("flashError", e.getMessage());
                    view(req, resp, "admin/category-form");
                }
                break;
            case "/admin/categories/status":
                int sid = intParam(req, "id");
                boolean activate = "activate".equals(param(req, "action"));
                if (dao.findById(sid) == null) {
                    throw new BusinessException("Category not found.");
                }
                dao.setStatus(sid, activate ? 1 : 0);
                success(req, "Category with ID " + sid + (activate ? " activated" : " deactivated") + " successfully.");
                redirect(req, resp, "/admin/categories");
                break;
            case "/admin/categories/delete":
                int did = intParam(req, "id");
                try {
                    service.deleteCategory(did);
                    success(req, "Category deleted successfully at Number " + did + ".");
                } catch (BusinessException e) {
                    error(req, e.getMessage());
                }
                redirect(req, resp, "/admin/categories");
                break;
            default:
                resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        }
    }
}
