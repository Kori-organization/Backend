<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Esqueceu a senha – Kori</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/forgot-password.css">
  <link rel="icon" href="${pageContext.request.contextPath}/assets/logo-top.svg" type="image/png">
</head>
<body>
<!-- Background shapes -->
<div class="shape blue"></div>
<div class="shape pink"></div>
<div class="shape green"></div>
<div class="shape yellow"></div>

<!-- Card -->
<div class="login-card">
  <div class="logo">
    <img src="${pageContext.request.contextPath}/assets/logo.svg" alt="Logo" width="100px" onclick="location.href='enter'">
  </div>

  <h1>Esqueceu sua senha?</h1>
  <p class="subtitle">
    Não se preocupe, isso acontece.
    <br>Digite o e-mail associado à sua conta.
  </p>

  <form action="recoverPassword" method="post">
    <label>E-mail</label>
    <input type="email" name="email" placeholder="Digite seu e-mail">
    <button type="submit">
      Enviar e-mail
    </button>
    <p class="forgot">
      Lembrou da senha? <a href="enter">Entrar</a>
    </p>
  </form>
</div>

<button class="back-button" onclick="location.href='enter'">
  ← Voltar
</button>
</body>
</html>
