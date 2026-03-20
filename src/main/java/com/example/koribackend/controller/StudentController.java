package com.example.koribackend.controller;

// Import DAOs and Entity models for handling student-specific data
import com.example.koribackend.model.dao.ObservationDAO;
import com.example.koribackend.model.dao.ReportCardDAO;
import com.example.koribackend.model.entity.Observation;
import com.example.koribackend.model.entity.ReportCard;
import com.example.koribackend.model.entity.Student;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.List;

// Define the URL patterns accessible by student users
@WebServlet(urlPatterns = {
        "/homeStudent",
        "/reportCardStudent",
        "/observationsStudent",
        "/createPDF",
        "/informationsStudent",
        "/profileStudent",
        "/logoutStudent"})
public class StudentController extends HttpServlet {

    // Handle navigation and data retrieval requests for the student interface
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        switch (path) {
            case "/homeStudent":
                request.getRequestDispatcher("/WEB-INF/view/student/homeStudent.jsp").forward(request,response);
                break;
            case "/reportCardStudent":
                showBulletin(request,response);
                break;
            case "/observationsStudent":
                showObservations(request,response);
                break;
            case "/createPDF":
                // Retrieve student enrollment from session and forward to the PDF generation controller
                int enrollment = ((Student) request.getSession(false).getAttribute("student")).getEnrollment();
                request.setAttribute("enrollment",enrollment);
                request.getRequestDispatcher("createReportCardPDF").forward(request,response);
                break;
            case "/informationsStudent":
                request.getRequestDispatcher("/WEB-INF/view/student/information.jsp").forward(request,response);
                break;
            case "/profileStudent":
                request.getRequestDispatcher("/WEB-INF/view/student/profile.jsp").forward(request,response);
                break;
            case "/logoutStudent":
                logout(request,response);
                break;
        }
    }

    // Invalidate the current session and redirect to the login entry point
    private void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect("logout");
    }

    // Fetch and display the academic report card for the logged-in student
    private void showBulletin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Student student = (Student) request.getSession(false).getAttribute("student");
        ReportCard reportCard = new ReportCardDAO().selectReportCard(student.getEnrollment());
        request.setAttribute("bulletin",reportCard);
        request.getRequestDispatcher("/WEB-INF/view/student/bulletin.jsp").forward(request,response);
    }

    // Retrieve and display behavioral observations recorded for the logged-in student
    private void showObservations(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Student student = (Student) request.getSession(false).getAttribute("student");
        List<Observation> observations = new ObservationDAO().selectObservationsForStudent(student.getEnrollment());
        request.setAttribute("observations",observations);
        request.getRequestDispatcher("/WEB-INF/view/student/observation.jsp").forward(request,response);
    }
}