package com.example.koribackend.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

// Map the servlet to the specific download endpoint
@WebServlet(urlPatterns = {"/dowloadRegulation"})
public class DowloadRegulationController extends HttpServlet {

    // Process GET requests and route to the download logic
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        if (path.equals("/dowloadRegulation")) {
            downloadRegulation(request,response);
        }
    }

    // Handle the file retrieval and streaming process
    private void downloadRegulation(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Retrieve the redirect point in case the file is missing
        String endPoint = request.getParameter("point");

        // Load the document file as a stream from the protected WEB-INF directory
        InputStream is = getServletContext()
                .getResourceAsStream("/WEB-INF/data/RegulamentoEscolar-Kori.docx");

        // If the file is not found, redirect the user back to the source page
        if (is == null) {
            response.sendRedirect(endPoint);
            return;
        }

        // Set the MIME type specifically for Microsoft Word .docx files
        response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");

        // Set the header to force the browser to trigger a file download with a specific name
        response.setHeader("Content-Disposition", "attachment; filename=RegulamentoEscolar-Kori.docx");

        // Open the output stream to send file data to the client
        OutputStream os = response.getOutputStream();

        // Create a buffer to read and write data in chunks for better performance
        byte[] buffer = new byte[1024];
        int bytesRead;

        // Read from the input stream and write to the output stream until the end of the file
        while ((bytesRead = is.read(buffer)) != -1) {
            os.write(buffer, 0, bytesRead);
        }

        // Close both streams to release system resources
        is.close();
        os.close();
    }
}