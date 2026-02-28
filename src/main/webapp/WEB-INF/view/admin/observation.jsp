<%@ page import="com.example.koribackend.dto.StudentObservationsDTO" %>
<%@ page import="com.example.koribackend.model.entity.Observation" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width,initial-scale=1" />
    <title>Kori — Observações</title>
    <link rel="icon" href="${pageContext.request.contextPath}/assets/logo-top.svg" type="image/png">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin/observation.css">
    <%
        StudentObservationsDTO studentObservationsDTO = (StudentObservationsDTO) request.getAttribute("studentObservationsDTO");
        String resultDeleteObservation = (String) request.getAttribute("resultDeleteObservation");
    %>
</head>
<body>

<!-- Top navigation bar -->
<div class="topbar">
    <!-- Back button -->
    <a class="back" href="selectClass?serie=<%=studentObservationsDTO.getSerie()%>" title="Voltar">
        <img src="${pageContext.request.contextPath}/assets/left.svg" alt="Voltar" width="15px"> Voltar
    </a>
</div>

<main>

    <section class="student-top">
        <div class="student-card">
            <div class="student-left">
                <h2 class="student-name"><%=studentObservationsDTO.getName()%></h2>
                <p class="student-email"><%=studentObservationsDTO.getEmail()%></p>
            </div>

            <div class="student-right">
                <div class="info-pill">
                    <div class="series">Série: <strong><%=studentObservationsDTO.getSerie()%>º Ano</strong></div>
                    <div class="matricula">Matrícula: <span><%=studentObservationsDTO.getEnrollment()%></span></div>
                </div>
            </div>
        </div>

        <p class="student-note">
            Veja as observações de todos os professores para este aluno.
        </p>
    </section>

    <!-- Observation container -->
    <div class="obs-container" id="obsContainer">
        <% int i = 1; String color = "pink"; %>
        <% for (Observation observation : studentObservationsDTO.getObservations()) { %>
            <div class="obs-card <%=color%>">
                <input type="hidden" value="<%=observation.getId()%>" class="id">
                <p><%=observation.getObservation()%></p>

                <button class="delete-btn">
                    <img src="${pageContext.request.contextPath}/assets/trash.svg" alt="Excluir">
                </button>

                <button class="copy-btn">
                    <img src="${pageContext.request.contextPath}/assets/copy.svg" alt="Copiar">
                </button>
            </div>
            <%
                switch (i) {
                    case 1 -> { i = 2; color = "yellow"; }
                    case 2 -> { i = 3; color = "blue"; }
                    case 3 -> { i = 4; color = "green"; }
                    case 4 -> { i = 1; color = "pink"; }
                }
            %>
        <% } %>
    </div>
</main>
<div id="copyToast" class="copy-toast">
    Copiado para a área de transferência
</div>

<div id="confirmDeleteOverlay" class="overlay" aria-hidden="true">

    <div class="confirm-modal">
        <div class="confirm-icon-edit">
            <img src="${pageContext.request.contextPath}/assets/info-circle-red.svg" alt="">
        </div>

        <h2 class="confirm-title">
            Deseja excluir esta observação?
        </h2>

        <div class="confirm-buttons">
            <button id="confirmCancel" class="btn-confirm btn-cancel">
                Cancelar
            </button>

            <button id="confirmDelete" class="btn-confirm btn-save">
                Excluir
            </button>
        </div>
    </div>
</div>

<!-- TOAST CONTAINER IGUAL PROFESSOR -->
<div id="toastWrapTeacher" class="toast-wrap-teacher"></div>

</body>
<script>
    const contextPath = "<%=request.getContextPath()%>"
</script>
<script src="${pageContext.request.contextPath}/js/admin/observation.js"></script>
<script>
    <% if ("true".equals(resultDeleteObservation)) { %>
        createToast('Observação excluída<br>com sucesso','', 'success');
    <% } else if ("false".equals(resultDeleteObservation)) { %>
        createToast('Erro ao excluir a observação.<br>Tente novamente.','', 'error');
    <% } %>
</script>
</html>
