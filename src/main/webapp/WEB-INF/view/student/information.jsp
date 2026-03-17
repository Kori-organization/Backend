<%@ page import="com.example.koribackend.model.entity.Student" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Kori – Informações</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/student/information.css">
    <link rel="icon" href="${pageContext.request.contextPath}/assets/logo-top.svg" type="image/png">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/chatbot.css">

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
</html>