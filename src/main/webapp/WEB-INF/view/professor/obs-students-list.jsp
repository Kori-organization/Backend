<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.koribackend.model.entity.Student" %>
<%@ page import="java.util.List" %>
<%
        List<Student> students = (List<Student>) request.getAttribute("students");
        int grade = (Integer) request.getAttribute("grade");
%>

<!doctype html>
<html lang="pt-BR">

<head>
    <!-- Character encoding -->
    <meta charset="utf-8" />

    <!-- Responsive viewport -->
    <meta name="viewport" content="width=device-width,initial-scale=1" />

    <!-- Page title -->
    <title>Alunos do <%=grade%>º Ano — Observações</title>

    <!-- Favicon -->
    <link rel="icon" href="${pageContext.request.contextPath}/assets/logo-top.svg" type="image/png">

    <!-- Main stylesheet -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/professor/obs-students-list.css">


</head>

<body>

    <!-- Top navigation bar -->
    <div class="topbar">
        <!-- Back button -->
        <a class="back" href="observationGrades" title="Voltar">
            <img src="${pageContext.request.contextPath}/assets/left.svg" alt="Voltar" width="15px"> Voltar
        </a>

        <!-- Greeting icon (reloads page on click) -->
        <img src="${pageContext.request.contextPath}/assets/notes-3.svg" alt="" width="40px" class="img-greeting" onclick="location.reload()">
    </div>

    <main>
        <!-- Page title -->
        <h1>Alunos do <%=grade%>º Ano</h1>

        <form action="obsStudentsList">
            <div class="search-box">
                <input type="hidden" name="grade" value="<%=grade%>">
                <input type="text" placeholder="Pesquisar Aluno..." aria-label="Pesquisar Aluno" name="studentId">
                <button class="search-btn" title="Pesquisar" aria-label="Pesquisar" type="submit">
                    <img src="${pageContext.request.contextPath}/assets/icon-search.svg" width="17" alt="Pesquisar">
                </button>
            </div>
        </form>
        <!-- Search box -->
        

        <!-- Students table section -->
        <section class="table" aria-label="Lista de alunos">

            <!-- Table header -->
            <div class="header" role="row">
                <div>Matrícula <img src="${pageContext.request.contextPath}/assets/chevron.svg" alt="" width="10"></div>
                <div>Nome <img src="${pageContext.request.contextPath}/assets/chevron.svg" alt="" width="10"></div>
                <div>Email <img src="${pageContext.request.contextPath}/assets/chevron.svg" alt="" width="10"></div>
                <div>Admissão <img src="${pageContext.request.contextPath}/assets/chevron.svg" alt="" width="10"></div>
            </div>

            <!-- Student row -->
            <%
               for (Student student: students) {%>

            <div class="student-row" role="row" onclick="window.location='obsStudent?studentId=<%= student.getEnrollment() %>'">
                <div class="matricula"><%=student.getEnrollment()%></div>
                <div class="nome"><%=student.getName()%></div>
                <div class="email"><%=student.getEmail()%></div>
                <div class="admissao"><%=student.getIssueDate()%></div>

                <!-- Action buttons -->
                <div class="actions">
                    <button class="btn-observacoes">
                        <img src="${pageContext.request.contextPath}/assets/notes-5.svg" width="18" alt="">
                        Observações
                    </button>
                </div>
            </div>
            <% } %>

            <!-- Spacer at bottom -->
            <div style="height:26px"></div>
        </section>

    </main>

</body>

</html>