package com.example.koribackend.controller;

import com.example.koribackend.config.ConnectionFactory;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

@WebServlet(urlPatterns = {"/status"})
public class StatusController extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        resp.setContentType("text/plain");
        ServletContext context = getServletContext();
        Boolean ready = (Boolean) context.getAttribute("appReady");
        if (ready != null && ready) {
            resp.getWriter().write("READY");
            return;
        }
        try {
            for (int i = 0; i < 5; i++) {
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement("SELECT 1");
                stmt.execute();
                conn.close();
            }
            context.setAttribute("appReady", true);
            resp.getWriter().write("READY");
        } catch (Exception e) {
            resp.getWriter().write("ERROR");
        }
    }
}
