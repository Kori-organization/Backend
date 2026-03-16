<%@ page import="com.example.koribackend.model.entity.Student" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <%
    Student student = (Student) session.getAttribute("student");
    if (student == null) {
      response.sendRedirect("enter");
      return;
    }
  %>
  <meta charset="utf-8" />
  <meta name="viewport" content="width=device-width,initial-scale=1" />
  <title>Kori - Perfil</title>
  <link rel="icon" href="${pageContext.request.contextPath}/assets/logo-top.svg" type="image/png">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/student/profile.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/chatbot.css">
</head>
<body>

<!-- Top navigation bar -->
<div class="topbar">
  <!-- Back button -->
  <a class="back" href="homeStudent" title="Voltar">
    <img src="${pageContext.request.contextPath}/assets/left.svg" alt="Voltar" width="15px"> Voltar
  </a>

  <!-- Greeting icon -->
  <img src="${pageContext.request.contextPath}/assets/user-2.svg" alt="" width="40px" class="img-greeting" onclick="location.reload()">
</div>

<main>
  <!-- Page title -->
  <h1>Seu perfil</h1>

  <!-- Profile card (visual structure follows the provided mockup) -->
  <section class="profile-card" aria-label="Student profile">
    <!-- Hero band with rounded top and decorative diagonal highlight -->
    <div class="profile-hero">
      <!-- Avatar icon placed on the left of the hero -->
      <div class="avatar-icon" aria-hidden="true">
        <img src="${pageContext.request.contextPath}/assets/user-2.svg" alt="Avatar">
      </div>

      <!-- decorative diagonal highlight (purely visual) -->
      <div class="hero-sheen" aria-hidden="true"></div>
    </div>

    <!-- Main content area with student details -->
    <div class="profile-body">
      <!-- Left column -->
      <div class="profile-col profile-left">
        <div class="field">
          <div class="label">Nome:</div>
          <div class="value name"><%=student.getName()%></div>
        </div>

        <div class="field">
          <div class="label">Série:</div>
          <div class="value"><%=student.getCompleteSerie()%></div>
        </div>

        <div class="field">
          <div class="label">Email:</div>
          <div class="value email"><%=student.getEmail()%></div>
        </div>
      </div>

      <!-- Right column -->
      <div class="profile-col profile-right">
        <div class="meta-row">
          <div class="meta-label">Matrícula:</div>
          <div class="meta-value"><%=student.getEnrollment()%></div>
        </div>

        <div class="meta-row">
          <div class="meta-label">Data de Admissão:</div>
          <div class="meta-value"><%=student.getFormatDate()%></div>
        </div>
      </div>
    </div>
  </section>

  <!-- Logout -->
  <button class="logout-btn" id="openLogout">
    <img src="${pageContext.request.contextPath}/assets/icon-logout.svg" alt="" width="14px" style="margin-bottom: -2px;"> Sair da conta
  </button>

  <!-- Small help link under the card -->
  <p class="profile-help">
    Há algo de errado com suas informações? <a href="mailto:korieducation@gmail.com" class="support-link">Entre em contato com o suporte.</a>
  </p>

  <!-- Overlay + Popup -->
  <div class="overlay" id="logoutOverlay">
    <div class="logout-modal">
      <div class="logout-icon"><img src="${pageContext.request.contextPath}/assets/info-circle-4.svg" alt="" width="40px"></div>
      <h2>Tem certeza que deseja<br> sair da conta?</h2>

      <div class="logout-actions">
        <button class="btn-cancel" id="cancelLogout">Voltar</button>
        <button class="btn-danger" onclick="window.location='logoutStudent'">Sair</button>
      </div>
    </div>
  </div>
</main>

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
<script src="${pageContext.request.contextPath}/js/student/profile.js"></script>
<script src="${pageContext.request.contextPath}/js/student/chatbotStudent.js"></script>
</html>
