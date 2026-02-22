<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="pt-BR">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Kori – Boletim</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/professor/bulletin-grades.css">
    <link rel="icon" href="${pageContext.request.contextPath}/assets/logo-top.svg" type="image/png">
</head>

<body>
    <div class="app">

        <!-- Sidebar -->
        <aside class="sidebar">
            <div class="sidebar-menu">
                <div class="menu-item active2 one" onclick="window.location='homeProfessor'">
                    <img src="${pageContext.request.contextPath}/assets/home-3.svg">
                    <h2>Início</h2>
                </div>
                <div class="menu-item active" onclick="location.reload()">
                    <img src="${pageContext.request.contextPath}/assets/greeting-2.svg">
                    <h2>Boletim</h2>
                </div>
                <div class="menu-item active2 two" onclick="window.location='observationGrades'">
                    <img src="${pageContext.request.contextPath}/assets/notes.svg">
                    <h2>Observações</h2>
                </div>
                <div class="menu-item active2 three" onclick="window.location='informationProfessor'">
                    <img src="${pageContext.request.contextPath}/assets/info-circle.svg">
                    <h2>Informações</h2>
                </div>
            </div>

            <div class="profile" onclick="window.location='profileProfessor'">
                <img src="${pageContext.request.contextPath}/assets/user.svg">
            </div>
        </aside>

        <!-- Main -->
        <main class="main">
            <div class="color"></div>

            <div class="top-main">
                <div class="logo-container" onclick="window.location='homeProfessor'">
                    <img src="${pageContext.request.contextPath}/assets/logo.svg" width="180">
                </div>
                <div class="home-button" onclick="location.reload()">
                    <img src="${pageContext.request.contextPath}/assets/greeting-3.svg" width="50">
                </div>
            </div>

            <!-- Cards -->
            <div class="bulletin-wrapper">

                <h1 class="cards-title">Cadastrar notas</h1>

                <div class="cards-grid">

                    <div class="grade-card pink" onclick="window.location='bulletinStudentsList?grade=1'">
                        <div class="card-label">
                            <h2>1º Ano</h2>
                            <p>Clique para cadastrar notas desta turma.</p>
                        </div>
                    </div>

                    <div class="grade-card green" onclick="window.location='bulletinStudentsList?grade=2'">
                        <div class="card-label">
                            <h2>2º Ano</h2>
                            <p>Clique para cadastrar notas desta turma.</p>
                        </div>
                    </div>

                    <div class="grade-card blue" onclick="window.location='bulletinStudentsList?grade=3'">
                        <div class="card-label">
                            <h2>3º Ano</h2>
                            <p>Clique para cadastrar notas desta turma.</p>
                        </div>
                    </div>

                    <div class="grade-card yellow" onclick="window.location='bulletinStudentsList?grade=4'">
                        <div class="card-label">
                            <h2>4º Ano</h2>
                            <p>Clique para cadastrar notas desta turma.</p>
                        </div>
                    </div>

                    <div class="grade-card gray" onclick="window.location='bulletinStudentsList?grade=5'">
                        <div class="card-label">
                            <h2>5º Ano</h2>
                            <p>Clique para cadastrar notas desta turma.</p>
                        </div>
                    </div>

                </div>
            </div>

        </main>
    </div>
</body>

</html>
