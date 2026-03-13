<%@ page import="com.example.koribackend.model.entity.Student" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Kori – Informações</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/student/information.css">
    <link rel="icon" href="${pageContext.request.contextPath}/assets/logo-top.svg" type="image/png">

    <%
        Student student = (Student) session.getAttribute("student");
        if (student == null) {
            response.sendRedirect("enter");
            return;
        }
    %>
</head>

<body>
<div class="app">

    <!-- Sidebar -->
    <aside class="sidebar">
        <div class="sidebar-menu">

            <div class="menu-item active2 one" onclick="window.location='homeStudent'">
                <img src="${pageContext.request.contextPath}/assets/home-3.svg">
                <h2>Início</h2>
            </div>

            <div class="menu-item active2 two" onclick="window.location='reportCardStudent'">
                <img src="${pageContext.request.contextPath}/assets/greeting.svg">
                <h2>Boletim</h2>
            </div>

            <div class="menu-item active2 three" onclick="window.location='observationsStudent'">
                <img src="${pageContext.request.contextPath}/assets/notes.svg">
                <h2>Observações</h2>
            </div>

            <div class="menu-item active" onclick="location.reload()">
                <img src="${pageContext.request.contextPath}/assets/info-circle-2.svg">
                <h2>Informações</h2>
            </div>

        </div>

        <!-- Profile -->
        <div class="profile" onclick="window.location='profileStudent'">
            <img src="${pageContext.request.contextPath}/assets/user.svg" width="38">
        </div>
    </aside>


    <!-- Main content -->
    <main class="main">

        <section class="hero">

            <div class="container">

                <div class="left">
                    <img src="${pageContext.request.contextPath}/assets/text-information.svg" class="title-img">

                    <button class="btn" onclick="window.location='homeStudent'">
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