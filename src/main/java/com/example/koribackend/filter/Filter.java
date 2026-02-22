package com.example.koribackend.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter(urlPatterns = {"/enter"})
public class Filter implements jakarta.servlet.Filter {
    public void init(FilterConfig config) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        String path = ((HttpServletRequest) request).getServletPath();
        if (path.equals("/enter")) {
            enter(request, response, chain);
        }
    }

    private void enter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        if (session != null) {
            if (session.getAttribute("admin") != null) {
                res.sendRedirect("/admin/home");
            } else if (session.getAttribute("student") != null) {
                res.sendRedirect("/student/home");
            } else if (session.getAttribute("teacher") != null) {
                // Also add the professor's homepage path.
                // After adding the path, delete the line below.
                chain.doFilter(request, response);
            } else {
                session.invalidate();
                chain.doFilter(request,response);
            }
        } else {
            chain.doFilter(request, response);
        }
    }
}
