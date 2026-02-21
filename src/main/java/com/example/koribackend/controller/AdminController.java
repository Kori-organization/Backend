package com.example.koribackend.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(urlPatterns = {"/homeAdmin","/informationsAdmin","/logoutAdmin"})
public class AdminController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        if (path.equals("/homeAdmin")) {
            request.getRequestDispatcher("WEB-INF/view/admin/homeAdmin.jsp").forward(request,response);
        } else if (path.equals("/informationsAdmin")) {
            request.getRequestDispatcher("WEB-INF/view/admin/information.jsp").forward(request,response);
        } else if (path.equals("/logoutAdmin")) {
            logout(request,response);
        }
    }

    private void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        response.sendRedirect("enter");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
