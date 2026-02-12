package com.example.koribackend.controller;

import com.example.koribackend.model.dao.AdministratorDAO;
import com.example.koribackend.model.dao.ProfessorDAO;
import com.example.koribackend.model.dao.StudentDAO;
import com.example.koribackend.model.entity.Student;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.Date;

@WebServlet(urlPatterns = {"/enter","/forgotPassword"})
public class LoginController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        if (path.equals("/enter")) {
            response.sendRedirect("index.jsp");
        } else if (path.equals("/forgotPassword")) {
            request.getRequestDispatcher("WEB-INF/view/forgot-password.jsp").forward(request,response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        if (path.equals("/enter")) {
            enterScreen(request, response);
        }
    }

    public void enterScreen(HttpServletRequest request, HttpServletResponse response) {
        String emailOrUser = request.getParameter("emailOrUser");
        String password = request.getParameter("password");
        if (emailOrUser.matches("^@.+$")) {
            emailOrUser = emailOrUser.split("@")[1];
            if (new AdministratorDAO().loginValid(emailOrUser, password)) {
                System.out.println("You are a Admin Kori");
            } else {
                System.out.println("Implement text of error - Admin");
            }
        } else if (emailOrUser.matches("^\\w+\\.\\w+$")) {
            if (new ProfessorDAO().loginValid(emailOrUser,password)) {
                System.out.println("You are a Professor");
            } else {
                System.out.println("Implement text of error - Professor");
            }
        } else if (emailOrUser.matches("^[A-Za-z0-9._+-]+@[A-Za-z0-9-]+(\\.[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?)+$|^$")) {
            if (new StudentDAO().loginValid(emailOrUser, password)) {
                System.out.println("You are a Student");
            } else {
                System.out.println("Implement text of error - Student");
            }
        }
    }
}
