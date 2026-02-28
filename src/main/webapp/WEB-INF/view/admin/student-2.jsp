<%@ page import="java.util.ArrayList" %>
<%@ page import="com.example.koribackend.model.entity.Student" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>

<head>

  <meta charset="utf-8" />
  <meta name="viewport" content="width=device-width,initial-scale=1" />
  <title>Kori — Alunos</title>
  <link rel="icon" href="${pageContext.request.contextPath}/assets/logo-top.svg" type="image/png">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin/student-2.css">
  <%
    ArrayList<Student> students = (ArrayList<Student>) request.getAttribute("students");
    String resultDeleteStudent = (String) request.getAttribute("resultDeleteStudent");
    String resultEditStudent = (String) request.getAttribute("resultEditStudent");
    String filter = (String) request.getAttribute("filter");
  %>
</head>
<body>

<!-- Top navigation bar -->
<div class="topbar">
  <!-- Back button -->
  <a class="back" href="showClass" title="Voltar">
    <img src="${pageContext.request.contextPath}/assets/left.svg" alt="Voltar" width="15px"> Voltar
  </a>
</div>

<main>
  <!-- Page title -->
  <h1>Alunos do ${empty sessionScope.serie ? "" : sessionScope.serie}º Ano</h1>

  <div class="search-container">
    <!-- Search box -->
    <div class="search-box">
      <input type="text" placeholder="Pesquisar Aluno..." aria-label="Pesquisar Aluno" id="inputFilter" value="<%=filter%>">
      <button class="btn-limpar" id="buttonClear" onclick="window.location='selectClass?serie=${empty sessionScope.serie ? "" : sessionScope.serie}'">
        Limpar
      </button>

      <button class="search-btn" title="Pesquisar" aria-label="Pesquisar" id="buttonFilter">
        <img src="${pageContext.request.contextPath}/assets/icon-search.svg" width="17" alt="Pesquisar">
      </button>
    </div>
  </div>

  <!-- Students table section -->
  <section class="table" aria-label="Lista de alunos">

    <!-- Table header -->
    <div class="header" role="row">
      <div>Matrícula <img src="${pageContext.request.contextPath}/assets/chevron.svg" alt="" width="10"></div>
      <div>Nome <img src="${pageContext.request.contextPath}/assets/chevron.svg" alt="" width="10"></div>
      <div>Email <img src="${pageContext.request.contextPath}/assets/chevron.svg" alt="" width="10"></div>
      <div>Admissão <img src="${pageContext.request.contextPath}/assets/chevron.svg" alt="" width="10"></div>
    </div>

    <!-- Student row -->
    <% for (Student student : students) { %>
      <div class="student-row" role="row">
        <div class="matricula"><%=student.getEnrollment()%></div>
        <div class="nome"><%=student.getName()%></div>
        <div class="email"><%=student.getEmail()%></div>
        <div class="admissao"><%=student.getFormatDate()%></div>

        <!-- Action buttons -->
        <div class="actions">
          <button class="btn-observacoes">
            <img src="${pageContext.request.contextPath}/assets/list.svg" width="18" alt="">
          </button>
        </div>
      </div>
    <% } %>
  </section>
  <!-- Spacer at bottom -->
  <div style="height:120px"></div>
</main>

<!-- Popup menu container -->
<div class="popup-menu" id="popupMenu">
  <!-- Edit option -->
  <div class="popup-item edit" id="popupEdit">
    <img src="${pageContext.request.contextPath}/assets/icon-edit.svg" width="16">
    Editar
  </div>

  <!-- Bulletin option -->
  <div class="popup-item" id="popupBulletin">
    <img src="${pageContext.request.contextPath}/assets/greeting-4.svg" width="18" style="margin-left: -3px;">
    Boletim
  </div>

  <!-- Observations option -->
  <div class="popup-item" id="popupObservations">
    <img src="${pageContext.request.contextPath}/assets/notes-5.svg" width="16">
    Observações
  </div>

  <!-- Delete option -->
  <div class="popup-item delete" id="popupDelete">
    <img src="${pageContext.request.contextPath}/assets/trash.svg" width="16">
    Deletar
  </div>
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
      <button id="confirmCancel" class="btn btn-cancel" type="button">Voltar</button>
      <button id="confirmDelete" class="btn btn-delete" type="button">Deletar</button>
    </div>
  </div>
</div>

<!-- Toast container -->
<div id="toastWrapTeacher" class="toast-wrap-teacher" aria-live="polite" aria-atomic="true"></div>

<!-- EDIT STUDENT OVERLAY (matches your screenshot) -->
<div id="editStudentOverlay" class="overlay" aria-hidden="true">

  <div class="edit-student-card" role="dialog" aria-modal="true" aria-labelledby="editStudentTitle">

    <!-- big colored header -->
    <div class="edit-student-header">
      <h2 id="editStudentTitle">Alterar <span id="editStudentNameHeader">Aluno</span></h2>
    </div>

    <!-- form -->
    <form class="edit-student-form" id="formEdit" method="post" action="updateStudent">
      <div class="row-2">
        <input type="hidden" id="inputId" value="" name="id">

        <div class="field t1">
          <label>Nome</label>
          <input id="inputName" type="text" placeholder="Nome completo" name="name">
        </div>

        <div class="field t1">
          <label>Email</label>
          <input id="inputEmail" type="email" placeholder="email@kori.com" name="email">
        </div>
      </div>

      <div class="row-3" style="margin-top:18px;">
        <div class="field small t2">
          <label>Admissão</label>
          <input id="inputAdmission" type="date" placeholder="01 / 02 / 2025" name="admission">
        </div>

        <div class="field xsmall t2">
          <label>Série</label>
          <select id="inputSeries" name="serie">

            <option value="">Selecionar turma</option>

            <% int serie = (int) request.getSession(false).getAttribute("serie"); %>
            <% for (int i = 1; i <= 5; i++) { %>
              <option value="<%=i%>"<%=serie == i ? " selected" : ""%>><%=i%>° Ano</option>
            <% } %>

          </select>
        </div>

        <div class="field t1">
          <label>Senha</label>
          <div class="password-input-wrap small">
            <input id="inputPassword" type="password" placeholder="••••••" name="password">
            <button type="button" class="password-toggle" id="togglePwd">
              <img src="${pageContext.request.contextPath}/assets/eye-off.svg" width="18" alt="toggle">
            </button>
          </div>
        </div>
      </div>

    </form>

    <!-- actions -->
    <div class="edit-student-actions">
      <button class="btn-edit-cancel">Voltar</button>
      <button class="btn-edit-save">Salvar</button>
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
<script src="${pageContext.request.contextPath}/js/admin/student-2.js"></script>
<script>
  <% if ("true".equals(resultDeleteStudent)) { %>
    createToast('Aluno excluído com sucesso', '${requestScope.name}' , 'success');
  <% } else if ("false".equals(resultDeleteStudent)) { %>
    createToast('Erro ao excluído aluno', 'Erro ao tentar excluir o aluno. Tente novamente.' , 'error');
  <% } else if ("true".equals(resultEditStudent)) { %>
    createToast('Aluno atualizado com sucesso', '${requestScope.name}' , 'success');
  <% } else if ("false".equals(resultEditStudent)) { %>
    createToast('Erro ao editar aluno', 'Erro ao tentar editar o aluno. Tente novamente.', 'error');
  <% } %>
</script>
</html>
