<%--
  Created by IntelliJ IDEA.
  User: lucaslima-ieg
  Date: 25/02/2026
  Time: 16:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="icon" type="image/png" href="assets/logo-top.svg">
    <title id="title">Kori - Carregando</title>

    <style>
        body {
            margin: 0;
            height: 100vh;
            background: #F9F5EC;
            display: flex;
            justify-content: center;
            align-items: center;
            font-family: 'Nunito', sans-serif;
            overflow: hidden;
            user-select: none;
        }

        .loading-container {
            text-align: center;
            width: 100%;
        }

        /* ===== SCENARIO ===== */
        .scene {
            position: relative;
            width: 100%;
            max-width: 700px;
            height: 220px;
            margin: auto;
            overflow: hidden;
            border-bottom: 4px solid #6BCF9B;
            background: linear-gradient(#AEE6FF, #EAF8FF);
            border-radius: 20px;
            transform: scale(1.7);
            margin-top: 100px;
        }

        /* ===== CLOUDS ===== */
        .cloud {
            position: absolute;
            top: 30px;
            width: 80px;
            height: 40px;
            background: #fff;
            border-radius: 50px;
            animation: cloud 20s linear infinite;
        }

        .cloud::before,
        .cloud::after {
            content: '';
            position: absolute;
            background: #fff;
            width: 40px;
            height: 40px;
            border-radius: 50%;
            top: -20px;
        }

        .cloud::before {
            left: 10px;
        }

        .cloud::after {
            right: 10px;
        }

        .cloud:nth-child(1) { left: -100px; animation-delay: 0s; }
        .cloud:nth-child(2) { top: 60px; left: -200px; animation-delay: 8s; }

        /* ===== BUS ===== */
        .bus {
            position: absolute;
            bottom: 10px;
            left: -150px;
            width: 140px;
            height: 70px;
            background: #F5C75B;
            border-radius: 15px;
            animation: bus 5s linear infinite;
        }

        .bus::before {
            content: '';
            position: absolute;
            bottom: -12px;
            left: 20px;
            width: 25px;
            height: 25px;
            background: #444;
            border-radius: 50%;
            box-shadow: 70px 0 #444;
        }

        .window {
            position: absolute;
            top: 15px;
            left: 15px;
            width: 30px;
            height: 20px;
            background: #AEE6FF;
            border-radius: 5px;
            box-shadow: 35px 0 #AEE6FF, 70px 0 #AEE6FF;
        }

        /* ===== TEXT ===== */
        .loading-text {
            font-size: 22px;
            color: #3A5A5E;
            font-weight: 700;
            margin-top: 150px;
        }

        .loading-sub {
            font-size: 16px;
            margin-top: 10px;
            color: #6A8F94;
        }

        /* ===== ANIMATION ===== */
        @keyframes bus {
            0% { transform: translateX(0); }
            100% { transform: translateX(900px); }
        }

        @keyframes cloud {
            0% { transform: translateX(0); }
            100% { transform: translateX(900px); }
        }

        /* ===== RESPONSIVE ===== */
        @media (max-width: 600px) {
            .scene {
                height: 180px;
            }

            .loading-text {
                font-size: 18px;
            }
        }
    </style>
</head>
<body>

<div class="loading-container">
    <div class="scene">
        <div class="cloud"></div>
        <div class="cloud"></div>

        <div class="bus">
            <div class="window"></div>
        </div>
    </div>

    <div class="loading-text">Carregando a kori… </div>
    <div class="loading-sub">Preparando um mundo de aprendizado</div>
</div>
<script>
    function checkStatus() {
        fetch('status')
            .then(res => res.text())
            .then(data => {
                if (data === "READY") {
                    window.location.href = 'enter';
                } else {
                    setTimeout(checkStatus, 1000);
                }
            });
    }
    checkStatus();
</script>
<script>
    history.pushState(null, null, location.href);
    window.onpopstate = function () {
        history.go(1);
    };
    function load() {
        let text = ''
        const title = document.getElementById("title");
        switch (title.innerHTML) {
            case "Kori - Carregando":
                text = "Kori - Carregando.";
                break;
            case "Kori - Carregando.":
                text = "Kori - Carregando..";
                break;
            case "Kori - Carregando..":
                text = "Kori - Carregando...";
                break;
            case "Kori - Carregando...":
                text = "Kori - Carregando"
                break;
        }
        title.innerHTML = text;
        setTimeout(load,500)
    }
    load()
</script>
</body>
</html>
