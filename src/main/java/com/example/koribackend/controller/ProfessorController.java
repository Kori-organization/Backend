package com.example.koribackend.controller;

// Import DTOs, DAOs, and Entity models for academic and behavioral data
import com.example.koribackend.dto.StudentDTO;
import com.example.koribackend.model.dao.*;
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

// Map multiple endpoints related to professor actions and student management
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
        "/addRec",
        "/studentsFilter",
        "/studentsFilterDTO"})
public class ProfessorController extends HttpServlet {

    // Handle navigation and data retrieval for the professor interface
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession(false).getAttribute("professor") == null) {
            response.sendRedirect("index.jsp");
        }
        String path = request.getServletPath();
        switch (path) {
            case "/homeProfessor":
                // Fetch the subject assigned to the logged-in professor and display the class ranking
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
            case "/studentsFilter":
                studentFilter(request, response);
                break;
            case "/studentsFilterDTO":
                studentFilterDTO(request, response);
                break;
        }
    }

    // Handle data submission such as observations and grade updates
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession(false).getAttribute("professor") == null) {
            response.sendRedirect("index.jsp");
        }
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

    // Update the recovery (remedial) grade for a specific student and subject
    private void addRec(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException {
        // Parse the recovery grade, defaulting to -1 if the input is empty
        double rec = request.getParameter("rec").equals("") ? -1 : Double.parseDouble(request.getParameter("rec"));
        int enrollment = Integer.parseInt(request.getParameter("enrollment"));
        String subject = request.getParameter("subject");
        boolean result = new GradeDAO().updateRec(rec,enrollment,subject);
        request.getSession().setAttribute("resultEditRec",String.valueOf(result));
        response.sendRedirect("reportCardStudentsList");
    }

    // Update standard grades (n1 and n2) for a specific student and subject
    private void addGrades(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        // Parse input grades and handle empty values as -1
        double n1 = request.getParameter("n1").equals("") ? -1 : Double.parseDouble(request.getParameter("n1"));
        double n2 = request.getParameter("n2").equals("") ? -1 : Double.parseDouble(request.getParameter("n2"));
        int enrollment = Integer.parseInt(request.getParameter("enrollment"));
        String subject = request.getParameter("subject");
        boolean result = new GradeDAO().updateGrades(n1, n2, enrollment,subject);
        request.getSession().setAttribute("resultEditGrades",String.valueOf(result));
        response.sendRedirect("reportCardStudentsList");
    }

    // Display a list of students for behavioral observation filtering by grade level or search criteria
    private void showStudentsObservationList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int grade;
        // Check session for grade level to maintain state during filtering
        if (request.getSession(false).getAttribute("grade") != null && request.getParameter("grade") == null) {
            grade = (int) request.getSession(false).getAttribute("grade");
        } else {
            grade = Integer.parseInt(request.getParameter("grade"));
        }
        request.getSession().setAttribute("grade",grade);
        String filter = request.getParameter("filter") == null ? "" : request.getParameter("filter");
        List<Student> students;

        // Execute search logic based on whether the filter is empty, numeric (enrollment), or text-based
        if (filter.equals("")) {
            students = new StudentDAO().selectStudentAllByGrade(grade);
        } else if (filter.matches("^[0-9]+$")) {
            students = new StudentDAO().selectStudentsByEnrollment(Integer.parseInt(filter));
        } else {
            students = new StudentDAO().selectStudentsByNameOrEmail(filter,grade);
        }
        request.setAttribute("students", students);
        request.setAttribute("filter",filter);
        request.getRequestDispatcher("WEB-INF/view/professor/obs-students-list.jsp").forward(request, response);
    }

    // Fetch and display specific behavioral notes for a single student
    private void showStudentObservation(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int enrollment;
        if (request.getSession(false).getAttribute("enrollmentObs") != null && request.getParameter("enrollment") == null) {
            enrollment = (int) request.getSession(false).getAttribute("enrollmentObs");
        } else {
            enrollment = Integer.parseInt(request.getParameter("enrollment"));
        }
        request.getSession().setAttribute("enrollmentObs",enrollment);
        Student student = new StudentDAO().selectStudentByEnrollment(enrollment);
        List<Observation> observations = new ObservationDAO().selectObservationsForStudent(enrollment);

        String resultAddObs = (String) request.getSession().getAttribute("resultAddObs");
        if (resultAddObs != null) {
            request.setAttribute("resultAddObs", resultAddObs);
            request.getSession().removeAttribute("resultAddObs");
        }

        request.setAttribute("student", student);
        request.setAttribute("observations", observations);
        request.getRequestDispatcher("WEB-INF/view/professor/obs-student.jsp").forward(request,response);
    }

    // Display student list specifically formatted for report card and grade management
    private void showStudentsReportCardList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int grade;
        if (request.getSession(false).getAttribute("grade") != null && request.getParameter("grade") == null) {
            grade = (int) request.getSession(false).getAttribute("grade");
        } else {
            grade = Integer.parseInt(request.getParameter("grade"));
        }
        request.getSession().setAttribute("grade",grade);
        String filter = request.getParameter("filter") == null ? "" : request.getParameter("filter");
        List<StudentDTO> students;

        // Retrieve student DTOs including performance data for the specific subject
        if (filter.equals("")) {
            students = new StudentDAO().selectStudentDTOAllByGrade(grade, ((Professor) request.getSession().getAttribute("professor")).getSubjectName());
        } else if (filter.matches("^[0-9]+$")) {
            students = new StudentDAO().selectStudentDTOByEnrollment(Integer.parseInt(filter), ((Professor) request.getSession().getAttribute("professor")).getSubjectName());
        } else {
            students = new StudentDAO().selectStudentDTOByNameOrEmail(filter, ((Professor) request.getSession().getAttribute("professor")).getSubjectName(),grade);
        }

        String resultEditGrades = (String) request.getSession().getAttribute("resultEditGrades");
        if (resultEditGrades != null) {
            request.setAttribute("resultEditGrades", resultEditGrades);
            request.getSession().removeAttribute("resultEditGrades");
        }

        String resultEditRec = (String) request.getSession().getAttribute("resultEditRec");
        if (resultEditRec != null) {
            request.setAttribute("resultEditRec", resultEditRec);
            request.getSession().removeAttribute("resultEditRec");
        }

        request.setAttribute("students", students);
        request.setAttribute("filter",filter);
        request.getRequestDispatcher("WEB-INF/view/professor/reportcard-students-list.jsp").forward(request, response);
    }

    // Insert a new behavioral observation for a student and refresh the view
    private void addObservation(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int enrollment = (int) request.getSession(false).getAttribute("enrollmentObs");
        String observation = request.getParameter("observation");
        boolean result = new ObservationDAO().insertObservation(enrollment, observation);
        request.getSession().setAttribute("resultAddObs", String.valueOf(result));
        response.sendRedirect("obsStudent?enrollment=" + enrollment);
    }

    // Invalidate the session and redirect the professor back to the login screen
    private void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect("logout");
    }

    // Retrieve and display the full academic report card for a specific student
    private void showStudentReportCard(HttpServletRequest request, HttpServletResponse response, int enrollment) throws ServletException, IOException {
        ReportCard reportCard = new ReportCardDAO().selectReportCard(enrollment);
        Student student = new StudentDAO().selectStudentByEnrollment(enrollment);
        request.setAttribute("reportCard",reportCard);
        request.setAttribute("student",student);
        request.getRequestDispatcher("/WEB-INF/view/professor/reportcard.jsp").forward(request,response);
    }

    public void studentFilterDTO(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Professor professor = (Professor) request.getSession().getAttribute("professor");
        String situation = request.getParameter("situation");
        System.out.println(situation);
        int grade;
        // Check session for grade level to maintain state during filtering
        if (request.getSession(false).getAttribute("grade") != null && request.getParameter("grade") == null) {
            grade = (int) request.getSession(false).getAttribute("grade");
        } else {
            grade = Integer.parseInt(request.getParameter("grade"));
        }

        String page = request.getParameter("page");
        ArrayList<StudentDTO> studentsDTOS = new ProfessorDAO().StudentsFilterDTO(situation, grade, professor.getSubjectName());

        request.setAttribute("students", studentsDTOS);
        request.getRequestDispatcher("/WEB-INF/view/professor/" + page + ".jsp").forward(request, response);
    }

    public void studentFilter(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Professor professor = (Professor) request.getSession().getAttribute("professor");
        String situation = request.getParameter("situation");

        int grade;
        // Check session for grade level to maintain state during filtering
        if (request.getSession(false).getAttribute("grade") != null && request.getParameter("grade") == null) {
            grade = (int) request.getSession(false).getAttribute("grade");
        } else {
            grade = Integer.parseInt(request.getParameter("grade"));
        }

        String page = request.getParameter("page");
        ArrayList<Student> students = new ProfessorDAO().StudentsFilter(situation, grade, professor.getSubjectName());

        request.setAttribute("students", students);
        request.getRequestDispatcher("/WEB-INF/view/professor/" + page + ".jsp").forward(request, response);
    }
}