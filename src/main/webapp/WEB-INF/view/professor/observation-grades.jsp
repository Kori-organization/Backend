<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="pt-BR">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Kori – Observações</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/professor/observation-grades.css">
    <link rel="icon" href="${pageContext.request.contextPath}/assets/logo-top.svg" type="image/png">
</head>

<body>
    <!-- Main app container -->
    <div class="app">
        <!-- Sidebar navigation -->
        <aside class="sidebar">
            <div class="sidebar-menu">
                <div class="menu-item active2 one" onclick="window.location='homeProfessor'">
                    <img src="${pageContext.request.contextPath}/assets/home-3.svg">
                    <h2>Início</h2>
                </div>
                <div class="menu-item active2 two">
                    <img src="${pageContext.request.contextPath}/assets/greeting.svg" onclick="window.location='bulletinGrades'">
                    <h2>Boletim</h2>
                </div>
                <div class="menu-item active" onclick="location.reload()">
                    <img src="${pageContext.request.contextPath}/assets/notes-2.svg">
                    <h2>Observações</h2>
                </div>
                <div class="menu-item active2 three">
                    <img src="${pageContext.request.contextPath}/assets/info-circle.svg" onclick="window.location='informationProfessor'">
                    <h2>Informações</h2>
                </div>
            </div>
            <!-- User profile section -->
            <div class="profile">
                <img src="${pageContext.request.contextPath}/assets/user.svg" width="38">
            </div>
        </aside>

        <!-- Main -->
        <main class="main">
            <div class="color"></div>

            <div class="top-main">
                <div class="logo-container" onclick="window.location='index.html'">
                    <img src="${pageContext.request.contextPath}/assets/logo.svg" width="180">
                </div>
                <div class="home-button" onclick="location.reload()">
                    <img src="${pageContext.request.contextPath}/assets/notes-3.svg" alt="Página inicial" width="40">
                </div>
            </div>

            <!-- Cards -->
            <div class="bulletin-wrapper">

                <h1 class="cards-title">Ver & cadastrar observações</h1>

                <div class="cards-grid">

                    <div class="grade-card pink" onclick="window.location='obsStudentsList?grade=1'">
                        <div class="card-label">
                            <h2>1º Ano</h2>
                            <p>Clique para ver & cadastrar observações desta turma.</p>
                        </div>
                    </div>

                    <div class="grade-card green" onclick="window.location='obsStudentsList?grade=2'">
                        <div class="card-label">
                            <h2>2º Ano</h2>
                            <p>Clique para ver & cadastrar observações desta turma.</p>
                        </div>
                    </div>

                    <div class="grade-card blue" onclick="window.location='obsStudentsList?grade=3'">
                        <div class="card-label">
                            <h2>3º Ano</h2>
                            <p>Clique para ver & cadastrar observações desta turma.</p>
                        </div>
                    </div>

                    <div class="grade-card yellow" onclick="window.location='obsStudentsList?grade=4'">
                        <div class="card-label">
                            <h2>4º Ano</h2>
                            <p>Clique para ver & cadastrar observações desta turma.</p>
                        </div>
                    </div>

                    <div class="grade-card gray" onclick="window.location='obsStudentsList?grade=5'">
                        <div class="card-label">
                            <h2>5º Ano</h2>
                            <p>Clique para ver & cadastrar observações desta turma.</p>
                        </div>
                    </div>

                </div>
            </div>

        </main>
    </div>
</body>

<script src="${pageContext.request.contextPath}/js/professor/observation.js"></script>

</html>