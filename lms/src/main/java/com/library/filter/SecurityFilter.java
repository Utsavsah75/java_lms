package com.library.filter;

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
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * 1. UTF-8 for every request
 * 2. basic security headers
 * 3. CSRF protection: every POST must carry the session's token (field _csrf). JSP forms print ${csrfToken}.
 */
@WebFilter(filterName = "SecurityFilter", urlPatterns = "/*")
public class SecurityFilter implements Filter {

    private static final SecureRandom RANDOM = new SecureRandom();
    public static final String TOKEN = "csrfToken";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        req.setCharacterEncoding("UTF-8");
        resp.setHeader("X-Content-Type-Options", "nosniff");
        resp.setHeader("X-Frame-Options", "SAMEORIGIN");
        resp.setHeader("Referrer-Policy", "same-origin");

        HttpSession session = req.getSession(true);
        String token = (String) session.getAttribute(TOKEN);
        if (token == null) {
            byte[] b = new byte[24];
            RANDOM.nextBytes(b);
            token = Base64.getUrlEncoder().withoutPadding().encodeToString(b);
            session.setAttribute(TOKEN, token);
        }
        req.setAttribute(TOKEN, token);

        if ("POST".equalsIgnoreCase(req.getMethod())) {
            String sent = req.getParameter("_csrf");
            if (sent == null || !MessageDigest.isEqual(sent.getBytes(), token.getBytes())) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid or missing security token. Reload the page and try again.");
                return;
            }
        }
        chain.doFilter(request, response);
    }
}
