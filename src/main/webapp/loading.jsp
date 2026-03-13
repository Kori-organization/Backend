<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/assets/logo-top.svg">
    <title id="title">Kori - Carregando</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/loading.css">
</head>
<body>

<div class="loading-container">
    <div class="scene">
        <div class="cloud"></div>
        <div class="cloud"></div>
        <div class="road"></div>

        <div class="bus">
            <div class="bus-body">
                <div class="window"></div>
            </div>

            <div class="wheel back"></div>
            <div class="wheel front"></div>

            <div class="smoke s1"></div>
            <div class="smoke s2"></div>
            <div class="smoke s3"></div>
        </div>
    </div>

    <div class="loading-text">Carregando a kori…</div>
    <div class="loading-sub">Preparando um mundo de aprendizado</div>
</div>

<script src="${pageContext.request.contextPath}/js/loading.js"></script>
</body>
</html>