package com.example.koribackend.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(urlPatterns = {
        "/admin/home",
        "/admin/informations",
        "/admin/logout"})
public class AdminController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        switch (path) {
            case "/admin/home":
                request.getRequestDispatcher("/WEB-INF/view/admin/homeAdmin.jsp").forward(request,response);
            case "/admin/informations":
                request.getRequestDispatcher("/WEB-INF/view/admin/information.jsp").forward(request,response);
            case "/admin/logout":
                logout(request,response);
        }
    }

    private void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        response.sendRedirect(request.getContextPath() + "/enter");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
