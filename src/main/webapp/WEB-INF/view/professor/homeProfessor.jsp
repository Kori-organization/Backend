<%@ page import="com.example.koribackend.model.entity.Professor" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.example.koribackend.dto.RakingDTO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="pt-BR">

<head>
    <!-- Basic page settings -->
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width,initial-scale=1" />
    <title>Kori – Início</title>
    <link rel="icon" href="${pageContext.request.contextPath}/assets/logo-top.svg" type="image/png">

    <!-- Main stylesheet -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/professor/home-screen.css">
    <%
        Professor professor = (Professor) session.getAttribute("professor");
        ArrayList<RakingDTO> raking = (ArrayList<RakingDTO>) request.getAttribute("raking");
     %>
</head>

<body>
    <!-- App wrapper -->
    <div class="app">

        <!-- Sidebar navigation -->
        <aside class="sidebar">
            <div class="sidebar-menu">
                <div class="menu-item active" onclick="location.reload()">
                    <img src="${pageContext.request.contextPath}/assets/home-2.svg" alt="Página inicial">
                    <h2>Início</h2>
                </div>

                <div class="menu-item active2 one" onclick="window.location='bulletinGrades'">
                    <img src="${pageContext.request.contextPath}/assets/greeting.svg" alt="Boletim">
                    <h2>Boletim</h2>
                </div>

                <div class="menu-item active2 two">
                    <img src="${pageContext.request.contextPath}/assets/notes.svg" alt="Observações" onclick="window.location='observationGrades'">
                    <h2>Observações</h2>
                </div>

                <div class="menu-item active2 three">
                    <img src="${pageContext.request.contextPath}/assets/info-circle.svg" alt="Professores" onclick="window.location='informationProfessor'">
                    <h2>Informações</h2>
                </div>
            </div>

            <!-- User profile -->
            <div class="profile" onclick="window.location='profileProfessor'">
                <img src="${pageContext.request.contextPath}/assets/user.svg" alt="Profile picture" width="38px">
            </div>
        </aside>

        <!-- Main content -->
        <main class="main">
            <div class="color"></div>
            <div class="color2"></div>

            <!-- Top header -->
            <div class="top-main">
                <div class="logo-container">
                    <img src="${pageContext.request.contextPath}/assets/logo.svg" alt="Kori Logo" width="180px" onclick="location.reload()">
                </div>
                <div class="home-button">
                    <img src="${pageContext.request.contextPath}/assets/home.svg" alt="Página inicial" width="40px" onclick="location.reload()">
                </div>
            </div>

            <!-- Main layout grid -->
            <div class="grid">

                <!-- Left column -->
                <section class="left-column">

                    <!-- Welcome card -->
                    <div class="card welcome-card">
                        <div>
                            <h1>Olá, Prof. <%= professor.getName()%>!</h1>
                            <p>
                                Não se esqueça <br>
                                de fazer observações <br>
                                para os alunos de suas <br>
                                turmas.
                            </p>
                            <h2 onclick="window.location='observationGrades'">Ver observações</h2>
                        </div>

                        <div class="monster-container">
                            <img src="${pageContext.request.contextPath}/assets/rocket.svg" alt="Monster" width="260px">
                        </div>
                    </div>

                    <!-- Shortcuts -->
                    <div class="shortcuts-container">
                        <div class="card shortcut-card" onclick="window.location='dowloadRegulation?point=homeProfessor'">
                            <img src="${pageContext.request.contextPath}/assets/document-2.svg" alt="School rules" class="shortcut-icon">
                            <div>Ver regulamento escolar</div>
                        </div>

                        <div class="card shortcut-card"
                            onclick="window.location.href='mailto:korieducation@gmail.com?subject=Suporte%20Kori'">
                            <img src="${pageContext.request.contextPath}/assets/support-2.svg" alt="Kori Support" class="shortcut-icon">
                            <div>Suporte Kori</div>
                        </div>
                    </div>

                    <!-- Ranking Card -->
                    <div class="card ranking-card">

                        <!-- Ranking header -->
                        <div class="ranking-header">
                            <span class="ranking-icon"><img src="${pageContext.request.contextPath}/assets/scale.svg" alt="" width="25px"></span>

                            <!-- Ranking title -->
                            <span class="ranking-title">
                                3 Ranking: turmas com médias mais baixas na sua <br>disciplina.
                            </span>
                        </div>

                        <!-- Ranking content -->
                        <div class="ranking-content">
                            <!-- Chart container -->
                            <div class="chart-container">
                                <!-- Bar 1 -->
                                <div class="bar-item">
                                    <!-- Class name -->
                                    <div class="bar-label"><%=raking.get(0).getSerieComplete()%></div>
                                    <!-- Bar background -->
                                    <div class="bar-bg">
                                        <!-- Bar fill -->
                                        <div class="bar-fill" id="bar1"></div>
                                    </div>
                                    <!-- Average value -->
                                    <div class="bar-value">
                                        média <strong id="value1"></strong>
                                    </div>
                                </div>

                                <!-- Bar 2 -->
                                <div class="bar-item">
                                    <div class="bar-label"><%=raking.get(1).getSerieComplete()%></div>
                                    <div class="bar-bg">
                                        <div class="bar-fill red" id="bar2"></div>
                                    </div>
                                    <div class="bar-value">
                                        média <strong id="value2"></strong>
                                    </div>
                                </div>

                                <!-- Bar 3 -->
                                <div class="bar-item">
                                    <div class="bar-label"><%=raking.get(2).getSerieComplete()%></div>
                                    <div class="bar-bg">
                                        <div class="bar-fill yellow" id="bar3"></div>
                                    </div>
                                    <div class="bar-value">
                                        média <strong id="value3"></strong>
                                    </div>
                                </div>
                            </div>

                            <!-- Info box -->
                            <div class="ranking-info">
                                <div class="info-icon"><img src="${pageContext.request.contextPath}/assets/info-circle-3.svg" alt="" width="10px"></div>
                                <div class="info-text" id="rankingText"></div>
                            </div>
                        </div>
                    </div>
                </section>

                <!-- Right column -->
                <aside class="right-column">

                    <!-- Calendar -->
                    <div class="calendar-container">
                        <div class="card">
                            <div class="calendar-header">
                                <button id="previous-month">‹</button>
                                <strong id="current-month">Janeiro 2026</strong>
                                <button id="next-month">›</button>
                            </div>
                            <div class="calendar-grid" id="calendar"></div>
                        </div>
                    </div>
                    <div class="calendar-legend">
                        <div class="legend-item">
                            <span class="legend-color today"></span>
                            <span>Hoje</span>
                        </div>
                        <div class="legend-item">
                            <span class="legend-color holiday"></span>
                            <span>Feriado</span>
                        </div>
                    </div>

                    <!-- Teachers list -->
                    <div class="card teachers-card">
                        <div class="user-group-teacher">
                            <img src="${pageContext.request.contextPath}/assets/user-group.svg" alt="Teachers">
                            <h3>Suas Turmas</h3>
                        </div>

                        <div class="teacher one">
                            <span>1° Série</span>
                            <div class="teacher-icons">
                                <img src="${pageContext.request.contextPath}/assets/notes-4.svg" class="img1">
                                <img src="${pageContext.request.contextPath}/assets/greeting-4.svg" class="img2">
                            </div>
                        </div>


                        <div class="teacher two">
                            <span>2° Série</span>
                            <div class="teacher-icons">
                                <img src="${pageContext.request.contextPath}/assets/notes-4.svg" class="img1">
                                <img src="${pageContext.request.contextPath}/assets/greeting-4.svg" class="img2">
                            </div>
                        </div>


                        <div class="teacher three">
                            <span>3° Série</span>
                            <div class="teacher-icons">
                                <img src="${pageContext.request.contextPath}/assets/notes-4.svg" class="img1">
                                <img src="${pageContext.request.contextPath}/assets/greeting-4.svg" class="img2">
                            </div>
                        </div>


                        <div class="teacher four">
                            <span>4° Série</span>
                            <div class="teacher-icons">
                                <img src="${pageContext.request.contextPath}/assets/notes-4.svg" class="img1">
                                <img src="${pageContext.request.contextPath}/assets/greeting-4.svg" class="img2">
                            </div>

                        </div>

                        <div class="teacher five">
                            <span>5° Série</span>
                            <div class="teacher-icons">
                                <img src="${pageContext.request.contextPath}/assets/notes-4.svg" class="img1">
                                <img src="${pageContext.request.contextPath}/assets/greeting-4.svg" class="img2">
                            </div>

                        </div>
                    </div>
                </aside>
            </div>
        </main>
    </div>

    <!-- Scripts -->
    <script>
        window.dataSystem = {
            ranking: [
                { class: "<%=raking.get(0).getSerieComplete()%>", average: <%=raking.get(0).getAvarageClass()%> },
                { class: "<%=raking.get(1).getSerieComplete()%>", average: <%=raking.get(1).getAvarageClass()%> },
                { class: "<%=raking.get(2).getSerieComplete()%>", average: <%=raking.get(2).getAvarageClass()%> }
            ]
        };
    </script>
    <script src="${pageContext.request.contextPath}/js/professor/home-screen.js"></script>
</body>

</html>