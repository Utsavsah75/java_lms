package com.library.controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/** About.html -> team page. */
@WebServlet(name = "AboutServlet", urlPatterns = "/about")
public class AboutServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void get(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        view(req, resp, "public/about");
    }
}
