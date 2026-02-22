<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <!-- Basic page settings -->
  <meta charset="utf-8" />
  <meta name="viewport" content="width=device-width,initial-scale=1" />
  <title>Kori – Início</title>
  <link rel="icon" href="${pageContext.request.contextPath}/assets/logo-top.svg" type="image/png">

  <!-- Main stylesheet -->
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin/home-screen.css">
</head>
<body>
<!-- App wrapper -->
<div class="app">

  <!-- Sidebar navigation -->
  <aside class="sidebar">
    <div class="sidebar-menu">
      <div class="menu-item active" onclick="location.reload()">
        <img src="${pageContext.request.contextPath}/assets/home.svg" alt="Página inicial">
        <h2>Início</h2>
      </div>

      <div class="menu-item active2 one" onclick="window.location='student.html'">
        <img src="${pageContext.request.contextPath}/assets/student.svg" alt="Alunos">
        <h2>Alunos</h2>
      </div>

      <div class="menu-item active2 two" onclick="window.location='teacher.html'">
        <img src="${pageContext.request.contextPath}/assets/user-solo.svg" alt="Professores">
        <h2>Professores</h2>
      </div>

      <div class="menu-item active2 three" onclick="window.location='informationsAdmin'">
        <img src="${pageContext.request.contextPath}/assets/info-circle-white.svg" alt="Professores">
        <h2>Informações</h2>
      </div>
    </div>

    <!-- Logout -->
    <div class="logout" onclick="window.location='logoutAdmin'">
      <img src="${pageContext.request.contextPath}/assets/icon-logout.svg" width="38">
    </div>
  </aside>

  <!-- Main content -->
  <main class="main">
    <div class="color"></div>
    <div class="color2"></div>

    <!-- Top header -->
    <div class="top-main">
      <div class="logo-container">
        <img src="${pageContext.request.contextPath}/assets/logo.svg" alt="Kori Logo" width="180px" onclick="location.reload()">
      </div>
      <div class="home-button">
        <img src="${pageContext.request.contextPath}/assets/home.svg" alt="Página inicial" width="40px" onclick="location.reload()">
      </div>
    </div>

    <!-- Main layout grid -->
    <div class="grid">

      <!-- Left column -->
      <section class="left-column">

        <!-- Welcome card -->
        <div class="card welcome-card">
          <div>
            <h1>Olá, Administrador!</h1>
            <ul class="admin-list">
              <li>cadastre alunos novos</li>
              <li>altere notas no boletim</li>
              <li>altere observações</li>
              <li>adicione ou exclua usuários</li>
            </ul>
          </div>

          <div class="monster-container">
            <img src="${pageContext.request.contextPath}/assets/administrator.svg" alt="Administrador" width="250px">
          </div>
        </div>

        <!-- Shortcuts -->
        <div class="shortcuts-container">
          <div class="card shortcut-card" onclick="window.location='dowloadRegulation?point=homeAdmin'">
            <img src="${pageContext.request.contextPath}/assets/document.svg" alt="School rules" class="shortcut-icon">
            <div>Ver regulamento escolar</div>
          </div>

          <div class="card shortcut-card"
               onclick="window.location.href='mailto:korieducation@gmail.com?subject=Suporte%20Kori'">
            <img src="${pageContext.request.contextPath}/assets/support.svg" alt="Kori Support" class="shortcut-icon">
            <div>Suporte Kori</div>
          </div>
        </div>

        <!-- Action cards -->
        <div class="actions-container">
          <div class="card action-card student" onclick="window.location='student.html'">
            <img src="${pageContext.request.contextPath}/assets/plus-blue.svg" alt="Adicionar aluno">
            <span>Adicionar aluno</span>
          </div>

          <div class="card action-card teacher2" onclick="window.location='teacher.html'">
            <img src="${pageContext.request.contextPath}/assets/plus-pink.svg" alt="Adicionar professor">
            <span>Adicionar professor</span>
          </div>
        </div>
      </section>

      <!-- Right column -->
      <aside class="right-column">

        <!-- Calendar -->
        <div class="calendar-container">
          <div class="card">
            <div class="calendar-header">
              <button id="previous-month">‹</button>
              <strong id="current-month">Janeiro 2026</strong>
              <button id="next-month">›</button>
            </div>
            <div class="calendar-grid" id="calendar"></div>
          </div>
        </div>
        <div class="calendar-legend">
          <div class="legend-item">
            <span class="legend-color today"></span>
            <span>Hoje</span>
          </div>
          <div class="legend-item">
            <span class="legend-color holiday"></span>
            <span>Feriado</span>
          </div>
        </div>

        <!-- Teachers list -->
        <div class="card teachers-card">
          <div class="user-group-teacher">
            <img src="${pageContext.request.contextPath}/assets/user-group.svg" alt="Teachers">
            <h3>Suas Turmas</h3>
          </div>

          <div class="teacher one">
            <span>1° Série</span>
          </div>


          <div class="teacher two">
            <span>2° Série</span>
          </div>


          <div class="teacher three">
            <span>3° Série</span>
          </div>


          <div class="teacher four">
            <span>4° Série</span>
          </div>

          <div class="teacher five">
            <span>5° Série</span>
          </div>
        </div>
      </aside>
    </div>
  </main>
</div>

<!-- Scripts -->
<script src="${pageContext.request.contextPath}/js/admin/home-screen.js"></script>
</body>
</html>
