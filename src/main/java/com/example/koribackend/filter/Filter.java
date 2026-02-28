package com.example.koribackend.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

// Apply this filter specifically to the login entry point
@WebFilter(urlPatterns = {"/enter"})
public class Filter implements jakarta.servlet.Filter {
    public void init(FilterConfig config) throws ServletException {
    }

    // Intercept the request and determine if the user is already authenticated
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        String path = ((HttpServletRequest) request).getServletPath();
        if (path.equals("/enter")) {
            enter(request, response, chain);
        }
    }

    /**
     * Prevent authenticated users from accessing the login page.
     * If a valid session exists, redirect the user to their respective home dashboard.
     */
    private void enter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        if (session != null) {
            // Check for Administrator session attribute
            if (session.getAttribute("admin") != null) {
                res.sendRedirect("homeAdmin");
            }
            // Check for Student session attribute
            else if (session.getAttribute("student") != null) {
                res.sendRedirect("homeStudent");
            }
            // Check for Professor session attribute
            else if (session.getAttribute("professor") != null) {
                res.sendRedirect("homeProfessor");
            }
            // If the session is empty or invalid, clear it and proceed to the login page
            else {
                session.invalidate();
                chain.doFilter(request,response);
            }
        } else {
            // No session found, proceed to the login page normally
            chain.doFilter(request, response);
        }
    }
}