<%@ page import="com.example.koribackend.model.entity.Student" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <!-- Character encoding -->
  <meta charset="utf-8" />

  <!-- Responsive viewport -->
  <meta name="viewport" content="width=device-width,initial-scale=1" />

  <!-- Page title -->
  <title>Alunos do 1º Ano — Observações</title>

  <!-- Favicon -->
  <link rel="icon" href="${pageContext.request.contextPath}/assets/logo-top.svg" type="image/png">

  <!-- Main stylesheet -->
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/student/profile.css">
  <%
    Student student = (Student) session.getAttribute("student");
  %>
</head>
<body>

<!-- Top navigation bar -->
<div class="topbar">
  <!-- Back button -->
  <a class="back" href="${pageContext.request.contextPath}/student/home" title="Voltar">
    <img src="${pageContext.request.contextPath}/assets/left.svg" alt="Voltar" width="15px"> Voltar
  </a>

  <!-- Greeting icon (reloads page on click) -->
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
      <!-- Left column: labels + values -->
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

      <!-- Right column: metadata -->
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
  <!-- Small help link under the card -->
  <p class="profile-help">
    Há algo de errado com suas informações? <a href="mailto:korieducation@gmail.com" class="support-link">Entre em contato com o suporte.</a>
  </p>
</main>

</body>
</html>
