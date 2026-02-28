<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.koribackend.model.entity.Student" %>
<%@ page import="java.util.List" %>
<%
        List<Student> students = (List<Student>) request.getAttribute("students");
%>

<!doctype html>
<html lang="pt-BR">

<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width,initial-scale=1" />
    <title>Alunos do ${sessionScope.grade}º Ano — Observações</title>
    <link rel="icon" href="${pageContext.request.contextPath}/assets/logo-top.svg" type="image/png">
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
        <h1>Alunos do ${sessionScope.grade}º Ano</h1>

        <!-- Search box -->
        <div class="search-container">

            <form action="obsStudentsList" method="get">
                <div class="search-box">

                    <input type="text"
                           name="filter"
                           value="${param.filter}"
                           placeholder="Pesquisar Aluno..."
                           aria-label="Pesquisar Aluno">

                    <button type="button" class="btn-limpar"
                            onclick="window.location='obsStudentsList?grade=${sessionScope.grade}'">
                        Limpar
                    </button>

                    <button class="search-btn" title="Pesquisar" type="submit">
                        <img src="${pageContext.request.contextPath}/assets/icon-search.svg"
                             width="17"
                             alt="Pesquisar">
                    </button>
                </div>
            </form>
        </div>

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

            <div class="student-row" role="row">
                <div class="matricula"><%=student.getEnrollment()%></div>
                <div class="nome"><%=student.getName()%></div>
                <div class="email"><%=student.getEmail()%></div>
                <div class="admissao"><%=student.getIssueDate()%></div>

                <!-- Action buttons -->
                <div class="actions" onclick="window.location='obsStudent?enrollment=<%=student.getEnrollment()%>'">
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