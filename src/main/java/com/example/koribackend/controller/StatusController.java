package com.example.koribackend.controller;

// Import the database connection utility and Jakarta Servlet API
import com.example.koribackend.config.ConnectionFactory;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

// Map the servlet to the /status endpoint to check application health
@WebServlet(urlPatterns = {"/status"})
public class StatusController extends HttpServlet {

    // Process GET requests to determine if the application is fully initialized
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        // Set the response type to plain text
        resp.setContentType("text/plain");

        // Retrieve the global application state from the ServletContext
        ServletContext context = getServletContext();
        Boolean ready = (Boolean) context.getAttribute("appReady");

        // If the application was previously marked as ready, return immediate confirmation
        if (ready != null && ready) {
            resp.getWriter().write("READY");
            return;
        }

        try {
            // Perform a warm-up by establishing five consecutive database connections
            for (int i = 0; i < 5; i++) {
                Connection conn = ConnectionFactory.getConnection();
                // Execute a simple query to verify the connection is active
                PreparedStatement stmt = conn.prepareStatement("SELECT 1");
                stmt.execute();
                // Return the connection to the HikariCP pool
                conn.close();
            }

            // Mark the application as ready in the global context for future checks
            context.setAttribute("appReady", true);
            resp.getWriter().write("READY");
        } catch (Exception e) {
            // Return an error status if the database connection fails during initialization
            resp.getWriter().write("ERROR");
        }
    }
}