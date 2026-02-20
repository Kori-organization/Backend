<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Senha alterada – Kori</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/new-password.css">
    <link rel="icon" href="${pageContext.request.contextPath}/assets/logo-top.svg" type="image/png">
</head>
<body>
<!-- Background shapes -->
<div class="shape blue"></div>
<div class="shape pink"></div>
<div class="shape green"></div>
<div class="shape yellow"></div>

<!-- Card -->
<div class="login-card success">

    <img src="${pageContext.request.contextPath}/assets/logo.svg" class="logo" width="100px" onclick="location.href='enter'">

    <div class="success-content">
        <img src="${pageContext.request.contextPath}/assets/coral-monster.svg" class="monster">

        <div class="text">
            <h1>Senha alterada<br>com sucesso!</h1>
            <p>Faça login com sua nova senha.</p>

            <button onclick="location.href='enter'">
                Ir para Login
            </button>
        </div>
    </div>

</div>

</body>

<script>
    const monster = document.querySelector('.monster');

    monster.addEventListener('click', () => {
        monster.classList.remove('shake');
        void monster.offsetWidth;
        monster.classList.add('shake');
    });
</script>
</html>
