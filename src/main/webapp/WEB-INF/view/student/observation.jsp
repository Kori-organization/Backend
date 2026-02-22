<%@ page import="com.example.koribackend.model.entity.Observations" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Kori – Observações</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/student/observation.css">
    <link rel="icon" href="${pageContext.request.contextPath}/assets/logo-top.svg" type="image/png">
    <%
            List<Observations> observations = (List<Observations>) request.getAttribute("observations");
            int counter = 1;
    %>
</head>

<body>
    <!-- Main app container -->
    <div class="app">
        <!-- Sidebar navigation -->
        <aside class="sidebar">
            <div class="sidebar-menu">
                <div class="menu-item active2 one" onclick="window.location='homeStudent'">
                    <img src="${pageContext.request.contextPath}/assets/home-3.svg">
                    <h2>Início</h2>
                </div>
                <div class="menu-item active2 two">
                    <img src="${pageContext.request.contextPath}/assets/greeting.svg" onclick="window.location='reportCardStudent'">
                    <h2>Boletim</h2>
                </div>
                <div class="menu-item active" onclick="location.reload()">
                    <img src="${pageContext.request.contextPath}/assets/notes-2.svg">
                    <h2>Observações</h2>
                </div>
                <div class="menu-item active2 three">
                    <img src="${pageContext.request.contextPath}/assets/info-circle.svg" onclick="window.location='informationsStudent'">
                    <h2>Informações</h2>
                </div>
            </div>
            <!-- User profile section -->
            <div class="profile" onclick="window.location='profileStudent'">
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
                    <img src="${pageContext.request.contextPath}/assets/notes-3.svg" alt="Página inicial" width="40">
                </div>
            </div>

            <!-- Observation header -->
            <div class="obs-header">
                <h1>Observações</h1>
                <p>Observações são oportunidades de melhorar. Não fique chateado — use isso para <br>crescer.</p>
            </div>

            <!-- Observation container -->
            <div class="obs-container" id="obsContainer">
                <% for (Observations observation : observations) { %>
                     <% if (counter == 1) { %>
                          <div class="obs-card pink">
                        <% counter++; } else if (counter == 2) { %>
                           <div class="obs-card yellow">
                        <% counter++; } else if (counter == 3) { %>
                            <div class="obs-card blue">
                        <% counter++; } else { %>
                            <div class="obs-card green">
                        <% counter = 1; } %>

                               <p><%= observation.getObservation() %></p>

                               <button class="copy-btn">
                                   <img src="${pageContext.request.contextPath}/assets/copy.svg" alt="Copiar">
                               </button>
                          </div>
                <% } %>
            </div>
        </main>
    </div>
    <div id="copyToast" class="copy-toast">
        Copiado para a área de transferência
    </div>
</body>

<script src="${pageContext.request.contextPath}/js/student/observation.js"></script>

</html>