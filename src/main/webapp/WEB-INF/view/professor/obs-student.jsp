<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.koribackend.model.entity.Student" %>
<%@ page import="com.example.koribackend.model.entity.Observation" %>
<%@ page import="java.util.List" %>



<%
    Student student = (Student) request.getAttribute("student");
    List<Observation> observations = (List<Observation>) request.getAttribute("observations");
    int counter = 2;
%>

<!doctype html>
<html lang="pt-BR">

<head>
    <!-- Character encoding -->
    <meta charset="utf-8" />

    <!-- Responsive viewport -->
    <meta name="viewport" content="width=device-width,initial-scale=1" />

    <!-- Page title -->
    <title>Alunos do <%=student.getSerie()%>º Ano — Observações</title>

    <!-- Favicon -->
    <link rel="icon" href="${pageContext.request.contextPath}/assets/logo-top.svg" type="image/png">

    <!-- Main stylesheet -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/professor/obs-student.css">
</head>

<body>

    <!-- Top navigation bar -->
    <div class="topbar">
        <!-- Back button -->
        <a class="back" href="obsStudentsList?grade=<%=student.getSerie()%>" title="Voltar">
            <img src="${pageContext.request.contextPath}/assets/left.svg" alt="Voltar" width="15px"> Voltar
        </a>

        <!-- Greeting icon (reloads page on click) -->
        <img src="${pageContext.request.contextPath}/assets/notes-3.svg" alt="" width="40px" class="img-greeting" onclick="location.reload()">
    </div>

    <main>

        <section class="student-top">
            <div class="student-card">
                <div class="student-left">
                    <h2 class="student-name"><%=student.getName()%></h2>
                    <p class="student-email"><%=student.getEmail()%></p>
                </div>

                <div class="student-right">
                    <div class="info-pill">
                        <div class="series">Série: <strong><%=student.getSerie()%>º Ano</strong></div>
                        <div class="matricula">Matrícula: <span><%=student.getEnrollment()%></span></div>
                    </div>
                </div>
            </div>

            <p class="student-note">
                Veja as observações de todos os professores para este aluno.
            </p>
        </section>

        <!-- Observation container -->
        <div class="obs-container" id="obsContainer">
            <div class="obs-card p2">
                <img src="${pageContext.request.contextPath}/assets/create-observation.svg" alt="Criar observação">
                <h2>Criar observação para <br>esse aluno</h2>
            </div>
                <% for (Observation observation : observations) { %>
                   <% if (counter == 1) { %>
                        <div class="obs-card pink">
                      <% counter++; } else if (counter == 2) { %>
                         <div class="obs-card yellow">
                      <% counter++; } else if (counter == 3) { %>
                          <div class="obs-card blue">
                      <% counter++; } else { %>
                          <div class="obs-card green">
                      <% counter = 1; } %>

                             <p><%= observation.getObservation() %></p>

                             <button class="copy-btn">
                                 <img src="${pageContext.request.contextPath}/assets/copy.svg" alt="Copiar">
                             </button>
                        </div>
              <% } %>
        </div>
    </main>
    <div id="copyToast" class="copy-toast">
        Copiado para a área de transferência
    </div>

    <!-- POPUP 1 -->
    <form action="addObservation" method="post">
        <div id="observationOverlay" class="overlay" aria-hidden="true">
            <div class="modal observation-modal" role="dialog" aria-modal="true" aria-labelledby="obsTitle">
    
                <!-- header -->
                <div class="observation-header">
                    <div class="obs-student-left">
                        <h2 id="obsTitle"><%=student.getName()%></h2>
                        <p id="obsEmail"><%=student.getEmail()%></p>
                    </div>
    
                    <div class="obs-student-right">
                        <div>Série: <strong><%=student.getSerie()%>º Ano</strong></div>
                        <div>Matrícula: <span id="obsMat"><%=student.getEnrollment()%></span></div>
                    </div>
                </div>
    
                <!-- content -->
                <div class="observation-content">
                    <label for="observationInput">Escreva a observação para o aluno.</label>
    
                    <input type="hidden" name="studentId" value="<%=student.getEnrollment()%>">
                    <textarea id="observationInput" placeholder="" maxlength="1000" name="observation"></textarea>
    
                    <p class="admin-warning">
                        Atenção: somente um administrador poderá alterar a observação digitada acima.
                    </p>
                </div>
    
                <!-- actions -->
                <div class="modal-actions">
                    <button id="btnCancelObservation" class="btn btn-back" type="button">Voltar</button>
                    <button id="btnSaveObservation" class="btn btn-save" type="button">Salvar observação</button>
                </div>
            </div>
        </div>
    
        <!-- POPUP 2 -->
        <div id="confirmObservationOverlay" class="overlay" aria-hidden="true">
    
            <div class="confirm-modal">
    
                <!-- icon -->
                <div class="confirm-icon">
                    <img src="${pageContext.request.contextPath}/assets/info-circle-3.svg" alt="Info">
                </div>
    
                <!-- title -->
                <h2 class="confirm-title">
                    Tem certeza que deseja<br>salvar a observação?
                </h2>
    
                <!-- student name pill -->
                <div class="confirm-name" id="confirmObsName">
                    <%=student.getName()%>
                </div>
    
                <!-- warning -->
                <p class="confirm-warning">
                    Atenção: somente um administrador poderá alterar essa informação.
                </p>
    
                <!-- buttons -->
                <div class="confirm-buttons">
    
                    <button id="confirmObsCancel" class="btn-confirm btn-cancel">
                        Voltar
                    </button>
    
                    <button id="confirmObsSend" class="btn-confirm btn-save" type="submit">
                        Salvar
                    </button>
    
                </div>
    
            </div>
    
        </div>
    </form>


    <!-- TOAST -->
    <div id="toastWrap" class="toast-wrap"></div>

</body>
<script src="${pageContext.request.contextPath}/js/professor/obs-student.js"></script>

</html>