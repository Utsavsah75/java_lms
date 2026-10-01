package com.library.controller;

import com.library.dao.BookDAO;
import com.library.dao.IssueDAO;
import com.library.dao.StudentDAO;
import com.library.model.IssueRecord;
import com.library.service.IssueService;
import com.library.util.BusinessException;
import com.library.util.Validator;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * issue_book.php, manage_issue_book.php, edit_issue_book.php (return)
 *   GET+POST /admin/issue           issue a book
 *   GET      /admin/issues          borrowing records (optional ?from=&to= dates)
 *   GET+POST /admin/issues/return   return a book, store the fine
 */
@WebServlet(name = "IssueServlet", urlPatterns = {"/admin/issue", "/admin/issues", "/admin/issues/return"})
public class IssueServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;
    private final IssueDAO issueDAO = new IssueDAO();
    private final StudentDAO studentDAO = new StudentDAO();
    private final BookDAO bookDAO = new BookDAO();
    private final IssueService service = new IssueService();

    private void issueLists(HttpServletRequest req) {
        req.setAttribute("students", studentDAO.findActive());
        req.setAttribute("books", bookDAO.search(null));
    }

    @Override
    protected void get(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        switch (req.getServletPath()) {
            case "/admin/issue":
                issueLists(req);
                view(req, resp, "admin/issue-form");
                break;
            case "/admin/issues":
                String from = param(req, "from");
                String to = param(req, "to");
                if (!from.isEmpty() && !Validator.isDate(from)) {
                    from = "";
                }
                if (!to.isEmpty() && !Validator.isDate(to)) {
                    to = "";
                }
                req.setAttribute("from", from);
                req.setAttribute("to", to);
                req.setAttribute("issues", issueDAO.findAll(from, to));
                view(req, resp, "admin/issues");
                break;
            case "/admin/issues/return":
                IssueRecord r = issueDAO.findById(intParam(req, "id"));
                if (r == null) {
                    error(req, "Issue record not found.");
                    redirect(req, resp, "/admin/issues");
                    return;
                }
                req.setAttribute("issue", r);
                view(req, resp, "admin/issue-return");
                break;
            default:
                resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        }
    }

    @Override
    protected void post(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        if ("/admin/issue".equals(req.getServletPath())) {
            try {
                service.issueBook(param(req, "studentCode"), param(req, "isbn"));
                success(req, "Book issued successfully.");
                redirect(req, resp, "/admin/issues");
            } catch (BusinessException e) {
                issueLists(req);
                req.setAttribute("flashError", e.getMessage());
                view(req, resp, "admin/issue-form");
            }
        } else if ("/admin/issues/return".equals(req.getServletPath())) {
            int id = intParam(req, "id");
            try {
                service.returnBook(id, param(req, "fine"));
                success(req, "Book returned successfully with fine Rs. " + (param(req, "fine").isEmpty() ? "0" : param(req, "fine")) + ".");
                redirect(req, resp, "/admin/issues");
            } catch (BusinessException e) {
                req.setAttribute("issue", issueDAO.findById(id));
                req.setAttribute("flashError", e.getMessage());
                view(req, resp, "admin/issue-return");
            }
        } else {
            resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        }
    }
}
