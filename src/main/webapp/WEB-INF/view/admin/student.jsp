<%@ page import="com.example.koribackend.model.entity.Administrator" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Kori – Alunos</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin/student.css">
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
<div class="app">

  <!-- Sidebar -->
  <aside class="sidebar">
    <div class="sidebar-menu">
      <div class="menu-item active2 one" onclick="window.location='homeAdmin'">
        <img src="${pageContext.request.contextPath}/assets/home-white.svg">
        <h2>Início</h2>
      </div>
      <div class="menu-item active" onclick="location.reload()">
        <img src="${pageContext.request.contextPath}/assets/student-2.svg">
        <h2>Alunos</h2>
      </div>
      <div class="menu-item active2 two" onclick="window.location='showProfessors'">
        <img src="${pageContext.request.contextPath}/assets/user-solo.svg">
        <h2>Professores</h2>
      </div>
      <div class="menu-item active2 three" onclick="window.location='informationsAdmin'">
        <img src="${pageContext.request.contextPath}/assets/info-circle-white.svg">
        <h2>Informações</h2>
      </div>
    </div>

    <!-- Logout -->
    <div class="logout" onclick="window.location='logoutAdmin'">
      <img src="${pageContext.request.contextPath}/assets/icon-logout.svg" width="38">
    </div>
  </aside>

  <!-- Main -->
  <main class="main">
    <div class="color"></div>

    <div class="top-main">
      <div class="logo-container" onclick="window.location='homeAdmin'">
        <img src="${pageContext.request.contextPath}/assets/logo.svg" width="180">
      </div>
      <div class="home-button" onclick="location.reload()">
        <img src="${pageContext.request.contextPath}/assets/student-2.svg" width="50">
      </div>
    </div>

    <!-- Cards -->
    <div class="bulletin-wrapper">

      <div class="cards-grid">

        <div class="grade-card pink" onclick="window.location='selectClass?serie=1'">
          <div class="card-label">
            <h2>1º Ano</h2>
            <p>Clique para cadastrar notas desta turma.</p>
          </div>
        </div>

        <div class="grade-card green" onclick="window.location='selectClass?serie=2'">
          <div class="card-label">
            <h2>2º Ano</h2>
            <p>Clique para cadastrar notas desta turma.</p>
          </div>
        </div>

        <div class="grade-card blue" onclick="window.location='selectClass?serie=3'">
          <div class="card-label">
            <h2>3º Ano</h2>
            <p>Clique para cadastrar notas desta turma.</p>
          </div>
        </div>

        <div class="grade-card yellow" onclick="window.location='selectClass?serie=4'">
          <div class="card-label">
            <h2>4º Ano</h2>
            <p>Clique para cadastrar notas desta turma.</p>
          </div>
        </div>

        <div class="grade-card gray" onclick="window.location='selectClass?serie=5'">
          <div class="card-label">
            <h2>5º Ano</h2>
            <p>Clique para cadastrar notas desta turma.</p>
          </div>
        </div>

      </div>
    </div>

  </main>
</div>
</body>
</html>
