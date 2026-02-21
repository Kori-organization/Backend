package com.example.koribackend.controller;

import com.example.koribackend.model.dao.AdministratorDAO;
import com.example.koribackend.model.dao.ProfessorDAO;
import com.example.koribackend.model.dao.StudentDAO;
import com.example.koribackend.model.entity.Student;
import com.example.koribackend.util.FileCpfManager;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;

@WebServlet(urlPatterns = {"/enter","/forgotPassword","/createAccount"})
public class LoginController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        if (path.equals("/enter")) {
            response.sendRedirect("index.jsp");
        } else if (path.equals("/forgotPassword")) {
            request.getRequestDispatcher("WEB-INF/view/forgot-password.jsp").forward(request,response);
        } else if (path.equals("/createAccount")) {
            request.getRequestDispatcher("WEB-INF/view/create-account.jsp").forward(request,response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        if (path.equals("/enter")) {
            enterScreen(request, response);
        } else if (path.equals("/createAccount")) {
            createAccount(request,response);
        }
    }

    private void createAccount(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession(false).getAttribute("cpfs") == null) {
            request.getSession().setAttribute("cpfs",new FileCpfManager(getServletContext()));
        }

        String name = request.getParameter("name");
        String cpf = request.getParameter("cpf");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        FileCpfManager cpfs = (FileCpfManager) request.getSession(false).getAttribute("cpfs");
        Student student = new Student(email, Date.valueOf(LocalDate.now()),password,name,1);
        if (cpfs.checkCpf(cpf)) {
            if (!(new StudentDAO().accountExists(email))) {
                new StudentDAO().createAccount(student);
                request.getSession().setAttribute("cpfs",cpfs.deleteCpf(cpf));
                request.setAttribute("email", student.getEmail());
                request.setAttribute("password", student.getPassword());
                request.getRequestDispatcher("index.jsp").forward(request, response);
            } else {
                request.setAttribute("accountExists",true);
                returnAttributesAccount(request,response);
                request.getRequestDispatcher("WEB-INF/view/create-account.jsp").forward(request,response);
            }
        } else {
            request.setAttribute("canCreateAccount",false);
            returnAttributesAccount(request,response);
            request.getRequestDispatcher("WEB-INF/view/create-account.jsp").forward(request,response);
        }

    }

    private void returnAttributesAccount(HttpServletRequest request, HttpServletResponse response) {
        String name = request.getParameter("name"); request.setAttribute("name",name);
        String cpf = request.getParameter("cpf"); request.setAttribute("cpf",cpf);
        String email = request.getParameter("email"); request.setAttribute("email",email);
        String password = request.getParameter("password"); request.setAttribute("password",password);
    }

    private void enterScreen(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String emailOrUser = request.getParameter("emailOrUser");
        String password = request.getParameter("password");
        if (emailOrUser.matches("^@.+$")) {
            emailOrUser = emailOrUser.split("@")[1];
            if (new AdministratorDAO().loginValid(emailOrUser, password)) {
                request.getSession().setAttribute("admin",new AdministratorDAO().selectAdministratorForUsername(emailOrUser));
                response.sendRedirect("homeAdmin");
            } else {
                accountNotFound(request,response);
            }
        } else if (emailOrUser.matches("^\\w+\\.\\w+$")) {
            if (new ProfessorDAO().loginValid(emailOrUser,password)) {
                System.out.println("You are a Professor");
            } else {
                accountNotFound(request,response);
            }
        } else if (emailOrUser.matches("^[A-Za-z0-9._+-]+@[A-Za-z0-9-]+(\\.[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?)+$|^$")) {
            if (new StudentDAO().loginValid(emailOrUser, password)) {
                request.getSession().setAttribute("student",new StudentDAO().selectStudentForEmail(emailOrUser));
                response.sendRedirect("homeStudent");
            } else {
                accountNotFound(request,response);
            }
        } else {
            accountNotFound(request,response);
        }
    }

    private void accountNotFound(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String emailOrUser = request.getParameter("emailOrUser");
        String password = request.getParameter("password");
        request.setAttribute("email",emailOrUser);
        request.setAttribute("password",password);
        request.setAttribute("accountExists",false);
        request.getRequestDispatcher("index.jsp").forward(request,response);
    }
}
