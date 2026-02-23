package com.example.koribackend.controller;

import com.example.koribackend.model.dao.ProfessorDAO;
import com.example.koribackend.model.dao.StudentDAO;
import com.example.koribackend.model.entity.Professor;
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
        "/updateStudent"})
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
        }
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
        request.getSession().setAttribute("serie",serie);
        ArrayList<Student> students = new StudentDAO().selectStudentForSerie(serie);
        request.setAttribute("students",students);
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
        }
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
