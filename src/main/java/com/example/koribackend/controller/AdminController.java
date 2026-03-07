package com.example.koribackend.controller;

// Import Data Transfer Objects, DAOs, and Entity models
import com.example.koribackend.dto.StudentObservationsDTO;
import com.example.koribackend.model.dao.*;
import com.example.koribackend.model.entity.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;

// Map multiple administrative URL patterns to this single controller
@WebServlet(urlPatterns = {
        "/homeAdmin",
        "/informationsAdmin",
        "/logoutAdmin",
        "/createStudent",
        "/createProfessor",
        "/showProfessors",
        "/deleteProfessor",
        "/editProfessor",
        "/showClass",
        "/selectClass",
        "/deleteStudent",
        "/updateStudent",
        "/observationsStudentAdmin",
        "/deleteObservation",
        "/showReportCardStudent",
        "/addGradesReportCard",
        "/salveEvent",
        "/deleteEvent",
        "/updateEvent",
        "/selectEvent"})
public class AdminController extends HttpServlet {

    // Handle incoming GET requests and route them to specific internal methods
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
            case "/showProfessors":
                showProfessors(request,response);
                break;
            case "/deleteProfessor":
                deleteProfessor(request,response);
                break;
            case "/showClass":
                request.getRequestDispatcher("/WEB-INF/view/admin/student.jsp").forward(request,response);
                break;
            case "/selectClass":
                selectClass(request,response);
                break;
            case "/deleteStudent":
                deleteStudent(request,response);
                break;
            case "/observationsStudentAdmin":
                observationsStudent(request,response);
                break;
            case "/deleteObservation":
                deleteObservation(request,response);
                break;
            case "/showReportCardStudent":
                showReportCardStudent(request,response);
                break;
        }
    }

    // Retrieve and display the report card for a specific student
    private void showReportCardStudent(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        int enrollment;
        // Check if enrollment exists in the session; otherwise, get it from the request parameter
        if (request.getSession(false).getAttribute("enrollmentBulletin") != null && request.getParameter("enrollmentBulletin") == null) {
            enrollment = (int) request.getSession(false).getAttribute("enrollmentBulletin");
        } else {
            enrollment = Integer.parseInt(request.getParameter("enrollmentBulletin"));
        }
        request.getSession().setAttribute("enrollmentBulletin",enrollment);
        // Fetch report card data via DAO
        ReportCard reportCard = new ReportCardDAO().selectReportCard(enrollment);
        request.setAttribute("reportCard",reportCard);
        request.getRequestDispatcher("WEB-INF/view/admin/bulletin.jsp").forward(request,response);
    }

    // Remove a specific behavioral observation and refresh the view
    private void deleteObservation(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        int id = Integer.parseInt(request.getParameter("id"));
        boolean result = new ObservationDAO().deleteObservation(id);
        request.setAttribute("resultDeleteObservation",String.valueOf(result));
        observationsStudent(request,response);
    }

    // Load and display all observations associated with a student
    private void observationsStudent(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        int enrollment = -1;
        if (request.getParameter("enrollment") != null) {
            enrollment = Integer.parseInt(request.getParameter("enrollment"));
        } else {
            enrollment = (int) request.getSession(false).getAttribute("enrollment");
        }
        request.getSession().setAttribute("enrollment",enrollment);
        // Map student data to a DTO containing observations
        Student student = new StudentDAO().selectStudentByEnrollment(enrollment);
        StudentObservationsDTO studentObservationsDTO = new StudentObservationsDTO(student.getEnrollment(),student.getName(),student.getEmail(),student.getSerie());
        studentObservationsDTO.getAllObservations();
        request.setAttribute("studentObservationsDTO",studentObservationsDTO);
        request.getRequestDispatcher("/WEB-INF/view/admin/observation.jsp").forward(request,response);
    }

    // Remove a student record from the database
    private void deleteStudent(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        int enrollment = Integer.parseInt(request.getParameter("enrollment"));
        String name = request.getParameter("name");
        boolean result = new StudentDAO().deleteStudent(enrollment);
        request.setAttribute("resultDeleteStudent",String.valueOf(result));
        request.setAttribute("name",name);
        request.getRequestDispatcher("selectClass").forward(request,response);
    }

    // Filter students by grade level (serie) and optional search criteria
    private void selectClass(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String serieString = request.getParameter("serie");
        int serie = -1;
        if (serieString != null) {
            serie = Integer.parseInt(serieString);
        } else {
            serie = (int) request.getSession().getAttribute("serie");
        }

        String filter = request.getParameter("filter");
        ArrayList<Student> students = null;
        if (filter == null) { filter = ""; }
        filter = filter.trim();

        // Branch search logic based on whether the filter is empty, numeric (enrollment), or text (name/email)
        if (filter.equals("")) {
            students = new StudentDAO().selectStudentForSerie(serie);
        } else if (filter.matches("^[0-9]+$")) {
            students = new StudentDAO().selectStudentForSerieAndEnrollment(serie,Integer.parseInt(filter));
        } else {
            students = new StudentDAO().selectStudentForSerieAndEmailOrName(serie,filter);
        }

        request.getSession().setAttribute("serie",serie);
        request.setAttribute("students",students);
        request.setAttribute("filter",filter);
        request.getRequestDispatcher("/WEB-INF/view/admin/student-2.jsp").forward(request,response);
    }

    // Remove a professor from the database
    private void deleteProfessor(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        boolean result = new ProfessorDAO().deleteById(id);
        request.setAttribute("name",name);
        request.setAttribute("resultDeleteProfessor",String.valueOf(result));
        request.getRequestDispatcher("showProfessors").forward(request,response);
    }

    // Fetch all professors and forward to the management view
    private void showProfessors(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ArrayList<Professor> professors = new ProfessorDAO().selectProfessorAll();
        request.setAttribute("professors",professors);
        request.getRequestDispatcher("/WEB-INF/view/admin/teacher.jsp").forward(request,response);
    }

    // Invalidate the current session and redirect to the login page
    private void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        response.sendRedirect("enter");
    }

    // Handle incoming POST requests for data creation and modification
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
            case "/editProfessor":
                editProfessor(request,response);
                break;
            case "/updateStudent":
                updateStudent(request,response);
                break;
            case "/addGradesReportCard":
                addGradesReportCard(request,response);
                break;
            case "/salveEvent":
                salveEvent(request, response);
                break;
            case "/deleteEvent":
                deleteEvent(request, response);
                break;
            case "/updateEvent":
                updateEvent(request, response);
                break;
            case "/selectEvent":
                selectEvent(request, response);
                break;
        }
    }

    // Batch process and update multiple subject grades for a student
    private void addGradesReportCard(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        ArrayList<Grade> grades = new ArrayList<>();
        int enrollment = (int) request.getSession(false).getAttribute("enrollmentBulletin");
        int size = Integer.parseInt(request.getParameter("size")) + 1;
        int count = 1;
        // Loop through the submitted parameters to collect grades for each subject
        while (count != size) {
            String subject = request.getParameter(String.format("subject_%d",count));
            String n1Param = request.getParameter(String.format("n1_%d", count));
            String n2Param = request.getParameter(String.format("n2_%d", count));
            String recParam = request.getParameter(String.format("rec_%d", count));

            // Parse values while handling empty strings as -1 (null equivalent)
            double n1 = (n1Param == null || n1Param.isBlank()) ? -1 : Double.parseDouble(n1Param);
            double n2 = (n2Param == null || n2Param.isBlank()) ? -1 : Double.parseDouble(n2Param);
            double rec = (recParam == null || recParam.isBlank()) ? -1 : Double.parseDouble(recParam);
            grades.add(new Grade(n1,n2,subject,rec));
            count++;
        }
        boolean result = new GradeDAO().updateAllGrades(grades,enrollment);
        request.setAttribute("resultAddAllGrades",String.valueOf(result));
        showReportCardStudent(request,response);
    }

    // Update existing student details
    private void updateStudent(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        int enrollment = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        Date admission = Date.valueOf(LocalDate.parse(request.getParameter("admission")));
        int serie = Integer.parseInt(request.getParameter("serie"));
        String password = request.getParameter("password");
        boolean result = new StudentDAO().updateStudent(new Student(enrollment,email,admission,password,name,serie));
        request.setAttribute("resultEditStudent",String.valueOf(result));
        request.setAttribute("name",name);
        selectClass(request,response);
    }

    // Update existing professor details
    private void editProfessor(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("idEdit"));
        String user = request.getParameter("userEdit");
        String name = request.getParameter("nameEdit");
        String subject = request.getParameter("subjectEdit");
        String password = request.getParameter("passwordEdit");
        int result = new ProfessorDAO().updateProfessor(new Professor(id,user,password,name,subject));
        request.setAttribute("name",name);
        request.setAttribute("resultEditProfessor",String.valueOf(result));
        showProfessors(request,response);
    }

    // Register a new professor in the system
    private void createProfessor(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String user = request.getParameter("user");
        String name = request.getParameter("name");
        String subject = request.getParameter("subject");
        String password = request.getParameter("password");
        int result = new ProfessorDAO().createAccount(new Professor(user, password, name, subject));
        request.setAttribute("resultProfessor",String.valueOf(result));
        request.getRequestDispatcher("/WEB-INF/view/admin/homeAdmin.jsp").forward(request,response);
    }

    // Register a new student in the system
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

    private void salveEvent(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String eventText = request.getParameter("eventText");
        String eventName = request.getParameter("eventName");
        String eventDate = request.getParameter("eventDate");
        String eventStart = request.getParameter("eventStart");
        String eventEnd = request.getParameter("eventEnd");
        String adminName = request.getParameter("adminName");
        Event event = new Event(eventName, eventDate, eventStart, eventEnd, eventText, adminName);
        boolean result = new AdministratorDAO().salveEventOnCalender(event);
        request.setAttribute("resultEvent",String.valueOf(result));
        request.getRequestDispatcher("/WEB-INF/view/admin/teacher.jsp").forward(request,response);
    }

    private void deleteEvent(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String eventDate = request.getParameter("eventDate");
        String adminName = request.getParameter("adminName");
        boolean result = new AdministratorDAO().deleteEventOnCalendar(adminName, eventDate);
        request.setAttribute("resultEvent",String.valueOf(result));
        request.getRequestDispatcher("/WEB-INF/view/admin/teacher.jsp").forward(request,response);
    }

    public void updateEvent(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String eventText = request.getParameter("eventText");
        String eventName = request.getParameter("eventName");
        String eventDate = request.getParameter("eventDate");
        String eventStart = request.getParameter("eventStart");
        String eventEnd = request.getParameter("eventEnd");
        String adminName = request.getParameter("adminName");
        Event event = new Event(eventName, eventDate, eventStart, eventEnd, eventText, adminName);
        boolean result = new AdministratorDAO().updateEventOnCalenar(event);
        request.setAttribute("resultEvent",String.valueOf(result));
        request.getRequestDispatcher("/WEB-INF/view/admin/teacher.jsp").forward(request,response);
    }

    private void selectEvent(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String eventDate = request.getParameter("eventDate");
        Map<String, Object> result = new AdministratorDAO().selectEventForDate(eventDate);
        request.setAttribute("resultEvent",String.valueOf(result));
        request.getRequestDispatcher("/WEB-INF/view/admin/teacher.jsp").forward(request,response);
    }
}