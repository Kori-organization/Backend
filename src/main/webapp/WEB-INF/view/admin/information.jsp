<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Kori – Professores</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin/information.css">
  <link rel="icon" href="${pageContext.request.contextPath}/assets/logo-top.svg" type="image/png">
</head>
<body>
<!-- Main app container -->
<div class="app">
  <!-- Sidebar navigation -->
  <aside class="sidebar">
    <div class="sidebar-menu">
      <div class="menu-item active2 one" onclick="window.location='index.html'">
        <img src="${pageContext.request.contextPath}/assets/home-white.svg">
        <h2>Início</h2>
      </div>
      <div class="menu-item active2 two" onclick="window.location='student.html'">
        <img src="${pageContext.request.contextPath}/assets/student.svg" alt="Alunos">
        <h2>Alunos</h2>
      </div>
      <div class="menu-item active2 three" onclick="window.location='teacher.html'">
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
    <div>
      <img src="${pageContext.request.contextPath}/assets/circles.svg" alt="" width="1400px">
    </div>

    <div class="home-button" onclick="location.reload()">
      <img src="${pageContext.request.contextPath}/assets/info-circle-3.svg" alt="Página inicial" width="43">
    </div>

    <div class="children-container">
      <img src="${pageContext.request.contextPath}/assets/children.png" alt="Criança" class="children-img">

      <div class="info-box box1">
        Nossa prioridade é a educação <br>infantil de qualidade.
      </div>

      <div class="info-box box2">
        Empatia e respeito com o <br>futuro do país.
      </div>
    </div>

    <div class="text-kori">
      <img src="${pageContext.request.contextPath}/assets/text-information.svg" alt="">
    </div>

    <button class="info-button" onclick="window.location='homeAdmin'">
      Ir para Início
    </button>
  </main>
</div>
</body>
</html>
