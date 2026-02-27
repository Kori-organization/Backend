<%@ page import="com.example.koribackend.model.entity.ReportCard" %>
<%@ page import="com.example.koribackend.model.entity.Grade" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <meta charset="utf-8" />
  <meta name="viewport" content="width=device-width,initial-scale=1" />
  <title>Kori — Boletim</title>

  <link rel="icon" href="${pageContext.request.contextPath}/assets/logo-top.svg" type="image/png">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin/bulletin.css">
  <%
    ReportCard reportCard = (ReportCard) request.getAttribute("reportCard");
    int contador = 1;
    String resultAddAllGrades = (String) request.getAttribute("resultAddAllGrades");
  %>
</head>
<body>

<div class="topbar">
  <a class="back" href="selectClass">
    <img src="${pageContext.request.contextPath}/assets/left.svg" width="15"> Voltar
  </a>
</div>

<main>

  <!-- FORM START -->
  <form class="bulletin-wrapper" id="bulletinForm" method="post" action="addGradesReportCard">

    <input type="hidden" name="size" value="<%=reportCard.getGrader().size()%>">

    <div class="bulletin-title">
      <h1>Davi Arakaki</h1>
      <span class="bulletin-year">1º Ano</span>
    </div>

    <div class="final-status">
      <img src="${pageContext.request.contextPath}/assets/download.svg" class="download-icon">
      <span>Situação final: Reprovado.</span>
    </div>

    <div class="bulletin">

      <!-- DISCIPLINES -->
      <div class="bcol left">
        <h3>Disciplina</h3>
        <% for (Grade grade : reportCard.getGrader()) { %>
          <input type="hidden" name="subject_<%=contador%>" value="<%=grade.getSubject()%>">
          <% contador++; %>
          <div class="bitem subject<%=reportCard.getGrader().getFirst().equals(grade) ? " top" : ""%><%=reportCard.getGrader().getLast().equals(grade) ? " bottom" : ""%>"><%=grade.getSubject()%></div>
        <% } %>
        <% contador = 1; %>
      </div>

      <!-- SEMESTER 1 -->
      <div class="bcol left2">
        <h3><span class="badge yellow">1º Semestre</span>Média</h3>

        <% for (Grade grade : reportCard.getGrader()) { %>
          <div class="bitem<%=reportCard.getGrader().getFirst().equals(grade) ? " top" : ""%><%=reportCard.getGrader().getLast().equals(grade) ? " bottom" : ""%>">
            <input type="number" name="n1_<%=contador%>" class="nota n1" value="<%=grade.getGrade1() == -1 ? "" : grade.getGrade1()%>" step="0.01">
          </div>
          <% contador++; %>
        <% } %>
        <% contador = 1; %>
      </div>

      <!-- SEMESTER 2 -->
      <div class="bcol left3">
        <h3><span class="badge yellow">2º Semestre</span>Média</h3>

        <% for (Grade grade : reportCard.getGrader()) { %>
          <div class="bitem<%=reportCard.getGrader().getFirst().equals(grade) ? " top" : ""%><%=reportCard.getGrader().getLast().equals(grade) ? " bottom" : ""%>">
            <input type="number" name="n2_<%=contador%>" class="nota n2" value="<%=grade.getGrade2() == -1 ? "" : grade.getGrade2()%>" step="0.01">
          </div>
          <% contador++; %>
        <% } %>
        <% contador = 1; %>
      </div>

      <!-- RECUPERATION -->
      <div class="bcol right3">
        <h3><span class="badge pink">Recuperação</span>Média</h3>

        <% for (Grade grade : reportCard.getGrader()) { %>
        <div class="bitem<%=reportCard.getGrader().getFirst().equals(grade) ? " top" : ""%><%=reportCard.getGrader().getLast().equals(grade) ? " bottom" : ""%>">
          <input type="number" name="rec_<%=contador%>" class="nota rec" value="<%=grade.getRec() == -1 ? "" : grade.getRec()%>" step="0.01">
        </div>
        <% contador++; %>
        <% } %>
      </div>

      <!-- AVERAGE -->
      <div class="bcol right2">
        <h3>Média Final</h3>
        <% for (int i = 0; i < reportCard.getGrader().size();i++) { %>
          <div class="bitem media-final<%=i == 0 ? " top" : ""%><%=i == reportCard.getGrader().size() - 1 ? " bottom" : ""%>"></div>
        <% } %>
      </div>

      <!-- STATUS -->
      <div class="bcol right">
        <h3>Situação</h3>
        <% for (int i = 0; i < reportCard.getGrader().size();i++) { %>
          <div class="bitem status<%=i == 0 ? " top" : ""%><%=i == reportCard.getGrader().size() - 1 ? " bottom" : ""%>"></div>
        <% } %>
      </div>

    </div>

    <button type="submit" class="save-btn">
      Salvar alterações
    </button>

  </form>
</main>

<div id="toastWrap" class="toast-wrap" aria-live="polite" aria-atomic="true"></div>

<div class="toast" id="toast">
  Número inválido
</div>
</body>
<script>
  const contextPath = "<%=request.getContextPath()%>"
</script>
<script src="${pageContext.request.contextPath}/js/admin/bulletin.js"></script>
<script>
  <% if ("true".equals(resultAddAllGrades)) { %>
    createToast("Boletim atualizado com sucesso","As notas do boletim foram alterados com sucesso.")
  <% } else if ("false".equals(resultAddAllGrades)) { %>
    createToast("Erro ao atualizar o boletim","Algo de errado aconteceu ao alterar as notas. Tente novamente.")
  <% } %>
</script>
</html>
