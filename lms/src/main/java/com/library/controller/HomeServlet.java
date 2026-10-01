package com.library.controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/** index.php  -> home page with the STUDENT login card. */
@WebServlet(name = "HomeServlet", urlPatterns = "/home")
public class HomeServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void get(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        if (req.getSession().getAttribute(SESSION_STUDENT_CODE) != null) {
            redirect(req, resp, "/student/dashboard");
            return;
        }
        view(req, resp, "public/home");
    }
}
