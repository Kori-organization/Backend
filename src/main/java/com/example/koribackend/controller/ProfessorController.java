package com.example.koribackend.controller;

import com.example.koribackend.dto.StudentDTO;
import com.example.koribackend.model.dao.ObservationDAO;
import com.example.koribackend.model.dao.StudentDAO;
import com.example.koribackend.model.entity.Observation;
import com.example.koribackend.model.entity.Professor;
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

@WebServlet(urlPatterns = {"/homeProfessor", "/obsStudentsList", "/observationGrades", "/obsStudent", "/addObservation", "/bulletinGrades", "/bulletinStudentsList", "/informationProfessor", "/profileProfessor", "/logoutProfessor"})
public class ProfessorController extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();

        if (path.equals("/homeProfessor")) {
            request.getRequestDispatcher("WEB-INF/view/professor/homeProfessor.jsp").forward(request,response);
        } else if (path.equals("/observationGrades")) {
            request.getRequestDispatcher("WEB-INF/view/professor/observation-grades.jsp").forward(request,response);
        } else if (path.equals("/obsStudentsList")) {
            String enrollment = request.getParameter("studentId") == null ? "" : request.getParameter("studentId");
            showStudentsObservationList(request, response, Integer.parseInt(request.getParameter("grade")), enrollment);
        } else if (path.equals("/obsStudent")) {
            showStudentObservation(request, response, Integer.parseInt(request.getParameter("studentId")));
        } else if (path.equals("/bulletinGrades")) {
            request.getRequestDispatcher("WEB-INF/view/professor/bulletin-grades.jsp").forward(request, response);
        } else if (path.equals("/bulletinStudentsList")) {
            String enrollment = request.getParameter("studentId") == null ? "" : request.getParameter("studentId");
            showStudentsBulletinList(request, response, Integer.parseInt(request.getParameter("grade")), enrollment);
        } else if (path.equals("/informationProfessor")) {
            request.getRequestDispatcher("WEB-INF/view/professor/information.jsp").forward(request, response);
        } else if (path.equals("/profileProfessor")) {
            request.getRequestDispatcher("WEB-INF/view/professor/profile.jsp").forward(request, response);
        } else if (path.equals("/logoutProfessor")) {
            logout(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();

        if (path.equals("/addObservation")) {
            addObservation(request, response, Integer.parseInt(request.getParameter("studentId")));
        }
    }

    private void showStudentsObservationList(HttpServletRequest request, HttpServletResponse response, int grade, String enrollment) throws ServletException, IOException {
        List<Student> students = new ArrayList<>();
        if (enrollment.equals("")) {
            students = new StudentDAO().selectStudentAllByGrade(grade);
        } else {
            students = new StudentDAO().selectStudentsByEnrollment(Integer.parseInt(enrollment));
        }
        request.setAttribute("students", students);
        request.setAttribute("grade", grade);
        request.getRequestDispatcher("WEB-INF/view/professor/obs-students-list.jsp").forward(request, response);
    }

    private void showStudentObservation(HttpServletRequest request, HttpServletResponse response, int studentId) throws ServletException, IOException {
        Student student = new StudentDAO().selectStudentByEnrollment(studentId);
        List<Observation> observations = new ObservationDAO().selectObservationsForStudent(studentId);
        request.setAttribute("student", student);
        request.setAttribute("observations", observations);
        request.getRequestDispatcher("WEB-INF/view/professor/obs-student.jsp").forward(request,response);
    }

    private void showStudentsBulletinList(HttpServletRequest request, HttpServletResponse response, int grade, String enrollment) throws ServletException, IOException {
        List<StudentDTO> students = new ArrayList<>();
        if (enrollment.equals("")) {
            students = new StudentDAO().selectStudentDTOAllByGrade(grade, ((Professor) request.getSession().getAttribute("professor")).getSubjectName());
        } else {
            students = new StudentDAO().selectStudentDTOByEnrollment(Integer.parseInt(enrollment), ((Professor) request.getSession().getAttribute("professor")).getSubjectName());
        }
        request.setAttribute("students", students);
        request.setAttribute("grade", grade);
        request.getRequestDispatcher("WEB-INF/view/professor/bulletin-students-list.jsp").forward(request, response);
    }

    private void addObservation(HttpServletRequest request, HttpServletResponse response, int studentId) throws ServletException, IOException {
        String observation = request.getParameter("observation");
        boolean insertResult = new ObservationDAO().insertObservation(studentId, observation);
        if (insertResult) {
            response.sendRedirect("obsStudent?studentId=" + studentId);
        }
    }

    private void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        response.sendRedirect("enter");
    }
}
