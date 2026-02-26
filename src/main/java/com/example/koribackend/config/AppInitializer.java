package com.example.koribackend.config;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.sql.Connection;
import java.sql.PreparedStatement;

@WebListener
public class AppInitializer implements ServletContextListener, HttpSessionListener, HttpSessionAttributeListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        sce.getServletContext().setAttribute("appReady", false);
    }
}
