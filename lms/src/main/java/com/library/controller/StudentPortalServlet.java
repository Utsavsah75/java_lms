package com.library.controller;

import com.library.dao.BookDAO;
import com.library.dao.IssueDAO;
import com.library.dao.StatsDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/** user_dashboard.php, book_list.php, borrow_book.php  -> read-only student portal. */
@WebServlet(name = "StudentPortalServlet", urlPatterns = {"/student/dashboard", "/student/books", "/student/issued"})
public class StudentPortalServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;
    private final StatsDAO statsDAO = new StatsDAO();
    private final BookDAO bookDAO = new BookDAO();
    private final IssueDAO issueDAO = new IssueDAO();

    @Override
    protected void get(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String code = studentCode(req);
        switch (req.getServletPath()) {
            case "/student/dashboard":
                req.setAttribute("stats", statsDAO.studentStats(code));
                view(req, resp, "student/dashboard");
                break;
            case "/student/books":
                req.setAttribute("books", bookDAO.search(null));
                view(req, resp, "student/books");
                break;
            case "/student/issued":
                req.setAttribute("issues", issueDAO.findByStudent(code));
                view(req, resp, "student/issued");
                break;
            default:
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
