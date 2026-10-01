package com.library.controller;

import com.library.dao.AdminDAO;
import com.library.model.Admin;
import com.library.service.AdminService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.Optional;

/** admin_login.php + admin_page.php  -> administrator login. */
@WebServlet(name = "AdminLoginServlet", urlPatterns = "/admin-login")
public class AdminLoginServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;
    private final AdminDAO adminDAO = new AdminDAO();
    private final AdminService adminService = new AdminService();

    @Override
    protected void get(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        if (req.getSession().getAttribute(SESSION_ADMIN_ID) != null) {
            redirect(req, resp, "/admin/dashboard");
            return;
        }
        String remembered = "";
        if (req.getCookies() != null) {
            for (Cookie c : req.getCookies()) {
                if ("rem_email".equals(c.getName())) {
                    remembered = c.getValue();
                }
            }
        }
        req.setAttribute("rememberedEmail", remembered);
        req.setAttribute("registrationOpen", adminService.registrationOpen());
        view(req, resp, "public/admin-login");
    }

    @Override
    protected void post(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String email = param(req, "email").toLowerCase();
        String password = req.getParameter("pwd") == null ? "" : req.getParameter("pwd");
        Optional<Admin> admin = email.isEmpty() || password.isEmpty() ? Optional.empty() : adminDAO.authenticate(email, password);
        if (admin.isEmpty()) {
            error(req, "Invalid Email / Password");
            redirect(req, resp, "/admin-login");
            return;
        }
        HttpSession session = newSession(req);
        session.setAttribute(SESSION_ADMIN_ID, admin.get().getId());
        session.setAttribute(SESSION_ADMIN_NAME, admin.get().getName());
        session.setMaxInactiveInterval(30 * 60);

        // "Remember me" keeps only the e-mail address, never the password
        Cookie c = new Cookie("rem_email", "on".equals(req.getParameter("rem")) ? email : "");
        c.setHttpOnly(true);
        c.setPath(req.getContextPath().isEmpty() ? "/" : req.getContextPath());
        c.setMaxAge("on".equals(req.getParameter("rem")) ? 60 * 60 * 24 * 30 : 0);
        resp.addCookie(c);
        redirect(req, resp, "/admin/dashboard");
    }
}
