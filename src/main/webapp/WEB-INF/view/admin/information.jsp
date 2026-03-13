<%@ page import="com.example.koribackend.model.entity.Administrator" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="pt-BR">

<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Kori – Informações</title>

  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin/information.css">
  <link rel="icon" href="${pageContext.request.contextPath}/assets/logo-top.svg" type="image/png">

  <%
    Administrator admin = (Administrator) session.getAttribute("admin");
    if (admin == null) {
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

      <div class="menu-item active2 one" onclick="window.location='homeAdmin'">
        <img src="${pageContext.request.contextPath}/assets/home-white.svg">
        <h2>Início</h2>
      </div>

      <div class="menu-item active2 two" onclick="window.location='showClass'">
        <img src="${pageContext.request.contextPath}/assets/student.svg" alt="Alunos">
        <h2>Alunos</h2>
      </div>

      <div class="menu-item active2 three" onclick="window.location='showProfessors'">
        <img src="${pageContext.request.contextPath}/assets/user-solo.svg">
        <h2>Professores</h2>
      </div>

      <div class="menu-item active" onclick="location.reload()">
        <img src="${pageContext.request.contextPath}/assets/info-circle-3.svg">
        <h2>Informações</h2>
      </div>

    </div>

    <!-- Logout -->
    <div class="logout" onclick="window.location='logoutAdmin'">
      <img src="${pageContext.request.contextPath}/assets/icon-logout.svg" width="38">
    </div>
  </aside>


  <!-- Main content area -->
  <main class="main">

    <section class="hero">

      <div class="container">

        <div class="left">

          <img src="${pageContext.request.contextPath}/assets/text-information.svg" class="title-img">

          <button class="btn" onclick="window.location='homeAdmin'">
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