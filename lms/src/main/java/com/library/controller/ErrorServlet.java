package com.library.controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/** Target of the error-page entries in web.xml: shows a friendly page and never a stack trace. */
@WebServlet(name = "ErrorServlet", urlPatterns = "/error")
public class ErrorServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void get(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        int code = intParam(req, "code");
        String msg;
        switch (code) {
            case 403:
                msg = "You are not allowed to do that. If a form expired, reload the page and try again.";
                break;
            case 404:
                msg = "The page you are looking for does not exist.";
                break;
            default:
                code = 500;
                msg = "Something went wrong on our side. Please try again later.";
        }
        showError(req, resp, code, msg);
    }

    @Override
    protected void post(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        get(req, resp);
    }
}
