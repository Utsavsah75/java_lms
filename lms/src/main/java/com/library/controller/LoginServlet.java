package com.library.controller;

import com.library.dao.StudentDAO;
import com.library.dao.StudentDAO.LoginOutcome;
import com.library.model.Student;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/** user_login.php  -> student login. */
@WebServlet(name = "LoginServlet", urlPatterns = "/login")
public class LoginServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;
    private final StudentDAO studentDAO = new StudentDAO();

    @Override
    protected void get(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        redirect(req, resp, "/home");
    }

    @Override
    protected void post(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String email = param(req, "email").toLowerCase();
        String password = req.getParameter("pwd") == null ? "" : req.getParameter("pwd");
        if (email.isEmpty() || password.isEmpty()) {
            error(req, "Enter your e-mail and password.");
            redirect(req, resp, "/home");
            return;
        }
        LoginOutcome out = studentDAO.authenticate(email, password);
        switch (out.result) {
            case OK:
                Student s = out.student;
                HttpSession session = newSession(req);
                session.setAttribute(SESSION_STUDENT_ID, s.getId());
                session.setAttribute(SESSION_STUDENT_CODE, s.getStudentCode());
                session.setAttribute(SESSION_STUDENT_NAME, s.getName());
                session.setMaxInactiveInterval(30 * 60);
                redirect(req, resp, "/student/dashboard");
                break;
            case NOT_ACTIVE:
                error(req, "Your account is not verified yet or has been blocked. Check your e-mail for the verification link or contact the library.");
                redirect(req, resp, "/home");
                break;
            default:
                error(req, "Invalid E-mail / Password");
                redirect(req, resp, "/home");
        }
    }
}
