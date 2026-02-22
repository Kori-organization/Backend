package com.example.koribackend.controller;

import com.example.koribackend.model.dao.ProfessorDAO;
import com.example.koribackend.model.dao.StudentDAO;
import com.example.koribackend.model.entity.Professor;
import com.example.koribackend.model.entity.Student;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;

@WebServlet(urlPatterns = {
        "/homeAdmin",
        "/informationsAdmin",
        "/logoutAdmin",
        "/createStudent",
        "/createProfessor"})
public class AdminController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        switch (path) {
            case "/homeAdmin":
                request.getRequestDispatcher("/WEB-INF/view/admin/homeAdmin.jsp").forward(request,response);
                break;
            case "/informationsAdmin":
                request.getRequestDispatcher("/WEB-INF/view/admin/information.jsp").forward(request,response);
                break;
            case "/logoutAdmin":
                logout(request,response);
                break;
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
        String path = request.getServletPath();
        switch (path) {
            case "/createStudent":
                createStudent(request,response);
                break;
            case "/createProfessor":
                createProfessor(request,response);
                break;
        }
    }

    private void createProfessor(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String user = request.getParameter("user");
        String name = request.getParameter("name");
        String subject = request.getParameter("subject");
        String password = request.getParameter("password");

        boolean result = new ProfessorDAO().createAccount(new Professor(user, password, name, subject));
        request.setAttribute("resultProfessor",String.valueOf(result));
        request.getRequestDispatcher("/WEB-INF/view/admin/homeAdmin.jsp").forward(request,response);
    }

    private void createStudent(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        Date admission = Date.valueOf(LocalDate.parse(request.getParameter("admission")));
        int studentGrade = Integer.parseInt(request.getParameter("studentGrade"));
        String password = request.getParameter("password");

        boolean result = new StudentDAO().createAccount(new Student(email,admission,password,name,studentGrade));
        request.setAttribute("resultStudent",String.valueOf(result));
        request.getRequestDispatcher("/WEB-INF/view/admin/homeAdmin.jsp").forward(request,response);
    }
}
