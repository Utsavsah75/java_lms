package com.library.controller;

import com.library.dao.StatsDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/** dashboard.php  -> admin statistics cards. */
@WebServlet(name = "DashboardServlet", urlPatterns = "/admin/dashboard")
public class DashboardServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;
    private final StatsDAO statsDAO = new StatsDAO();

    @Override
    protected void get(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        req.setAttribute("stats", statsDAO.adminStats());
        view(req, resp, "admin/dashboard");
    }
}
