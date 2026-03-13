<%@ page import="com.example.koribackend.model.entity.Professor" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="pt-BR">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Kori – Informações</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/professor/information.css">
    <link rel="icon" href="${pageContext.request.contextPath}/assets/logo-top.svg" type="image/png">

    <%
        Professor professor = (Professor) session.getAttribute("professor");
        if (professor == null) {
            response.sendRedirect("enter");
            return;
        }
    %>
</head>

<body>
<!-- Main app container -->
<div class="app">

    <!-- Sidebar navigation -->
    <aside class="sidebar">
        <div class="sidebar-menu">

            <div class="menu-item active2 one" onclick="window.location='homeProfessor'">
                <img src="${pageContext.request.contextPath}/assets/home-3.svg">
                <h2>Início</h2>
            </div>

            <div class="menu-item active2 two" onclick="window.location='reportCardGrades'">
                <img src="${pageContext.request.contextPath}/assets/greeting.svg">
                <h2>Boletim</h2>
            </div>

            <div class="menu-item active2 three" onclick="window.location='observationGrades'">
                <img src="${pageContext.request.contextPath}/assets/notes.svg">
                <h2>Observações</h2>
            </div>

            <div class="menu-item active" onclick="location.reload()">
                <img src="${pageContext.request.contextPath}/assets/info-circle-2.svg">
                <h2>Informações</h2>
            </div>

        </div>

        <!-- User profile section -->
        <div class="profile" onclick="window.location='profileProfessor'">
            <img src="${pageContext.request.contextPath}/assets/user.svg" width="38">
        </div>
    </aside>


    <!-- Main content area -->
    <main class="main">

        <section class="hero">

            <div class="container">

                <div class="left">
                    <img src="${pageContext.request.contextPath}/assets/text-information.svg" class="title-img">

                    <button class="btn" onclick="window.location='homeProfessor'">
                        Ir para início
                    </button>
                </div>

                <div class="right">

                    <img src="${pageContext.request.contextPath}/assets/children.png" class="child">

                    <div class="card card1">
                        Nossa prioridade é a educação infantil de qualidade.
                    </div>

                    <div class="card card2">
                        Empatia e respeito com o futuro do país
                    </div>

                </div>

            </div>

        </section>

    </main>

</div>
</body>

</html>