package com.example.koribackend.controller;

import com.example.koribackend.dto.StudentDTO;
import com.example.koribackend.model.dao.GradeDAO;
import com.example.koribackend.model.dao.ObservationDAO;
import com.example.koribackend.model.dao.ReportCardDAO;
import com.example.koribackend.model.dao.StudentDAO;
import com.example.koribackend.model.entity.Observation;
import com.example.koribackend.model.entity.Professor;
import com.example.koribackend.model.entity.ReportCard;
import com.example.koribackend.model.entity.Student;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(urlPatterns = {
        "/homeProfessor",
        "/obsStudentsList",
        "/observationGrades",
        "/obsStudent",
        "/addObservation",
        "/reportCardGrades",
        "/reportCardStudentsList",
        "/informationProfessor",
        "/profileProfessor",
        "/logoutProfessor",
        "/studentReportCard",
        "/addGrades",
        "/addRec"})
public class ProfessorController extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();

        switch (path) {
            case "/homeProfessor":
                String subjectName = ((Professor) request.getSession(false).getAttribute("professor")).getSubjectName();
                request.setAttribute("raking",new StudentDAO().selectClassRanking(subjectName));
                request.getRequestDispatcher("WEB-INF/view/professor/homeProfessor.jsp").forward(request,response);
                break;
            case "/observationGrades":
                request.getRequestDispatcher("WEB-INF/view/professor/observation-grades.jsp").forward(request,response);
                break;
            case "/obsStudentsList":
                showStudentsObservationList(request, response);
                break;
            case "/obsStudent":
                showStudentObservation(request, response);
                break;
            case "/reportCardGrades":
                request.getRequestDispatcher("WEB-INF/view/professor/reportcard-grades.jsp").forward(request, response);
                break;
            case "/reportCardStudentsList":
                showStudentsReportCardList(request, response);
                break;
            case "/informationProfessor":
                request.getRequestDispatcher("WEB-INF/view/professor/information.jsp").forward(request, response);
                break;
            case "/profileProfessor":
                request.getRequestDispatcher("WEB-INF/view/professor/profile.jsp").forward(request, response);
                break;
            case "/logoutProfessor":
                logout(request, response);
                break;
            case "/studentReportCard":
                showStudentReportCard(request, response, Integer.parseInt(request.getParameter("studentId")));
                break;
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();

        switch (path) {
            case "/addObservation":
                addObservation(request, response);
                break;
            case "/addGrades":
                addGrades(request,response);
                break;
            case "/addRec":
                addRec(request,response);
                break;
        }
    }

    private void addRec(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException {
        double rec = request.getParameter("rec").equals("") ? -1 : Double.parseDouble(request.getParameter("rec"));
        int enrollment = Integer.parseInt(request.getParameter("enrollment"));
        String subject = request.getParameter("subject");
        boolean result = new GradeDAO().updateRec(rec,enrollment,subject);
        request.setAttribute("resultEditRec",String.valueOf(result));
        showStudentsReportCardList(request,response);
    }

    private void addGrades(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        double n1 = request.getParameter("n1").equals("") ? -1 : Double.parseDouble(request.getParameter("n1"));
        double n2 = request.getParameter("n2").equals("") ? -1 : Double.parseDouble(request.getParameter("n2"));
        int enrollment = Integer.parseInt(request.getParameter("enrollment"));
        String subject = request.getParameter("subject");
        boolean result = new GradeDAO().updateGrades(n1, n2, enrollment,subject);
        request.setAttribute("resultEditGrades",String.valueOf(result));
        showStudentsReportCardList(request,response);
    }

    private void showStudentsObservationList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int grade;
        if (request.getSession(false).getAttribute("grade") != null) {
            grade = (int) request.getSession(false).getAttribute("grade");
        } else {
            grade = Integer.parseInt(request.getParameter("grade"));
        }
        request.getSession().setAttribute("grade",grade);
        String filter = request.getParameter("filter") == null ? "" : request.getParameter("filter");
        List<Student> students;
        if (filter.equals("")) {
            students = new StudentDAO().selectStudentAllByGrade(grade);
        } else if (filter.matches("^[0-9]+$")) {
            students = new StudentDAO().selectStudentsByEnrollment(Integer.parseInt(filter));
        } else {
            students = new StudentDAO().selectStudentsByNameOrEmail(filter,grade);
        }
        request.setAttribute("students", students);
        request.getRequestDispatcher("WEB-INF/view/professor/obs-students-list.jsp").forward(request, response);
    }

    private void showStudentObservation(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int enrollment;
        if (request.getSession(false).getAttribute("enrollmentObs") != null) {
            enrollment = (int) request.getSession(false).getAttribute("enrollmentObs");
        } else {
            enrollment = Integer.parseInt(request.getParameter("enrollment"));
        }
        request.getSession().setAttribute("enrollmentObs",enrollment);
        Student student = new StudentDAO().selectStudentByEnrollment(enrollment);
        List<Observation> observations = new ObservationDAO().selectObservationsForStudent(enrollment);
        request.setAttribute("student", student);
        request.setAttribute("observations", observations);
        request.getRequestDispatcher("WEB-INF/view/professor/obs-student.jsp").forward(request,response);
    }

    private void showStudentsReportCardList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int grade;
        if (request.getSession(false).getAttribute("grade") != null) {
            grade = (int) request.getSession(false).getAttribute("grade");
        } else {
            grade = Integer.parseInt(request.getParameter("grade"));
        }
        request.getSession().setAttribute("grade",grade);
        String filter = request.getParameter("filter") == null ? "" : request.getParameter("filter");
        List<StudentDTO> students;
        if (filter.equals("")) {
            students = new StudentDAO().selectStudentDTOAllByGrade(grade, ((Professor) request.getSession().getAttribute("professor")).getSubjectName());
        } else if (filter.matches("^[0-9]+$")) {
            students = new StudentDAO().selectStudentDTOByEnrollment(Integer.parseInt(filter), ((Professor) request.getSession().getAttribute("professor")).getSubjectName());
        } else {
            students = new StudentDAO().selectStudentDTOByNameOrEmail(filter, ((Professor) request.getSession().getAttribute("professor")).getSubjectName());
        }
        request.setAttribute("students", students);
        request.setAttribute("filter",filter);
        request.getRequestDispatcher("WEB-INF/view/professor/reportcard-students-list.jsp").forward(request, response);
    }

    private void addObservation(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int enrollment = (int) request.getSession(false).getAttribute("enrollmentObs");
        String observation = request.getParameter("observation");
        boolean result = new ObservationDAO().insertObservation(enrollment, observation);
        request.setAttribute("resultAddObs",String.valueOf(result));
        showStudentObservation(request,response);
    }

    private void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        response.sendRedirect("enter");
    }

    private void showStudentReportCard(HttpServletRequest request, HttpServletResponse response, int enrollment) throws ServletException, IOException {
        ReportCard reportCard = new ReportCardDAO().selectReportCard(enrollment);
        Student student = new StudentDAO().selectStudentByEnrollment(enrollment);
        request.setAttribute("reportCard",reportCard);
        request.setAttribute("student",student);
        request.getRequestDispatcher("/WEB-INF/view/professor/reportcard.jsp").forward(request,response);
    }
}
