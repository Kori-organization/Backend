package com.example.koribackend.controller;

// Import necessary DAO classes for database interactions
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

// Define URL patterns for login, password recovery, and registration
@WebServlet(urlPatterns = {"/enter","/forgotPassword","/createAccount"})
public class LoginController extends HttpServlet {

    // Handle GET requests to navigate between login and registration views
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        if (path.equals("/enter")) {
            // Redirect to the main index page for login
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            response.sendRedirect("index.jsp");
        } else if (path.equals("/forgotPassword")) {
            // Forward to the forgot password view
            request.getRequestDispatcher("WEB-INF/view/forgot-password.jsp").forward(request,response);
        } else if (path.equals("/createAccount")) {
            // Forward to the account creation view
            request.getRequestDispatcher("WEB-INF/view/create-account.jsp").forward(request,response);
        }
    }

    // Handle POST requests for sensitive operations like logging in and creating accounts
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        if (path.equals("/enter")) {
            enterScreen(request, response);
        } else if (path.equals("/createAccount")) {
            createAccount(request,response);
        }
    }

    // Process the registration logic for new students
    private void createAccount(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Ensure the CPF manager is available in the session to validate registration codes
        if (request.getSession(false).getAttribute("cpfs") == null) {
            request.getSession().setAttribute("cpfs",new FileCpfManager(getServletContext()));
        }

        // Retrieve form data from the request
        String name = request.getParameter("name");
        String cpf = request.getParameter("cpf");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        FileCpfManager cpfs = (FileCpfManager) request.getSession(false).getAttribute("cpfs");

        // Instantiate a new Student entity with a default grade level of 1
        Student student = new Student(email, Date.valueOf(LocalDate.now()),password,name,1);

        // Validate the CPF against the pre-loaded file manager
        if (cpfs.checkCpf(cpf)) {
            // Ensure the email is not already registered in the database
            if (!(new StudentDAO().accountExists(email))) {
                new StudentDAO().createAccount(student);
                // Remove the used CPF from the valid list to prevent reuse
                request.getSession().setAttribute("cpfs",cpfs.deleteCpf(cpf));
                request.setAttribute("email", student.getEmail());
                request.setAttribute("password", student.getPassword());
                request.getRequestDispatcher("index.jsp").forward(request, response);
            } else {
                // Return an error if the email is already in use
                request.setAttribute("accountExists",true);
                returnAttributesAccount(request,response);
                request.getRequestDispatcher("WEB-INF/view/create-account.jsp").forward(request,response);
            }
        } else {
            // Return an error if the CPF is invalid or already used
            request.setAttribute("canCreateAccount",false);
            returnAttributesAccount(request,response);
            request.getRequestDispatcher("WEB-INF/view/create-account.jsp").forward(request,response);
        }
    }

    // Preserve form input values to improve user experience on validation failure
    private void returnAttributesAccount(HttpServletRequest request, HttpServletResponse response) {
        String name = request.getParameter("name"); request.setAttribute("name",name);
        String cpf = request.getParameter("cpf"); request.setAttribute("cpf",cpf);
        String email = request.getParameter("email"); request.setAttribute("email",email);
        String password = request.getParameter("password"); request.setAttribute("password",password);
    }

    // Manage the login process by identifying the user type via Regex patterns
    private void enterScreen(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String emailOrUser = request.getParameter("emailOrUser");
        String password = request.getParameter("password");

        // Identify Admin users by the '@' prefix (e.g., @adminUser)
        if (emailOrUser.matches("^@.+$")) {
            emailOrUser = emailOrUser.split("@")[1];
            if (new AdministratorDAO().loginValid(emailOrUser, password)) {
                request.getSession().setAttribute("admin",new AdministratorDAO().selectAdministratorForUsername(emailOrUser));
                response.sendRedirect("homeAdmin");
            } else {
                accountNotFound(request,response);
            }
        }
        // Identify Professors by a 'firstname.lastname' pattern
        else if (emailOrUser.matches("^\\w+\\.\\w+$")) {
            if (new ProfessorDAO().loginValid(emailOrUser,password)) {
                request.getSession().setAttribute("professor", new ProfessorDAO().selectProfessorByUser(emailOrUser));
                response.sendRedirect("homeProfessor");
            } else {
                accountNotFound(request,response);
            }
        }
        // Identify Students by a standard email format
        else if (emailOrUser.matches("^[A-Za-z0-9._+-]+@[A-Za-z0-9-]+(\\.[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?)+$|^$")) {
            if (new StudentDAO().loginValid(emailOrUser, password)) {
                request.getSession().setAttribute("student",new StudentDAO().selectStudentForEmail(emailOrUser));
                response.sendRedirect("homeStudent");
            } else {
                accountNotFound(request,response);
            }
        }
        // Handle cases where the input does not match any known format
        else {
            accountNotFound(request,response);
        }
    }

    // Set error attributes and return to the login page on failed authentication
    private void accountNotFound(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String emailOrUser = request.getParameter("emailOrUser");
        String password = request.getParameter("password");
        request.setAttribute("email",emailOrUser);
        request.setAttribute("password",password);
        request.setAttribute("accountExists",false);
        request.getRequestDispatcher("index.jsp").forward(request,response);
    }
}