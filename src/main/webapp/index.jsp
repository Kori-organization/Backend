<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login – Kori</title>
    <link rel="stylesheet" href="css/index.css">
    <link rel="icon" href="assets/logo-top.svg" type="image/png">
</head>
<body>
<!-- Background shapes -->
<div class="shape blue"></div>
<div class="shape pink"></div>
<div class="shape green"></div>
<div class="shape yellow"></div>

<!-- Login card -->
<div class="login-card">
    <div class="logo"><img src="assets/logo.svg" alt="Logo" width="100px" onclick="location.reload()"></div>

    <h1>Seja bem-vindo(a)<br>de volta.</h1>
    <p class="subtitle">Novo aqui? <a href="#">Criar uma conta</a></p>

    <form method="post" action="enter">
        <label>E-mail ou usuário</label>
        <input type="text" name="emailOrUser" placeholder="Digite seu e-mail ou usuário">

        <label>Senha</label>
        <div class="password-field">
            <input type="password" name="password" id="password" placeholder="Digite sua senha">
            <img src="assets/eye-off.svg" class="eye" id="togglePassword" alt="Show password">
        </div>

        <button type="submit">Entrar</button>

        <p class="forgot">
            Esqueceu sua senha? <a href="forgotPassword">Recuperar</a>
        </p>
    </form>
</div>

</body>

<script src="js/index.js"></script>
</html>
