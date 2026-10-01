package com.library.controller;

import com.library.service.AdminService;
import com.library.util.BusinessException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/** admin_reg.php + Admin_loginserver.php  -> create an administrator account (closed once an admin exists, see app.properties). */
@WebServlet(name = "AdminRegisterServlet", urlPatterns = "/admin-register")
public class AdminRegisterServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;
    private final AdminService adminService = new AdminService();

    @Override
    protected void get(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        if (!adminService.registrationOpen()) {
            error(req, "Administrator registration is disabled. Ask an existing administrator.");
            redirect(req, resp, "/admin-login");
            return;
        }
        view(req, resp, "public/admin-register");
    }

    @Override
    protected void post(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        try {
            adminService.register(param(req, "name"), param(req, "email"), param(req, "gender"), param(req, "phone"),
                    param(req, "address"), req.getParameter("password"));
            success(req, "Registered successfully. You can log in now.");
            redirect(req, resp, "/admin-login");
        } catch (BusinessException e) {
            req.setAttribute("flashError", e.getMessage());
            view(req, resp, "public/admin-register");
        }
    }
}
