<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.koribackend.model.entity.Professor" %>

<%
    Professor professor = (Professor) session.getAttribute("professor");
%>

<!doctype html>
<html lang="pt-BR">

<head>
    <!-- Character encoding -->
    <meta charset="utf-8" />

    <!-- Responsive viewport -->
    <meta name="viewport" content="width=device-width,initial-scale=1" />

    <!-- Page title -->
    <title>Perfil</title>

    <!-- Favicon -->
    <link rel="icon" href="${pageContext.request.contextPath}/assets/logo-top.svg" type="image/png">

    <!-- Main stylesheet -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/professor/profile.css">
</head>

<body>

    <!-- Top navigation bar -->
    <div class="topbar">
        <!-- Back button -->
        <a class="back" href="homeProfessor" title="Voltar">
            <img src="${pageContext.request.contextPath}/assets/left.svg" alt="Voltar" width="15px"> Voltar
        </a>

        <!-- Greeting icon (reloads page on click) -->
        <img src="${pageContext.request.contextPath}/assets/user-2.svg" alt="" width="40px" class="img-greeting" onclick="location.reload()">
    </div>

    <main>
        <!-- Page title -->
        <h1>Seu perfil</h1>

        <!-- Profile card -->
        <section class="profile-card" aria-label="Teacher profile">

            <!-- Top colored hero -->
            <div class="profile-hero">
                <!-- Avatar icon -->
                <div class="avatar-icon" aria-hidden="true">
                    <img src="${pageContext.request.contextPath}/assets/user-2.svg" alt="Avatar">
                </div>

                <!-- Decorative diagonal highlight -->
                <div class="hero-sheen" aria-hidden="true"></div>
            </div>

            <!-- Profile content -->
            <div class="profile-body">
                <!-- Left column -->
                <div class="profile-col profile-left">

                    <div class="field">
                        <div class="label">Nome:</div>
                        <div class="value name"><%=professor.getName()%></div>
                    </div>

                    <div class="field">
                        <div class="label">Disciplina:</div>
                        <div class="value"><%=professor.getSubjectName()%></div>
                    </div>

                    <div class="field">
                        <div class="label">Usuário:</div>
                        <div class="value"><%=professor.getUsername()%></div>
                    </div>

                </div>
            </div>
        </section>

        <!-- Logout -->
        <button class="logout-btn" id="openLogout">
            → Sair da conta
        </button>

        <!-- Help text -->
        <p class="profile-help">
            Há algo de errado com suas informações?
            <a href="mailto:korieducation@gmail.com" class="support-link">
                Entre em contato com o suporte.
            </a>
        </p>

        <!-- Overlay + Popup -->
            <div class="overlay" id="logoutOverlay">
                <div class="logout-modal">
                    <div class="logout-icon"><img src="${pageContext.request.contextPath}/assets/info-circle-4.svg" alt="" width="40px"></div>
                    <h2>Tem certeza que deseja<br> sair da conta?</h2>
    
                    <div class="logout-actions">
                        <button class="btn-cancel" id="cancelLogout">Voltar</button>
                        <a href="logoutProfessor"><button class="btn-danger" id="confirmLogout" type="submit">Sair</button></a>
                    </div>
                </div>
            </div>
    </main>

</body>
<script src="${pageContext.request.contextPath}/js/professor/profile.js"></script>
</html>