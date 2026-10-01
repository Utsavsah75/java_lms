package com.library.controller;

import com.library.service.StudentService;
import com.library.service.StudentService.RegistrationResult;
import com.library.util.BusinessException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/** user_form.php + registation.php  -> student self registration with e-mail verification. */
@WebServlet(name = "RegisterServlet", urlPatterns = "/register")
@MultipartConfig(maxFileSize = 2 * 1024 * 1024, maxRequestSize = 3 * 1024 * 1024)
public class RegisterServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;
    private final StudentService studentService = new StudentService();

    @Override
    protected void get(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        view(req, resp, "public/register");
    }

    @Override
    protected void post(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        try {
            RegistrationResult r = studentService.register(param(req, "fullname"), param(req, "address"), param(req, "phone"),
                    param(req, "gender"), param(req, "email"), req.getParameter("password"), req.getParameter("confirmpassword"),
                    req.getPart("photo"));
            if (r == RegistrationResult.MAIL_SENT) {
                success(req, "Account created. Check your e-mail and click the verification link to activate it.");
            } else {
                success(req, "Account created. The verification e-mail could not be sent, so the library will activate your account. You can log in after that.");
            }
            redirect(req, resp, "/home");
        } catch (BusinessException e) {
            req.setAttribute("flashError", e.getMessage());
            view(req, resp, "public/register");
        }
    }
}
