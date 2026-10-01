package com.library.controller;

import com.library.service.AdminService;
import com.library.util.BusinessException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/** change_pwd.php  -> the logged-in administrator changes the password, then must log in again. */
@WebServlet(name = "AdminPasswordServlet", urlPatterns = "/admin/change-password")
public class AdminPasswordServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;
    private final AdminService service = new AdminService();

    @Override
    protected void get(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        view(req, resp, "admin/change-password");
    }

    @Override
    protected void post(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        try {
            service.changePassword(adminId(req), req.getParameter("password"), req.getParameter("newpassword"),
                    req.getParameter("confirmpassword"));
            req.getSession().invalidate();
            req.getSession(true).setAttribute("flash_success", "Password updated successfully. Please log in with the new password.");
            redirect(req, resp, "/admin-login");
        } catch (BusinessException e) {
            req.setAttribute("flashError", e.getMessage());
            view(req, resp, "admin/change-password");
        }
    }
}
