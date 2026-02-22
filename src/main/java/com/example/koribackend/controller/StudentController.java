package com.example.koribackend.controller;

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

@WebServlet(urlPatterns = {"/homeStudent","/reportCard","/observations","/createPDF", "/informationStudent"})
public class StudentController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        if (path.equals("/homeStudent")) {
            request.getRequestDispatcher("WEB-INF/view/student/homeStudent.jsp").forward(request,response);
        } else if (path.equals("/reportCard")) {
            showBulletin(request,response);
        } else if (path.equals("/observations")) {
            showObservations(request,response);
        } else if (path.equals("/createPDF")) {
            int enrollment = ((Student) request.getSession(false).getAttribute("student")).getEnrollment();
            request.setAttribute("enrollment",enrollment);
            request.getRequestDispatcher("createReportCardPDF").forward(request,response);
        } else if (path.equals("/informationStudent")) {
            request.getRequestDispatcher("WEB-INF/view/student/information.jsp").forward(request, response);
        }
    }

    private void showBulletin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Student student = (Student) request.getSession(false).getAttribute("student");
        ReportCard reportCard = new ReportCardDAO().selectReportCard(student.getEnrollment());
        request.setAttribute("bulletin",reportCard);
        request.getRequestDispatcher("WEB-INF/view/student/bulletin.jsp").forward(request,response);
    }

    private void showObservations(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Student student = (Student) request.getSession(false).getAttribute("student");
        List<Observation> observations = new ObservationDAO().selectObservationsForStudent(student.getEnrollment());
        request.setAttribute("observations",observations);
        request.getRequestDispatcher("WEB-INF/view/student/observation.jsp").forward(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
