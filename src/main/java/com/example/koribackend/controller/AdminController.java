package com.example.koribackend.controller;

import com.example.koribackend.dto.StudentObservationsDTO;
import com.example.koribackend.model.dao.*;
import com.example.koribackend.model.entity.Grade;
import com.example.koribackend.model.entity.Professor;
import com.example.koribackend.model.entity.ReportCard;
import com.example.koribackend.model.entity.Student;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;

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
        "/addGradesReportCard"})
public class AdminController extends HttpServlet {
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

    private void showReportCardStudent(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        int enrollment;
        if (request.getSession(false).getAttribute("enrollmentBulletin") != null) {
            enrollment = (int) request.getSession(false).getAttribute("enrollmentBulletin");
        } else {
            enrollment = Integer.parseInt(request.getParameter("enrollmentBulletin"));
        }
        request.getSession().setAttribute("enrollmentBulletin",enrollment);
        ReportCard reportCard = new ReportCardDAO().selectReportCard(enrollment);
        request.setAttribute("reportCard",reportCard);
        request.getRequestDispatcher("WEB-INF/view/admin/bulletin.jsp").forward(request,response);
    }

    private void deleteObservation(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        int id = Integer.parseInt(request.getParameter("id"));
        boolean result = new ObservationDAO().deleteObservation(id);
        request.setAttribute("resultDeleteObservation",String.valueOf(result));
        observationsStudent(request,response);
    }

    private void observationsStudent(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        int enrollment = -1;
        if (request.getParameter("enrollment") != null) {
            enrollment = Integer.parseInt(request.getParameter("enrollment"));
        } else {
            enrollment = (int) request.getSession(false).getAttribute("enrollment");
        }
        request.getSession().setAttribute("enrollment",enrollment);
        Student student = new StudentDAO().selectStudentByEnrollment(enrollment);
        StudentObservationsDTO studentObservationsDTO = new StudentObservationsDTO(student.getEnrollment(),student.getName(),student.getEmail(),student.getSerie());
        studentObservationsDTO.getAllObservations();
        request.setAttribute("studentObservationsDTO",studentObservationsDTO);
        request.getRequestDispatcher("/WEB-INF/view/admin/observation.jsp").forward(request,response);
    }

    private void deleteStudent(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        int enrollment = Integer.parseInt(request.getParameter("enrollment"));
        String name = request.getParameter("name");
        boolean result = new StudentDAO().deleteStudent(enrollment);
        request.setAttribute("resultDeleteStudent",String.valueOf(result));
        request.setAttribute("name",name);
        request.getRequestDispatcher("selectClass").forward(request,response);
    }
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

    private void deleteProfessor(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        boolean result = new ProfessorDAO().deleteById(id);
        request.setAttribute("name",name);
        request.setAttribute("resultDeleteProfessor",String.valueOf(result));
        request.getRequestDispatcher("showProfessors").forward(request,response);
    }

    private void showProfessors(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ArrayList<Professor> professors = new ProfessorDAO().selectProfessorAll();
        request.setAttribute("professors",professors);
        request.getRequestDispatcher("/WEB-INF/view/admin/teacher.jsp").forward(request,response);
    }

    private void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        response.sendRedirect("enter");
    }

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
        }
    }

    private void addGradesReportCard(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        ArrayList<Grade> grades = new ArrayList<>();
        int enrollment = (int) request.getSession(false).getAttribute("enrollmentBulletin");
        int size = Integer.parseInt(request.getParameter("size")) + 1;
        int count = 1;
        while (count != size) {
            String subject = request.getParameter(String.format("subject_%d",count));
            String n1Param = request.getParameter(String.format("n1_%d", count));
            String n2Param = request.getParameter(String.format("n2_%d", count));
            String recParam = request.getParameter(String.format("rec_%d", count));

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

    private void editProfessor(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("idEdit"));
        String user = request.getParameter("userEdit");
        String name = request.getParameter("nameEdit");
        String subject = request.getParameter("subjectEdit");
        String password = request.getParameter("passwordEdit");
        boolean result = new ProfessorDAO().updateProfessor(new Professor(id,user,password,name,subject));
        request.setAttribute("name",name);
        request.setAttribute("resultEditProfessor",String.valueOf(result));
        showProfessors(request,response);
    }

    private void createProfessor(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String user = request.getParameter("user");
        String name = request.getParameter("name");
        String subject = request.getParameter("subject");
        String password = request.getParameter("password");
        boolean result = new ProfessorDAO().createAccount(new Professor(user, password, name, subject));
        request.setAttribute("resultProfessor",String.valueOf(result));
        request.getRequestDispatcher("/WEB-INF/view/admin/homeAdmin.jsp").forward(request,response);
    }

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
}
