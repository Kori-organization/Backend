package com.example.koribackend.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@WebServlet(urlPatterns = {"/dowloadRegulation"})
public class DowloadRegulationController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        if (path.equals("/dowloadRegulation")) {
            downloadRegulation(request,response);
        }
    }

    private void downloadRegulation(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String endPoint = request.getParameter("point");

        InputStream is = getServletContext()
                .getResourceAsStream("/WEB-INF/data/RegulamentoEscolar-Kori.docx");

        if (is == null) {
            response.sendRedirect(endPoint);
            return;
        }

        // Word Type
        response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");

        // Force download
        response.setHeader("Content-Disposition", "attachment; filename=RegulamentoEscolar-Kori.docx");

        OutputStream os = response.getOutputStream();

        byte[] buffer = new byte[1024];
        int bytesRead;

        while ((bytesRead = is.read(buffer)) != -1) {
            os.write(buffer, 0, bytesRead);
        }

        is.close();
        os.close();
    }
}
