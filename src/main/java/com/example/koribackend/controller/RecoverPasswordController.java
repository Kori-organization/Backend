package com.example.koribackend.controller;

// Import necessary internal models, utilities, and external libraries for JWT and Dotenv
import com.example.koribackend.model.dao.StudentDAO;
import com.example.koribackend.model.entity.Student;
import com.example.koribackend.util.JavaMail;
import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.Base64;
import java.util.Date;

// Define URL patterns related to the password recovery lifecycle
@WebServlet(urlPatterns = {"/recoverPassword", "/checkToken","/updatePassword"})
public class RecoverPasswordController extends HttpServlet {

    // Load environment variables and initialize the cryptographic key for JWT operations
    private Dotenv dotenv = Dotenv.load();
    private final String secretKey = dotenv.get("JWT_SECRET");
    private final SecretKey key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKey));

    // Handle GET requests, specifically for token validation when the user clicks the email link
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        if (path.equals("/checkToken")) {
            checkToken(request,response);
        }
    }

    // Validate the JWT token provided in the URL parameter
    private void checkToken(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String token = request.getParameter("token");

        // Redirect to the index if no token is provided
        if (token == null || token.isEmpty()) {
            response.sendRedirect("index.jsp");
        }

        try {
            // Parse and verify the token signature using the secret key
            Claims claims = Jwts.parser()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            // Extract the student's enrollment ID from the token subject
            String userId = claims.getSubject();

            // Store the enrollment in the session to identify the user during the password update
            request.getSession().setAttribute("enrollment",userId);
            request.getRequestDispatcher("/WEB-INF/view/create-password.jsp").forward(request, response);
        } catch (Exception e ) {
            // Log the error and redirect to index if the token is invalid or expired
            e.printStackTrace();
            response.sendRedirect("index.jsp");
        }
    }

    // Handle POST requests for sending the recovery email and performing the actual password update
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        if (path.equals("/recoverPassword")) {
            sendEmail(request,response);
        } else if (path.equals("/updatePassword")) {
            updatePassword(request,response);
        }
    }

    // Process the final step of updating the student's password in the database
    private void updatePassword(HttpServletRequest request, HttpServletResponse response) throws IOException,ServletException {
        String newPassword = request.getParameter("newPassword");
        // Retrieve the enrollment ID stored in the session during the checkToken step
        String enrollment = (String) request.getSession(false).getAttribute("enrollment");
        boolean resultAction = new StudentDAO().newPasswordStudent(Integer.parseInt(enrollment),newPassword);

        if (resultAction) {
            // Forward to a success view if the database update was successful
            request.getRequestDispatcher("WEB-INF/view/new-password.jsp").forward(request,response);
        } else {
            // Reload the password creation page if the update failed
            request.getRequestDispatcher("WEB-INF/view/create-password.jsp");
            System.out.println("Implement popup error");
        }
    }

    // Generate a secure JWT token and send a recovery email to the student
    private void sendEmail(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException {
        String email = request.getParameter("email");
        Student student = new StudentDAO().selectStudentForEmail(email);
        boolean result;

        if (student != null) {
            // Construct the base URL of the application dynamically
            String baseURL = String.format("%s://%s:%s%s",request.getScheme(), request.getServerName(),request.getServerPort(), request.getContextPath());

            // Build the JWT token with a 24-hour expiration time
            String token = Jwts.builder()
                    .setSubject(String.valueOf(student.getEnrollment())) // enrollment of student
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000)) // 1 day
                    .signWith(SignatureAlgorithm.HS256, key)
                    .compact();

            // Use the JavaMail utility to send the email containing the recovery link
            result = JavaMail.sendPasswordRecovery(email,token,baseURL);
        } else {
            // Set result to false if no student is found with the provided email
            result = false;
        }

        // Pass the result to the confirmation view to inform the user
        request.setAttribute("email",email);
        request.setAttribute("result",result);
        request.getRequestDispatcher("WEB-INF/view/confirmation-email.jsp").forward(request,response);
    }
}