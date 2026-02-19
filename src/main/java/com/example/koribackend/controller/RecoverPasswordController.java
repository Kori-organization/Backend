package com.example.koribackend.controller;

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

@WebServlet(urlPatterns = {"/recoverPassword", "/checkToken","/updatePassword"})
public class RecoverPasswordController extends HttpServlet {

    private Dotenv dotenv = Dotenv.load();
    private final String secretKey = dotenv.get("JWT_SECRET");
    private final SecretKey key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKey));
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        if (path.equals("/checkToken")) {
            checkToken(request,response);
        }
    }

    private void checkToken(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String token = request.getParameter("token");

        if (token == null || token.isEmpty()) {
            response.sendRedirect("index.jsp");
        }

        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String userId = claims.getSubject();

            request.getSession().setAttribute("enrollment",userId);
            request.getRequestDispatcher("/WEB-INF/view/create-password.jsp").forward(request, response);
        } catch (Exception e ) {
            e.printStackTrace();
            response.sendRedirect("index.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        if (path.equals("/recoverPassword")) {
            sendEmail(request,response);
        } else if (path.equals("/updatePassword")) {
            updatePassword(request,response);
        }
    }

    private void updatePassword(HttpServletRequest request, HttpServletResponse response) throws IOException,ServletException {
        String newPassword = request.getParameter("newPassword");
        String enrollment = (String) request.getSession(false).getAttribute("enrollment");
        boolean resultAction = new StudentDAO().newPasswordStudent(Integer.parseInt(enrollment),newPassword);
        if (resultAction) {
            request.getRequestDispatcher("/html/new-password.html").forward(request,response);
        } else {
            request.getRequestDispatcher("WEB-INF/view/create-password.jsp");
            System.out.println("Implement popup error");
        }
    }

    private void sendEmail(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException {
        String email = request.getParameter("email");
        Student student = new StudentDAO().selectStudentForEmail(email);
        boolean result;
        if (student != null) {
            String baseURL = String.format("%s://%s:%s%s",request.getScheme(), request.getServerName(),request.getServerPort(), request.getContextPath());
            String token = Jwts.builder()
                    .setSubject(String.valueOf(student.getEnrollment())) // enrollment of student
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000)) // 1 day
                    .signWith(SignatureAlgorithm.HS256, key)
                    .compact();
            result = JavaMail.sendPasswordRecovery(email,token,baseURL);
        } else {
            result = false;
        }
        request.setAttribute("email",email);
        request.setAttribute("result",result);
        request.getRequestDispatcher("WEB-INF/view/confirmation-email.jsp").forward(request,response);
    }
}
