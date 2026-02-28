package com.example.koribackend.config;

// Import Jakarta Servlet API classes for web lifecycle management
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

// Register this class as a listener for application, session, and attribute events
@WebListener
public class AppInitializer implements ServletContextListener, HttpSessionListener, HttpSessionAttributeListener {

    // Define the logic to be executed when the web application starts
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // Initialize a global context attribute to flag that the application is not yet ready
        sce.getServletContext().setAttribute("appReady", false);
    }
}