package com.library.controller;

import com.library.util.BusinessException;
import com.library.util.DataAccessException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Parent of every servlet.
 *  - subclasses implement get() / post() instead of doGet() / doPost()
 *  - exceptions are handled in ONE place: technical errors are logged and the user only sees a friendly message
 *  - flash messages (success / error) survive one redirect, like $_SESSION['msg'] in the PHP version
 */
public abstract class BaseServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    protected final Logger log = Logger.getLogger(getClass().getName());

    public static final String SESSION_ADMIN_ID = "adminId";
    public static final String SESSION_ADMIN_NAME = "adminName";
    public static final String SESSION_STUDENT_ID = "studentId";
    public static final String SESSION_STUDENT_CODE = "studentCode";
    public static final String SESSION_STUDENT_NAME = "studentName";

    protected void get(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }

    protected void post(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }

    @Override
    protected final void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        run(req, resp, false);
    }

    @Override
    protected final void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        run(req, resp, true);
    }

    private void run(HttpServletRequest req, HttpServletResponse resp, boolean isPost) throws ServletException, IOException {
        try {
            if (isPost) {
                post(req, resp);
            } else {
                get(req, resp);
            }
        } catch (BusinessException e) {
            // a rule was broken: the message is written for the user
            error(req, e.getMessage());
            safeRedirect(req, resp);
        } catch (DataAccessException e) {
            log.log(Level.SEVERE, "Database problem on " + req.getRequestURI(), e);
            showError(req, resp, 500, "We could not complete your request. Please try again later.");
        } catch (ServletException | IOException e) {
            throw e;
        } catch (Exception e) {
            log.log(Level.SEVERE, "Unexpected problem on " + req.getRequestURI(), e);
            showError(req, resp, 500, "Something went wrong. Please try again.");
        }
    }

    /** After a BusinessException on a POST we go back to the page the form came from. */
    private void safeRedirect(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String target = req.getRequestURI().substring(req.getContextPath().length());
        String query = req.getQueryString();
        if ("GET".equals(req.getMethod()) && query == null) {
            // a plain GET that fails must not redirect to itself forever
            showError(req, resp, 400, String.valueOf(req.getSession().getAttribute("flash_error")));
            req.getSession().removeAttribute("flash_error");
            return;
        }
        resp.sendRedirect(req.getContextPath() + target + (query == null ? "" : "?" + query));
    }

    // ---- flash messages ------------------------------------------------------------------------

    protected static void success(HttpServletRequest req, String msg) {
        req.getSession().setAttribute("flash_success", msg);
    }

    protected static void error(HttpServletRequest req, String msg) {
        req.getSession().setAttribute("flash_error", msg);
    }

    // ---- navigation ----------------------------------------------------------------------------

    /** Forwards to /WEB-INF/views/{view}.jsp and moves pending flash messages into the request. */
    protected void view(HttpServletRequest req, HttpServletResponse resp, String view) throws ServletException, IOException {
        HttpSession s = req.getSession(false);
        if (s != null) {
            move(s, req, "flash_success", "flashSuccess");
            move(s, req, "flash_error", "flashError");
        }
        req.getRequestDispatcher("/WEB-INF/views/" + view + ".jsp").forward(req, resp);
    }

    private static void move(HttpSession s, HttpServletRequest req, String from, String to) {
        Object v = s.getAttribute(from);
        if (v != null) {
            req.setAttribute(to, v);
            s.removeAttribute(from);
        }
    }

    protected void redirect(HttpServletRequest req, HttpServletResponse resp, String path) throws IOException {
        resp.sendRedirect(req.getContextPath() + path);
    }

    protected void showError(HttpServletRequest req, HttpServletResponse resp, int status, String message)
            throws ServletException, IOException {
        if (resp.isCommitted()) {
            return;
        }
        resp.setStatus(status);
        req.setAttribute("errorStatus", status);
        req.setAttribute("errorMessage", message);
        view(req, resp, "common/error");
    }

    // ---- request helpers -----------------------------------------------------------------------

    protected static String param(HttpServletRequest req, String name) {
        String v = req.getParameter(name);
        return v == null ? "" : v.trim();
    }

    protected static int intParam(HttpServletRequest req, String name) {
        try {
            return Integer.parseInt(param(req, name));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    protected static int adminId(HttpServletRequest req) {
        HttpSession s = req.getSession(false);
        Object v = s == null ? null : s.getAttribute(SESSION_ADMIN_ID);
        return v instanceof Integer ? (Integer) v : 0;
    }

    protected static String studentCode(HttpServletRequest req) {
        HttpSession s = req.getSession(false);
        Object v = s == null ? null : s.getAttribute(SESSION_STUDENT_CODE);
        return v == null ? "" : v.toString();
    }

    /** Starts a fresh session after login (prevents session fixation). */
    protected static HttpSession newSession(HttpServletRequest req) {
        HttpSession old = req.getSession(false);
        if (old != null) {
            old.invalidate();
        }
        return req.getSession(true);
    }
}
