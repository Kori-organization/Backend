<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.koribackend.dto.StudentDTO" %>
<%@ page import="java.util.List" %>
<%
        List<StudentDTO> students = (List<StudentDTO>) request.getAttribute("students");
        int grade = (Integer) request.getAttribute("grade");
        String dataState = "";
%>
<!doctype html>
<html lang="pt-BR">

<head>
    <!-- Character encoding -->
    <meta charset="utf-8" />

    <!-- Responsive viewport -->
    <meta name="viewport" content="width=device-width,initial-scale=1" />

    <!-- Page title -->
    <title>Alunos do <%=grade%>º Ano — Boletim</title>

    <!-- Favicon -->
    <link rel="icon" href="${pageContext.request.contextPath}/assets/logo-top.svg" type="image/png">

    <!-- Main stylesheet -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/professor/bulletin-students-list.css">
</head>

<body>

    <!-- Top navigation bar -->
    <div class="topbar">
        <!-- Back button -->
        <a class="back" href="bulletinGrades" title="Voltar">
            <img src="${pageContext.request.contextPath}/assets/left.svg" alt="Voltar" width="15px"> Voltar
        </a>

        <!-- Greeting icon (reloads page on click) -->
        <img src="${pageContext.request.contextPath}/assets/greeting-4.svg" alt="" width="40px" class="img-greeting" onclick="location.reload()">
    </div>

    <main>
        <!-- Page title -->
        <h1>Alunos do <%=grade%>º Ano</h1>

        <!-- Search box -->
        <form action="bulletinStudentsList">
            <div class="search-box">
                <input type="hidden" name="grade" value="<%=grade%>">
                <input type="text" placeholder="Pesquisar Aluno..." aria-label="Pesquisar Aluno" name="studentId">
                <button class="search-btn" title="Pesquisar" aria-label="Pesquisar" type="submit">
                    <img src="${pageContext.request.contextPath}/assets/icon-search.svg" width="17" alt="Pesquisar">
                </button>
            </div>
        </form>

        <!-- Students table section -->
        <section class="table" aria-label="Lista de alunos">

            <!-- Table header -->
            <div class="header" role="row">
                <div>Matrícula <img src="${pageContext.request.contextPath}/assets/chevron.svg" alt="" width="10"></div>
                <div>Nome <img src="${pageContext.request.contextPath}/assets/chevron.svg" alt="" width="10"></div>
                <div>Email <img src="${pageContext.request.contextPath}/assets/chevron.svg" alt="" width="10"></div>
                <div>Admissão <img src="${pageContext.request.contextPath}/assets/chevron.svg" alt="" width="10"></div>
            </div>

            <!--
            State reference:
            - no-notes        -> Only "+" enabled
            - needs-recovery  -> Recovery enabled
            - complete        -> Highlighted row, all actions locked
            -->

            <!-- NO NOTES -->

        <% for (StudentDTO student: students) {
            if (student.getGrade1() == -1 || student.getGrade2() == -1) {
                dataState = "no-notes";
            } else if ((student.getGrade1() + student.getGrade2()) / 2 < 7) {
                dataState = "needs-recovery";
            } else {
                dataState = "complete";
            }
        %>
            <div class="student-row" role="row" data-state="<%=dataState%>">
                <div class="matricula"><%=student.getEnrollment()%></div>
                <div class="nome"><%=student.getName()%></div>
                <div class="email"><%=student.getEmail()%></div>
                <div class="admissao"><%=student.getIssueDate()%></div>

                <div class="actions">
                    <button class="action-btn plus">
                        <img src="${pageContext.request.contextPath}/assets/plus.svg" width="18">
                    </button>

                    <div class="square-actions">
                        <img src="${pageContext.request.contextPath}/assets/recovery.svg" class="recovery disabled" width="42">
                        <img src="${pageContext.request.contextPath}/assets/notes-5.svg" width="30">
                    </div>
                </div>
            </div>
        <%}%>
            <!-- Spacer at bottom -->
            <div style="height:26px"></div>
        </section>

    </main>

    <!-- Grade modal overlay -->
    <div id="gradeOverlay" class="overlay" aria-hidden="true">
        <div class="modal" role="dialog" aria-modal="true" aria-labelledby="gradeTitle">

            <!-- Modal header -->
            <div class="modal-top">
                <div class="student-info">
                    <h2 id="gradeTitle">Nome do Aluno</h2>
                    <p id="gradeEmail">email@exemplo.com</p>
                </div>

                <!-- Student metadata -->
                <div class="meta">
                    <div>Série: <strong id="gradeSerie">1º Ano</strong></div>
                    <div>Matrícula: <strong id="gradeMat">—</strong></div>
                </div>
            </div>

            <!-- Modal body -->
            <div class="modal-body">
                <div class="notes-col">

                    <!-- Grades inputs -->
                    <div class="notes-2">
                        <div class="field">
                            <div class="label">Digite a Nota 1</div>
                            <div class="note-row">
                                <input id="nota1" class="note-input" type="number" min="0" max="10" step="0.1"
                                    placeholder="—" />
                            </div>
                        </div>

                        <div class="field">
                            <div class="label">Digite a Nota 2</div>
                            <div class="note-row">
                                <input id="nota2" class="note-input" type="number" min="0" max="10" step="0.1"
                                    placeholder="—" />
                            </div>
                        </div>
                    </div>

                    <!-- Admin warning -->
                    <div id="saveHint" class="admin-note">
                        Atenção: somente um administrador poderá alterar as notas digitadas acima.
                    </div>
                </div>

                <!-- Average grade display -->
                <div class="average-box">
                    <div class="title">Média do aluno</div>
                    <div id="avgValue" class="value">—</div>
                </div>
            </div>

            <!-- Modal footer -->
            <div class="modal-footer">
                <button id="btnBack" class="btn ghost">Voltar</button>
                <button id="btnSaveNote" class="btn primary">Salvar nota</button>
            </div>
        </div>
    </div>

    <!-- Confirmation modal -->
    <div id="confirmOverlay" class="overlay" aria-hidden="true">
        <div class="confirm-content" role="dialog" aria-modal="true">

            <!-- Icon -->
            <div class="confirm-icon">
                <img src="${pageContext.request.contextPath}/assets/info-circle-3.svg" alt="Atenção">
            </div>

            <!-- Title -->
            <h2>Tem certeza que deseja<br>salvar a nota?</h2>

            <!-- Info pill -->
            <div class="confirm-pill">
                <span id="confirmStudent">Aluno</span>
                <span id="confirmAvg">Média: —</span>
            </div>

            <!-- Warning -->
            <p class="confirm-warning">
                Atenção: somente um administrador poderá<br>
                alterar essa informação.
            </p>

            <!-- Actions -->
            <div class="confirm-actions">
                <button id="confirmCancel" class="btn ghost">Voltar</button>
                <button id="confirmSend" class="btn primary">Salvar</button>
            </div>

        </div>
    </div>

    <!-- Toast notifications container -->
    <div class="toast-wrap" aria-live="polite" aria-atomic="true"></div>

    <!-- Recovery modal -->
    <div id="recoveryOverlay" class="overlay" aria-hidden="true">
        <div class="modal" role="dialog" aria-modal="true">

            <!-- Header -->
            <div class="modal-top" style="background-color: #C0CF5B;">
                <div class="student-info">
                    <h2 id="recoveryTitle">Nome do Aluno</h2>
                    <p id="recoveryEmail">email@exemplo.com</p>
                </div>

                <div class="meta">
                    <div>Série: <strong id="recoverySerie">1º Ano</strong></div>
                    <div>Matrícula: <strong id="recoveryMat">—</strong></div>
                </div>
            </div>

            <!-- Body -->
            <div class="modal-body">
                <div class="notes-col">
                    <div class="notes-2">
                        <div class="field">
                            <div class="label">Nota 1</div>
                            <input id="recNota1" class="note-input" disabled placeholder="—">
                        </div>

                        <div class="field">
                            <div class="label">Nota 2</div>
                            <input id="recNota2" class="note-input" disabled placeholder="—">
                        </div>

                        <div class="field">
                            <div class="label">Média</div>
                            <input id="recMedia" class="note-input" disabled placeholder="—">
                        </div>
                    </div>

                    <div class="field" style="margin-left: 35px;">
                        <div class="label">Digite a nota de recuperação</div>
                        <input id="recInput" class="note-input" type="number" min="0" max="10" step="0.1"
                            placeholder="—">
                    </div>
                </div>

                <div class="average-box">
                    <div class="title" style="font-size: 14px;">Média final do aluno</div>
                    <div id="recFinal" class="value">—</div>
                </div>
            </div>

            <!-- Footer -->
            <div class="modal-footer" style="margin-top: -30px;">
                <button id="recBack" class="btn ghost">Voltar</button>
                <button id="recToConfirm" class="btn primary">Salvar nota</button>
            </div>
        </div>
    </div>

    <!-- Recovery Confirmation modal -->
    <div id="confirmRecoveryOverlay" class="overlay" aria-hidden="true">
        <div class="confirm-content" role="dialog" aria-modal="true">
            <div class="confirm-icon">
                <img src="${pageContext.request.contextPath}/assets/info-circle-3.svg" alt="Atenção">
            </div>

            <h2>Salvar nota de recuperação?</h2>

            <div class="confirm-pill">
                <span id="confirmRecoveryStudent">Aluno</span>
                <span id="confirmRecoveryAvg">Média final: —</span>
            </div>

            <p class="confirm-warning">
                Atenção: somente um administrador poderá alterar essa informação.
            </p>

            <div class="confirm-actions">
                <button id="confirmRecoveryCancel" class="btn ghost">Voltar</button>
                <button id="confirmRecoverySend" class="btn primary">Salvar</button>
            </div>
        </div>
    </div>
</body>

<!-- Main JavaScript file -->
    <script src="${pageContext.request.contextPath}/js/professor/bulletin-students-list.js"></script>

</html>