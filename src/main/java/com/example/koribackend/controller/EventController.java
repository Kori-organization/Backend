package com.example.koribackend.controller;

import com.example.koribackend.model.dao.AdministratorDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

@WebServlet(urlPatterns = {"/selectAllEvents"})
public class EventController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        if (path.equals("/selectAllEvents")) {
            selectAllEvents(request, response);
        }
    }

    private void selectAllEvents(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ArrayList<Map<String, Object>> result = new AdministratorDAO().selectAllEvents();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String json = new com.google.gson.Gson().toJson(result);
        response.getWriter().write(json);
    }
}
