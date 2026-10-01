package com.library.controller;

import com.library.dao.StudentDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/** verify_email.php  -> the link inside the verification e-mail. */
@WebServlet(name = "VerifyEmailServlet", urlPatterns = "/verify-email")
public class VerifyEmailServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;
    private final StudentDAO studentDAO = new StudentDAO();

    @Override
    protected void get(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String code = param(req, "code");
        if (code.isEmpty() || code.length() > 200) {
            error(req, "This verification link is not valid.");
        } else {
            switch (studentDAO.verifyByCode(code)) {
                case 1:
                    success(req, "Your email is verified successfully. You can log in now.");
                    break;
                case 2:
                    success(req, "Your email is already verified. Please log in.");
                    break;
                default:
                    error(req, "This verification code is invalid.");
            }
        }
        redirect(req, resp, "/home");
    }
}
