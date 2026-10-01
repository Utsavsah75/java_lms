package com.library.controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/** logout.php  -> ends the session for admins and students. */
@WebServlet(name = "LogoutServlet", urlPatterns = "/logout")
public class LogoutServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void get(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        boolean wasAdmin = req.getSession().getAttribute(SESSION_ADMIN_ID) != null;
        HttpSession s = req.getSession(false);
        if (s != null) {
            s.invalidate();
        }
        req.getSession(true).setAttribute("flash_success", "You have been logged out.");
        redirect(req, resp, wasAdmin ? "/admin-login" : "/home");
    }
}
