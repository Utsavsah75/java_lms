package com.library.controller;

import com.library.dao.BookDAO;
import com.library.dao.StudentDAO;
import com.library.model.Book;
import com.library.model.Student;
import com.library.util.JsonUtil;
import com.library.util.Validator;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * check_avability.php, check_student.php, check_book.php  -> small JSON answers for the forms.
 *   type=email|phone            (public: registration form)
 *   type=isbn|student|book      (administrators only)
 */
@WebServlet(name = "AjaxCheckServlet", urlPatterns = "/ajax/check")
public class AjaxCheckServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;
    private final StudentDAO studentDAO = new StudentDAO();
    private final BookDAO bookDAO = new BookDAO();

    @Override
    protected void get(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String type = param(req, "type");
        String value = param(req, "value");
        boolean admin = req.getSession().getAttribute(SESSION_ADMIN_ID) != null;
        boolean ok;
        String message;

        switch (type) {
            case "email":
                if (!Validator.isEmail(value)) {
                    ok = false;
                    message = "Enter a valid e-mail address.";
                } else if (studentDAO.emailExists(value.toLowerCase(), 0)) {
                    ok = false;
                    message = "This e-mail is already registered.";
                } else {
                    ok = true;
                    message = "E-mail is available.";
                }
                break;
            case "phone":
                if (!Validator.isPhone(value)) {
                    ok = false;
                    message = "Enter digits only (7 to 15 digits).";
                } else if (studentDAO.phoneExists(value, 0)) {
                    ok = false;
                    message = "This mobile number is already registered.";
                } else {
                    ok = true;
                    message = "Mobile number is available.";
                }
                break;
            case "isbn":
                if (!admin) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN);
                    return;
                }
                ok = !bookDAO.isbnExists(value);
                message = ok ? "ISBN / Book ID is available." : "A book with this ISBN / Book ID already exists.";
                break;
            case "student":
                if (!admin) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN);
                    return;
                }
                Student s = studentDAO.findByCode(value);
                ok = s != null && s.isActive();
                message = s == null ? "Student not found."
                        : s.getName() + " | " + s.getEmail() + " | " + s.getPhone() + (s.isActive() ? "" : "  (BLOCKED / NOT VERIFIED)");
                break;
            case "book":
                if (!admin) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN);
                    return;
                }
                Book b = bookDAO.findByIsbn(value);
                ok = b != null && b.isInStock();
                message = b == null ? "Book not found."
                        : b.getName() + " | " + b.getAuthorName() + " | " + b.getCopiesAvailable() + " of " + b.getQuantity()
                                + " copies available" + (b.isInStock() ? "" : " (currently unavailable)");
                break;
            default:
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
        }
        Map<String, Object> json = new LinkedHashMap<>();
        json.put("ok", ok);
        json.put("message", message);
        resp.setContentType("application/json;charset=UTF-8");
        resp.setHeader("Cache-Control", "no-store");
        resp.getWriter().write(JsonUtil.toJson(json));
    }
}
