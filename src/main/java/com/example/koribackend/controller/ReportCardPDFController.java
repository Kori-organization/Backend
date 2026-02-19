package com.example.koribackend.controller;

import com.example.koribackend.model.dao.ReportCardDAO;
import com.example.koribackend.model.entity.Grade;
import com.example.koribackend.model.entity.ReportCard;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@WebServlet(urlPatterns = {"/createReportCardPDF"})
public class ReportCardPDFController extends HttpServlet {

    private final DeviceRgb red = new DeviceRgb(255, 59, 48);
    private final DeviceRgb yellow = new DeviceRgb(212, 160, 23);
    private final DeviceRgb blue = new DeviceRgb(45, 108, 223);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        if (path.equals("/createReportCardPDF")) {
            createReportCard(request, response);
        }
    }

    public void createReportCard(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int enrollment = (int) request.getAttribute("enrollment");
        ReportCard reportCard = new ReportCardDAO().selectReportCard(enrollment);

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=boletim.pdf");
        PdfWriter writer = new PdfWriter(response.getOutputStream());
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        Table header = new Table(new float[]{1, 1, 1});
        header.setWidth(UnitValue.createPercentValue(100));

        try {
            String caminho = getServletContext().getRealPath("/assets/logo.png");
            Image logo = new Image(ImageDataFactory.create(caminho));
            logo.setWidth(80);
            header.addCell(new Cell().add(logo).setBorder(Border.NO_BORDER));
        } catch (Exception e) {
            header.addCell(new Cell().add(new Paragraph("LOGO")).setBorder(Border.NO_BORDER));
        }

        header.addCell(new Cell().add(new Paragraph("BOLETIM ESCOLAR").setBold().setFontSize(18)
                        .setTextAlignment(TextAlignment.LEFT)).setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setBorder(Border.NO_BORDER));

        header.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));

        document.add(header);

        document.add(new Paragraph("\n"));

        Table box = new Table(new float[]{50, 50});
        box.setWidth(UnitValue.createPercentValue(100));
        box.setBorder(Border.NO_BORDER);

        LocalDate date = LocalDate.now();
        box.addCell(new Cell().add(new Paragraph("ALUNO(A): " + reportCard.getStudentName())).setBorder(Border.NO_BORDER));
        box.addCell(new Cell().add(new Paragraph("ANO LETIVO: " + date.getYear())).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT));
        box.addCell(new Cell().add(new Paragraph("SÉRIE: 1º")).setBorder(Border.NO_BORDER));
        box.addCell(new Cell().add(new Paragraph("SITUAÇÃO: " + reportCard.getFinalSituation())).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER));
        box.addCell(new Cell().add(new Paragraph("UNIDADE: Kori")).setBorder(Border.NO_BORDER));
        box.addCell(new Cell().add(new Paragraph("EMISSÃO: " + date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT));

        Table container = new Table(1);
        container.setWidth(UnitValue.createPercentValue(100));
        container.setBorder(Border.NO_BORDER);

        Cell wrapper = new Cell().add(box);
        container.addCell(wrapper);
        document.add(container);
        document.add(new Paragraph("\n"));

        // ===== TABLE =====
        float[] colunas = {200, 60, 60, 60, 60, 60, 100};
        Table tabela = new Table(colunas);
        tabela.setWidth(UnitValue.createPercentValue(100));

        // ===== HEADER =====
        tabela.addHeaderCell(new Cell().add(new Paragraph("Disciplinas").setBold()).setTextAlignment(TextAlignment.LEFT));
        tabela.addHeaderCell(new Cell().add(new Paragraph("N1").setBold())).setTextAlignment(TextAlignment.CENTER);
        tabela.addHeaderCell(new Cell().add(new Paragraph("N2").setBold())).setTextAlignment(TextAlignment.CENTER);
        tabela.addHeaderCell(new Cell().add(new Paragraph("ME").setBold())).setTextAlignment(TextAlignment.CENTER);
        tabela.addHeaderCell(new Cell().add(new Paragraph("RF").setBold())).setTextAlignment(TextAlignment.CENTER);
        tabela.addHeaderCell(new Cell().add(new Paragraph("MF").setBold())).setTextAlignment(TextAlignment.CENTER);
        tabela.addHeaderCell(new Cell().add(new Paragraph("Situação").setBold())).setTextAlignment(TextAlignment.CENTER);

        // INFORMATION OF STUDENT
        for (Grade nota : reportCard.getGrader()) {
            addLine(tabela, nota.getSubject(), nota.getGrade1(), nota.getGrade2(), nota.getRec());
        }

        document.add(tabela);

        document.add(new Paragraph("\n"));

        // ===== BASEBOARD =====
        document.add(new Paragraph("MA - Média Anual"));
        document.add(new Paragraph("RF - Recuperação Final"));
        document.add(new Paragraph("MF - Média Final"));

        document.close();
    }

    private void addLine(Table tabela, String disciplina, double n1, double n2, double rec) {

        double average = -1;
        double avarageFinal = -1;

        String situation;
        if (n1 != -1 && n2 != -1) {
            average = (n1 + n2) / 2;
            if (average >= 7) {
                situation = "Aprovado";
            } else if (average >= 5) {
                situation = "Recuperação";
                if (rec != -1) {
                    avarageFinal = (average + rec) / 2;
                    if (avarageFinal >= 7) {
                        situation = "Aprovado";
                    } else {
                        situation = "Reprovado";
                    }
                }
            } else {
                situation = "Reprovado";
            }
        } else {
            situation = "-";
        }

        // Subject
        tabela.addCell(new Cell().add(new Paragraph(disciplina).setTextAlignment(TextAlignment.LEFT)));

        // N1
        Cell n1Cell = new Cell().add(new Paragraph((n1 != -1) ? String.format("%.2f", n1) : "-"))
                .setTextAlignment(TextAlignment.CENTER);
        if (n1 < 7 && n1 != -1) {
            n1Cell.setFontColor(red);
        }
        tabela.addCell(n1Cell);

        // N2
        Cell n2Cell = new Cell().add(new Paragraph((n2 != -1) ? String.format("%.2f", n2) : "-"))
                .setTextAlignment(TextAlignment.CENTER);
        if (n2 < 7 && n2 != -1) {
            n2Cell.setFontColor(red);
        }
        tabela.addCell(n2Cell);

        // Annual average
        Cell mediaCell = new Cell().add(new Paragraph((average != -1) ? String.format("%.2f", average) : "-"))
                .setTextAlignment(TextAlignment.CENTER);
        if (average < 7 && average != -1) {
            mediaCell.setFontColor(red);
        }
        tabela.addCell(mediaCell);

        // Recovery
        Cell recCell = new Cell().add(new Paragraph((rec != -1) ? String.format("%.2f", rec) : "-")).setTextAlignment(TextAlignment.CENTER);
        if (rec < 7 && rec != -1) {
            recCell.setFontColor(red);
        }
        tabela.addCell(recCell);

        // Final average
        Cell cellFinal = new Cell().add(new Paragraph((avarageFinal != -1) ? String.format("%.2f", avarageFinal) : "-"))
                .setTextAlignment(TextAlignment.CENTER);
        if (avarageFinal < 7 && avarageFinal != -1) {
            cellFinal.setFontColor(red);
        } else if (avarageFinal != -1) {
            cellFinal.setFontColor(blue);
        }
        tabela.addCell(cellFinal);

        // Situation
        Cell situationCell = new Cell().add(new Paragraph(situation))
                .setTextAlignment(TextAlignment.CENTER);
        if (situation.equals("Reprovado")) {
            situationCell.setFontColor(red);
        } else if (situation.equals("Recuperação")) {
            situationCell.setFontColor(yellow);
        }
        tabela.addCell(situationCell);
    }
}