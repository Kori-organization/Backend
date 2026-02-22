<%@ page import="com.example.koribackend.model.entity.ReportCard" %>
<%@ page import="com.example.koribackend.model.entity.Grade" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Kori – Boletim</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/student/bulletin.css">
    <link rel="icon" href="${pageContext.request.contextPath}/assets/logo-top.svg" type="image/png">
    <%
        ReportCard reportCard = (ReportCard) request.getAttribute("bulletin");
        int counter = reportCard.getGrader().size();
    %>
</head>
<body>
<!-- Main app container -->
<div class="app">
    <!-- Sidebar navigation -->
    <aside class="sidebar">
        <div class="sidebar-menu">
            <div class="menu-item active2 one" onclick="window.location='${pageContext.request.contextPath}/student/home'">
                <img src="${pageContext.request.contextPath}/assets/home-3.svg">
                <h2>Início</h2>
            </div>
            <div class="menu-item active" onclick="window.location.reload()">
                <img src="${pageContext.request.contextPath}/assets/greeting-2.svg">
                <h2>Boletim</h2>
            </div>
            <div class="menu-item active2 two" onclick="window.location='${pageContext.request.contextPath}/student/observations'">
                <img src="${pageContext.request.contextPath}/assets/notes.svg">
                <h2>Observações</h2>
            </div>
            <div class="menu-item active2 three" onclick="window.location='${pageContext.request.contextPath}/student/information'">
                <img src="${pageContext.request.contextPath}/assets/info-circle.svg">
                <h2>Informações</h2>
            </div>
        </div>
        <!-- User profile section -->
        <div class="profile" onclick="window.location='${pageContext.request.contextPath}/student/profile'">
            <img src="${pageContext.request.contextPath}/assets/user.svg" width="38">
        </div>
    </aside>

    <!-- Main content area -->
    <main class="main">
        <!-- Top color overlay for visual effect -->
        <div class="color"></div>

        <!-- Top header with logo and home button -->
        <div class="top-main">
            <div class="logo-container" onclick="location.reload()">
                <img src="${pageContext.request.contextPath}/assets/logo.svg" alt="Kori Logo" width="180">
            </div>
            <div class="home-button" onclick="location.reload()">
                <img src="${pageContext.request.contextPath}/assets/greeting-3.svg" alt="Página inicial" width="50">
            </div>
        </div>

        <!-- Bulletin section -->
        <div class="bulletin-wrapper">
            <div class="bulletin-title">
                <h1>Boletim escolar</h1>
            </div>

            <!-- Final status card -->
            <div class="final-status">
                <img src="${pageContext.request.contextPath}/assets/download.svg" class="download-icon" alt="Download" onclick="window.location='${pageContext.request.contextPath}/student/createPDF'">
                Situação final: <%=reportCard.getFinalSituation()%>.
            </div>

            <!-- Report card grid -->
            <div class="bulletin">
                <!-- Subjects column -->
                <div class="bcol left">
                    <h3>Disciplina</h3>
                    <% for (Grade grade : reportCard.getGrader()) { %>
                        <% counter--; %>
                        <div class="bitem subject <%=counter == reportCard.getGrader().size() - 1 ? "top" : ""%> <%=counter == 0 ? "bottom" : ""%>"><%=grade.getSubject()%></div>
                    <% } %>
                    <% counter = reportCard.getGrader().size(); %>
                </div>

                <!-- First semester grades -->
                <div class="bcol left2">
                    <h3><span class="badge yellow">1º Semestre</span>Média</h3>
                    <% for (Grade grade : reportCard.getGrader()) { %>
                        <% counter--; %>
                        <div class="bitem <%=counter == reportCard.getGrader().size() - 1 ? "top" : ""%> <%=counter == 0 ? "bottom" : ""%> <%=(grade.getGrade1() != -1 && grade.getGrade1() < 7) ? " red" : ""%>"><%=(grade.getGrade1() == -1) ? "-" : String.format("%.2f",grade.getGrade1())%></div>
                    <% } %>
                    <% counter = reportCard.getGrader().size(); %>
                </div>

                <!-- Second semester grades -->
                <div class="bcol left3">
                    <h3><span class="badge yellow">2º Semestre</span>Média</h3>
                    <% for (Grade grade : reportCard.getGrader()) { %>
                        <% counter--; %>
                        <div class="bitem <%=counter == reportCard.getGrader().size() - 1 ? "top" : ""%> <%=counter == 0 ? "bottom" : ""%> <%=(grade.getGrade2() != -1 && grade.getGrade2() < 7) ? " red" : ""%>"><%=(grade.getGrade2() == -1) ? "-" : String.format("%.2f",grade.getGrade2())%></div>
                    <% } %>
                    <% counter = reportCard.getGrader().size(); %>
                </div>

                <!-- Recovery grades -->
                <div class="bcol right3">
                    <h3><span class="badge pink">Recuperação</span>Média</h3>
                    <% for (Grade grade : reportCard.getGrader()) { %>
                        <% counter--; %>
                        <div class="bitem <%=counter == reportCard.getGrader().size() - 1 ? "top" : ""%> <%=counter == 0 ? "bottom" : ""%><%=(grade.getRec() != -1 && grade.getRec() < 7) ? " red" : ""%>"><%=(grade.getRec() == -1) ? "-" : String.format("%.2f",grade.getRec())%></div>
                    <% } %>
                    <% counter = reportCard.getGrader().size(); %>
                </div>

                <!-- Final averages -->
                <div class="bcol right2">
                    <h3>Média Final</h3>
                    <% for (Grade grade : reportCard.getGrader()) { %>
                        <% counter--; %>
                        <%
                            double average;
                            if (grade.getGrade1() != -1 && grade.getGrade2() != -1) {
                                average = (grade.getGrade1() + grade.getGrade2()) / 2;
                                if (average < 7 && grade.getRec() != -1) {
                                    average = (average + grade.getRec()) / 2;
                                }
                            } else {
                                average = -1;
                            }
                        %>
                        <div class="bitem <%=counter == reportCard.getGrader().size() - 1 ? "top" : ""%> <%=counter == 0 ? "bottom" : ""%> <%=(average != -1 && average < 7) ? " red" : ""%>"><%=(average == -1) ? "-" : String.format("%.2f",average)%></div>
                    <% } %>
                    <% counter = reportCard.getGrader().size(); %>
                </div>

                <!-- Final status per subject -->
                <div class="bcol right">
                    <h3>Situação</h3>
                    <% for (Grade grade : reportCard.getGrader()) { %>
                    <% counter--; %>
                    <%
                        double average;
                        boolean isRec = false;
                        if (grade.getGrade1() != -1 && grade.getGrade2() != -1) {
                            average = (grade.getGrade1() + grade.getGrade2()) / 2;
                            if (average < 7 && grade.getRec() != -1) {
                                average = (average + grade.getRec()) / 2;
                            } else {
                                isRec = true;
                            }
                        } else {
                            average = -1;
                        }
                    %>
                    <div class="bitem <%=counter == reportCard.getGrader().size() - 1 ? "top" : ""%> <%=counter == 0 ? "bottom" : ""%> <%=(average != -1 && average <= 5) ? "red" : (average >= 7 ? "green" : (average != -1 ? "yellow" : "")) %>"><%=(average == -1) ? "-" : (average < 7 && average >= 5 && isRec ? "Recuperação" : (average >= 7 ? "Aprovado" : "Reprovado"))%></div>
                    <% } %>
                </div>
            </div>
        </div>
    </main>
</div>
</body>
</html>
