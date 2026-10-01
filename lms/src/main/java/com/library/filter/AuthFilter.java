package com.library.filter;

import com.library.controller.BaseServlet;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Replaces check_admin.php: /admin/* needs an administrator session, /student/* needs a student session.
 * Protected pages are also marked "no-store" so the Back button cannot show them after logout.
 */
@WebFilter(filterName = "AuthFilter", urlPatterns = {"/admin/*", "/student/*"})
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String path = req.getRequestURI().substring(req.getContextPath().length());
        boolean adminArea = path.startsWith("/admin/");

        HttpSession session = req.getSession(false);
        boolean ok = session != null
                && (adminArea ? session.getAttribute(BaseServlet.SESSION_ADMIN_ID) != null
                              : session.getAttribute(BaseServlet.SESSION_STUDENT_CODE) != null);

        if (!ok) {
            if (session != null) {
                session.setAttribute("flash_error", "Login first to view page.");
            } else {
                req.getSession(true).setAttribute("flash_error", "Login first to view page.");
            }
            resp.sendRedirect(req.getContextPath() + (adminArea ? "/admin-login" : "/"));
            return;
        }
        resp.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        resp.setHeader("Pragma", "no-cache");
        resp.setDateHeader("Expires", 0);
        chain.doFilter(request, response);
    }
}
