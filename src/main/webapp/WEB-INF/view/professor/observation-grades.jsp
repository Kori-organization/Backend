<%@ page import="com.example.koribackend.model.entity.Professor" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="pt-BR">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Kori – Observações</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/professor/observation-grades.css">
    <link rel="icon" href="${pageContext.request.contextPath}/assets/logo-top.svg" type="image/png">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/chatbot.css">
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
                <div class="menu-item active" onclick="location.reload()">
                    <img src="${pageContext.request.contextPath}/assets/notes-2.svg">
                    <h2>Observações</h2>
                </div>
                <div class="menu-item active2 three" onclick="window.location='informationProfessor'">
                    <img src="${pageContext.request.contextPath}/assets/info-circle.svg">
                    <h2>Informações</h2>
                </div>
            </div>
            <!-- User profile section -->
            <div class="profile" onclick="window.location='profileProfessor'">
                <img src="${pageContext.request.contextPath}/assets/user.svg" alt="Profile picture" width="38px">
            </div>
        </aside>

        <!-- Main -->
        <main class="main">
            <div class="color"></div>

            <div class="top-main">
                <div class="logo-container" onclick="window.location='homeProfessor'">
                    <img src="${pageContext.request.contextPath}/assets/logo.svg" width="180">
                </div>
                <div class="home-button" onclick="location.reload()">
                    <img src="${pageContext.request.contextPath}/assets/notes-3.svg" alt="Página inicial" width="40">
                </div>
            </div>

            <!-- Cards -->
            <div class="bulletin-wrapper">

                <h1 class="cards-title">Ver & cadastrar observações</h1>

                <div class="cards-grid">

                    <div class="grade-card pink" onclick="window.location='obsStudentsList?grade=1'">
                        <div class="card-icon">
                            <img src="${pageContext.request.contextPath}/assets/observation-pink.svg" alt="Ícone 1º Ano">
                        </div>

                        <div class="card-label">
                            <h2>1º Ano</h2>
                            <p>Clique para ver & cadastrar observações desta turma.</p>
                        </div>
                    </div>

                    <div class="grade-card green" onclick="window.location='obsStudentsList?grade=2'">
                        <div class="card-icon">
                            <img src="${pageContext.request.contextPath}/assets/observation-green.svg" alt="Ícone 2º Ano">
                        </div>

                        <div class="card-label">
                            <h2>2º Ano</h2>
                            <p>Clique para ver & cadastrar observações desta turma.</p>
                        </div>
                    </div>

                    <div class="grade-card blue" onclick="window.location='obsStudentsList?grade=3'">
                        <div class="card-icon">
                            <img src="${pageContext.request.contextPath}/assets/observation-blue.svg" alt="Ícone 3º Ano">
                        </div>

                        <div class="card-label">
                            <h2>3º Ano</h2>
                            <p>Clique para ver & cadastrar observações desta turma.</p>
                        </div>
                    </div>

                    <div class="grade-card yellow" onclick="window.location='obsStudentsList?grade=4'">
                        <div class="card-icon">
                            <img src="${pageContext.request.contextPath}/assets/observation-yellow.svg" alt="Ícone 4º Ano">
                        </div>

                        <div class="card-label">
                            <h2>4º Ano</h2>
                            <p>Clique para ver & cadastrar observações desta turma.</p>
                        </div>
                    </div>

                    <div class="grade-card gray" onclick="window.location='obsStudentsList?grade=5'">
                        <div class="card-icon">
                            <img src="${pageContext.request.contextPath}/assets/observation-gray.svg" alt="Ícone 5º Ano">
                        </div>

                        <div class="card-label">
                            <h2>5º Ano</h2>
                            <p>Clique para ver & cadastrar observações desta turma.</p>
                        </div>
                    </div>

                </div>
            </div>

        </main>
    </div>
    <!-- Floating Chat Button -->
    <div class="chat-toggle" id="chatToggle">
        <img src="${pageContext.request.contextPath}/assets/stars.svg" alt="" width="22px" style="margin-left: -2px">
    </div>

    <div class="chat-overlay" id="chatOverlay"></div>

    <!-- Chat Box -->
    <div class="chat-box" id="chatBox">

        <div class="chat-header">
            <div class="chat-controls">
                <button id="expandChat">⤢</button>
                <button id="closeChat">✕</button>
            </div>
        </div>

        <div class="chat-body">
            <div class="chat-avatar">
                <img src="${pageContext.request.contextPath}/assets/blue-moster1.svg" alt="" width="340px">
            </div>

            <p class="chat-text">
                Olá! Estou aqui para te auxiliar na plataforma Kori
                — <br>como funciona e o que você pode fazer com ela.
            </p>

            <div class="chat-suggestions">
                <button>Quais são os eventos próximos?</button>
                <button>Médias das salas</button>
            </div>
        </div>

        <div class="chat-input">
            <input type="text" placeholder="Digite aqui sua mensagem">
            <button><img src="${pageContext.request.contextPath}/assets/enter.svg" alt="" width="32px" style="margin-top: 5px"></button>
        </div>

    </div>

    <div class="chat-conversation" id="chatConversation">

        <div class="chat-header">
            <div class="chat-controls">
                <button id="expandConversation">⤢</button>
                <button id="closeConversation">✕</button>
            </div>
        </div>

        <div class="conversation-body" id="conversationMessages">

            <div class="message bot">
                <img src="${pageContext.request.contextPath}/assets/blue-monster2.svg">
                <div class="bubble">
                    Olá! Estou aqui para te auxiliar na plataforma Kori — como funciona e o que você pode fazer com ela.
                </div>
            </div>

        </div>

        <div class="chat-input conversation-input">
            <input type="text" placeholder="Digite aqui sua mensagem">
            <button>
                <img src="${pageContext.request.contextPath}/assets/enter.svg" width="32px" style="margin-top: 5px">
            </button>
        </div>

    </div>
</body>
<script>
    window.APP_CONFIG = {
        chatApiUrl: "https://datarep-g7xu.onrender.com/chat/professor",
        professorId: <%=professor.getId()%>
    };
</script>
<script src="${pageContext.request.contextPath}/js/professor/chatbotProfessor.js"></script>
<script src="${pageContext.request.contextPath}/js/professor/observation.js"></script>

</html>