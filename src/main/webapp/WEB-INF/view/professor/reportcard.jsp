<%@ page import="com.example.koribackend.model.entity.ReportCard" %>
<%@ page import="com.example.koribackend.model.entity.Grade" %>
<%@ page import="com.example.koribackend.model.entity.Student" %>
<%@ page import="com.example.koribackend.model.entity.Professor" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
 <%
     Professor professor = (Professor) session.getAttribute("professor");
     if (professor == null) {
         response.sendRedirect("enter");
         return;
     }

     Student student = (Student) request.getAttribute("student");
     ReportCard reportCard = (ReportCard) request.getAttribute("reportCard");
     int counter = reportCard.getGrader().size();
 %>
<!doctype html>
<html lang="pt-BR">

<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width,initial-scale=1" />
    <title><%=student.getName()%> — Boletim</title>

    <!-- Favicon -->
    <link rel="icon" href="${pageContext.request.contextPath}/assets/logo-top.svg" type="image/png">

    <!-- Main stylesheet -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/professor/reportcard.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/chatbot.css">
</head>

<body>

    <!-- Top navigation bar -->
    <div class="topbar">
        <!-- Back button -->
        <a class="back" href="reportCardStudentsList?grade=<%=student.getSerie()%>" title="Voltar">
            <img src="${pageContext.request.contextPath}/assets/left.svg" alt="Voltar" width="15px"> Voltar
        </a>

        <!-- Greeting icon (reloads page on click) -->
        <img src="${pageContext.request.contextPath}/assets/greeting-4.svg" alt="" width="40px" class="img-greeting" onclick="location.reload()">
    </div>

    <main>
        <!-- Bulletin section -->
        <div class="bulletin-wrapper">


            <div class="bulletin-title">
                <h1><%=student.getName()%></h1>
                <span class="bulletin-year"><%=student.getSerie()%>º Ano</span>
            </div>


            <!-- Final status card -->
            <div class="final-status">
                <img src="${pageContext.request.contextPath}/assets/download.svg" class="download-icon" alt="Download" onclick="window.location='createReportCardPDF?enrollment=<%=student.getEnrollment()%>'">
                Situação final: <%=reportCard.getFinalSituation()%>.
            </div
            <!-- Report card grid -->
            <div class="bulletin">
                <!-- Subjects column -->
                <div class="bcol left">
                    <h3>Disciplina</h3>
                    <% for (Grade grade : reportCard.getGrader()) { %>
                        <% counter--; %>
                        <div class="bitem subject <%=counter == reportCard.getGrader().size() - 1 ? "top" : ""%> <%=counter == 0 ? "bottom" : ""%>"><%=grade.getSubject()%></div>
                    <% } %>
                    <% counter = reportCard.getGrader().size(); %>
                </div
                <!-- First semester grades -->
                <div class="bcol left2">
                    <h3><span class="badge yellow">1º Semestre</span>Média</h3>
                    <% for (Grade grade : reportCard.getGrader()) { %>
                        <% counter--; %>
                        <div class="bitem <%=counter == reportCard.getGrader().size() - 1 ? "top" : ""%> <%=counter == 0 ? "bottom" : ""%> <%=(grade.getGrade1() != -1 && grade.getGrade1() < 7) ? " red" : ""%>"><%=(grade.getGrade1() == -1) ? "-" : String.format("%.2f",grade.getGrade1())%></div>
                    <% } %>
                    <% counter = reportCard.getGrader().size(); %>
                </div
                <!-- Second semester grades -->
                <div class="bcol left3">
                    <h3><span class="badge yellow">2º Semestre</span>Média</h3>
                    <% for (Grade grade : reportCard.getGrader()) { %>
                        <% counter--; %>
                        <div class="bitem <%=counter == reportCard.getGrader().size() - 1 ? "top" : ""%> <%=counter == 0 ? "bottom" : ""%> <%=(grade.getGrade2() != -1 && grade.getGrade2() < 7) ? " red" : ""%>"><%=(grade.getGrade2() == -1) ? "-" : String.format("%.2f",grade.getGrade2())%></div>
                    <% } %>
                    <% counter = reportCard.getGrader().size(); %>
                </div
                <!-- Recovery grades -->
                <div class="bcol right3">
                    <h3><span class="badge pink">Recuperação</span>Média</h3>
                    <% for (Grade grade : reportCard.getGrader()) { %>
                        <% counter--; %>
                        <div class="bitem <%=counter == reportCard.getGrader().size() - 1 ? "top" : ""%> <%=counter == 0 ? "bottom" : ""%><%=(grade.getRec() != -1 && grade.getRec() < 7) ? " red" : ""%>"><%=(grade.getRec() == -1) ? "-" : String.format("%.2f",grade.getRec())%></div>
                    <% } %>
                    <% counter = reportCard.getGrader().size(); %>
                </div
                <!-- Final averages -->
                <div class="bcol right2">
                    <h3>Média Final</h3>
                    <% for (Grade grade : reportCard.getGrader()) { %>
                        <% counter--; %>
                        <%
                            double average;
                            if (grade.getGrade1() != -1 && grade.getGrade2() != -1) {
                                average = (grade.getGrade1() + grade.getGrade2()) / 2;
                                if (average < 7 && grade.getRec() != -1) {
                                    average = (average + grade.getRec()) / 2;
                                }
                            } else {
                                average = -1;
                            }
                        %>
                        <div class="bitem <%=counter == reportCard.getGrader().size() - 1 ? "top" : ""%> <%=counter == 0 ? "bottom" : ""%> <%=(average != -1 && average < 7) ? " red" : ""%>"><%=(average == -1) ? "-" : String.format("%.2f",average)%></div>
                    <% } %>
                    <% counter = reportCard.getGrader().size(); %>
                </div
                <!-- Final status per subject -->
                <div class="bcol right">
                    <h3>Situação</h3>
                    <% for (Grade grade : reportCard.getGrader()) { %>
                    <% counter--; %>
                    <%
                        double average;
                        boolean isRec = false;
                        if (grade.getGrade1() != -1 && grade.getGrade2() != -1) {
                            average = (grade.getGrade1() + grade.getGrade2()) / 2;
                            if (average < 7 && grade.getRec() == -1) {
                                isRec = true;
                            } else if (grade.getRec() != -1) {
                                average = (average + grade.getRec()) / 2;
                            }
                        } else {
                            average = -1;
                        }
                    %>
                    <div class="bitem <%=counter == reportCard.getGrader().size() - 1 ? "top" : ""%> <%=counter == 0 ? "bottom" : ""%> <%=(isRec ? "yellow" : (average == -1 ? "" : (average >= 7 ? "green" : "red")))%>"><%=(isRec ? "Recuperação" : (average == -1 ? "-" : (average < 7 ? "Reprovado" : "Aprovado")))%></div>
                    <% } %>
                </div>
            </div>
        </div>
    </main>
    <!-- Floating Chat Button -->
    <div class="chat-toggle" id="chatToggle">
        <img src="${pageContext.request.contextPath}/assets/stars.svg" alt="" width="22px" style="margin-left: -2px">
    </div>

    <div class="chat-overlay" id="chatOverlay"></div>

    <!-- Chat Box -->
    <div class="chat-box" id="chatBox">

        <div class="chat-header">
            <div class="chat-controls">
                <button id="expandChat">⤢</button>
                <button id="closeChat">✕</button>
            </div>
        </div>

        <div class="chat-body">
            <div class="chat-avatar">
                <img src="${pageContext.request.contextPath}/assets/blue-moster1.svg" alt="" width="340px">
            </div>

            <p class="chat-text">
                Olá! Estou aqui para te auxiliar na plataforma Kori
                — <br>como funciona e o que você pode fazer com ela.
            </p>

            <div class="chat-suggestions">
                <button>Quais são os eventos próximos?</button>
                <button>Médias das salas</button>
            </div>
        </div>

        <div class="chat-input">
            <input type="text" placeholder="Digite aqui sua mensagem">
            <button><img src="${pageContext.request.contextPath}/assets/enter.svg" alt="" width="32px" style="margin-top: 5px"></button>
        </div>

    </div>

    <div class="chat-conversation" id="chatConversation">

        <div class="chat-header">
            <div class="chat-controls">
                <button id="expandConversation">⤢</button>
                <button id="closeConversation">✕</button>
            </div>
        </div>

        <div class="conversation-body" id="conversationMessages">

            <div class="message bot">
                <img src="${pageContext.request.contextPath}/assets/blue-monster2.svg">
                <div class="bubble">
                    Olá! Estou aqui para te auxiliar na plataforma Kori — como funciona e o que você pode fazer com ela.
                </div>
            </div>

        </div>

        <div class="chat-input conversation-input">
            <input type="text" placeholder="Digite aqui sua mensagem">
            <button>
                <img src="${pageContext.request.contextPath}/assets/enter.svg" width="32px" style="margin-top: 5px">
            </button>
        </div>

    </div>
</body>
<script>
    window.APP_CONFIG = {
        chatApiUrl: "https://datarep-g7xu.onrender.com/chat/professor",
        professorId: <%=professor.getId()%>
    };
</script>
<script src="${pageContext.request.contextPath}/js/professor/chatbotProfessor.js"></script>
<script src="${pageContext.request.contextPath}/js/observation-3.js"></script>

</html>