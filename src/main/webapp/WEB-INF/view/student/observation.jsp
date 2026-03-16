<%@ page import="com.example.koribackend.model.entity.Observation" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.koribackend.model.entity.Student" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Kori – Observações</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/student/observation.css">
    <link rel="icon" href="${pageContext.request.contextPath}/assets/logo-top.svg" type="image/png">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/chatbot.css">
    <%
        Student student = (Student) session.getAttribute("student");
        if (student == null) {
            response.sendRedirect("enter");
            return;
        }

        List<Observation> observations = (List<Observation>) request.getAttribute("observations");
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
                <div class="menu-item active2 two" onclick="window.location='reportCardStudent'">
                    <img src="${pageContext.request.contextPath}/assets/greeting.svg">
                    <h2>Boletim</h2>
                </div>
                <div class="menu-item active" onclick="location.reload()">
                    <img src="${pageContext.request.contextPath}/assets/notes-2.svg">
                    <h2>Observações</h2>
                </div>
                <div class="menu-item active2 three" onclick="window.location='informationsStudent'">
                    <img src="${pageContext.request.contextPath}/assets/info-circle.svg">
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
                <div class="logo-container" onclick="window.location='homeStudent'">
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
                <% for (Observation observation : observations) { %>
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
                <button>Eu vou passar de ano?</button>
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
    const contextPath = "${pageContext.request.contextPath}";
    window.APP_CONFIG = {
        chatApiUrl: "https://datarep-g7xu.onrender.com/chat/student",
        studentEnrollment: <%=student.getEnrollment()%>
    };
</script>
<script src="${pageContext.request.contextPath}/js/student/chatbotStudent.js"></script>
<script src="${pageContext.request.contextPath}/js/student/observation.js"></script>
</html>