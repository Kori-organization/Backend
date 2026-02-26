<%@ page import="java.util.ArrayList" %>
<%@ page import="com.example.koribackend.model.entity.Professor" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Kori – Professores</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin/teacher.css">
  <link rel="icon" href="${pageContext.request.contextPath}/assets/logo-top.svg" type="image/png">
  <%
    ArrayList<Professor> professors = (ArrayList<Professor>) request.getAttribute("professors");
    String resultDeleteProfessor = (String) request.getAttribute("resultDeleteProfessor");
    String resultEditProfessor = (String) request.getAttribute("resultEditProfessor");
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
        <img src="${pageContext.request.contextPath}/assets/student.svg">
        <h2>Alunos</h2>
      </div>
      <div class="menu-item active" onclick="location.reload()">
        <img src="${pageContext.request.contextPath}/assets/user-solo-2.svg">
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

  <!-- Main content area -->
  <main class="main">
    <div class="color"></div>

    <!-- Top header -->
    <div class="top-main">
      <div class="logo-container" onclick="location.reload()">
        <img src="${pageContext.request.contextPath}/assets/logo.svg" width="180">
      </div>
      <div class="home-button" onclick="location.reload()">
        <img src="${pageContext.request.contextPath}/assets/user-solo-2.svg" alt="Professores" width="50">
      </div>
    </div>

    <!-- Page title -->
    <h1 class="first-h1">Professores</h1>

    <!-- Professors table -->
    <section class="table" aria-label="Lista de professores">

      <!-- Header -->
      <div class="header">
        <div>Usuário <img src="${pageContext.request.contextPath}/assets/chevron.svg" width="10"></div>
        <div>Nome <img src="${pageContext.request.contextPath}/assets/chevron.svg" width="10"></div>
        <div>Disciplina <img src="${pageContext.request.contextPath}/assets/chevron.svg" width="10"></div>
        <div>Senha <img src="${pageContext.request.contextPath}/assets/chevron.svg" width="10"></div>
        <div></div>
      </div>
      <!-- Teacher row -->
      <% for (Professor professor : professors) { %>
        <div class="student-row">
          <input type="hidden" class="teacher-id" value="<%=professor.getId()%>">
          <div class="teacher-username"><%=professor.getUsername()%></div>
          <div class="teacher-name"><%=professor.getName()%></div>
            <div class="teacher-subject"><%=professor.getSubjectName()%></div>
            <div class="teacher-password">••••••••</div>

            <div class="actions">
              <img src="${pageContext.request.contextPath}/assets/icon-edit.svg" width="22" style="cursor:pointer">
              <img src="${pageContext.request.contextPath}/assets/trash.svg" width="24" style="cursor:pointer">
            </div>
          </div>
      <% } %>
    </section>
    <div style="height:40px"></div>
  </main>
</div>

<!-- DELETE CONFIRM OVERLAY -->
<div id="confirmDeleteOverlay" class="overlay" aria-hidden="true">
  <div class="confirm-card" role="dialog" aria-modal="true" aria-labelledby="confirmTitle">
    <div class="confirm-icon" aria-hidden="true">
      <div class="confirm-icon-inner">
        <img src="${pageContext.request.contextPath}/assets/info-circle-red.svg" alt="Atenção" width="40" height="40">
      </div>
    </div>

    <h2 id="confirmTitle" class="confirm-title">
      Tem certeza que deseja<br>
      deletar <span id="confirmName">Ana souza</span>?
    </h2>

    <div class="confirm-actions">
      <input id="deleteProfessorId" type="hidden" value="">
      <button id="confirmCancel" class="btn btn-cancel" type="button">Voltar</button>
      <button id="confirmDelete" class="btn btn-delete" type="button">Deletar</button>
    </div>
  </div>
</div>

<!-- Toast container -->
<div id="toastWrapTeacher" class="toast-wrap-teacher" aria-live="polite" aria-atomic="true"></div>

<!-- EDIT TEACHER OVERLAY -->
<div id="editTeacherOverlay" class="overlay" aria-hidden="true">

  <div class="edit-card" role="dialog" aria-modal="true" aria-labelledby="editTeacherTitle">

    <!-- Header -->
    <div class="edit-header">
      <h2 id="editTeacherTitle">Editar Professor</h2>
    </div>

    <!-- Form -->
    <form class="edit-form" id="edit-form" action="editProfessor" method="post">

      <input type="hidden" name="idEdit" value="">

      <!-- Row 1 -->
      <div class="edit-row">
        <div class="edit-field small">
          <label>Usuário</label>
          <input type="text" name="userEdit" placeholder="usuario">
        </div>

        <div class="edit-field">
          <label>Nome</label>
          <input type="text" name="nameEdit" placeholder="Nome completo">
        </div>
      </div>

      <!-- Row 2 -->
      <div class="edit-row">
        <div class="edit-field">
          <label>Disciplina</label>
          <input type="text" name="subjectEdit" placeholder="Disciplina">
        </div>

        <div class="edit-field password-field">
          <label>Senha</label>

          <div class="password-input-wrap">
            <input type="password" name="passwordEdit" placeholder="••••••••">

            <!-- Eye icon -->
            <button type="button" class="password-toggle">
              <img src="${pageContext.request.contextPath}/assets/eye-off.svg" width="18">
            </button>
          </div>
        </div>
      </div>
    </form>

    <!-- Buttons -->
    <div class="edit-actions">
      <button class="btn-edit-cancel">
        Voltar
      </button>
      <button class="btn-edit-save">
        Salvar
      </button>
    </div>
  </div>
</div>

<!-- POPUP 2 -->
<div id="confirmTeacherOverlay" class="overlay" aria-hidden="true">

  <div class="confirm-modal">

    <!-- icon -->
    <div class="confirm-icon-edit">
      <img src="${pageContext.request.contextPath}/assets/info-circle-orange.svg" alt="Info">
    </div>

    <!-- title -->
    <h2 class="confirm-title">
      Tem certeza que deseja<br>salvar as alterações?
    </h2>

    <!-- buttons -->
    <div class="confirm-buttons">

      <button id="confirmTeacherCancel" class="btn-confirm btn-cancel">
        Voltar
      </button>

      <button id="confirmTeacherSend" class="btn-confirm btn-save">
        Salvar
      </button>

    </div>

  </div>

</div>
</body>

<script>
  const contextPath = "<%=request.getContextPath()%>"
</script>
<script src="${pageContext.request.contextPath}/js/admin/teacher.js"></script>
<script>
  <% if ("true".equals(resultDeleteProfessor)) { %>
    showToast('teacher', 'success', 'Professor(a) excluído', `${requestScope.name} removido com sucesso.`);
  <% } else if ("false".equals(resultDeleteProfessor)) { %>
    showToast('teacher', 'error', 'Erro ao excluir Professor', `Não foi possível excluir o professor. Tente novamente.`);
  <% } else if ("true".equals(resultEditProfessor)) { %>
    showToast('teacher', 'success', 'Professor(a) editado', `${requestScope.name} editado com sucesso.`);
  <% } else if ("false".equals(resultEditProfessor)) { %>
    showToast('teacher', 'success', 'Erro ao editar Professor', `Não foi possível editar o professor. Tente novamente.`);
  <% } %>
</script>
</html>
