<%--
  Created by IntelliJ IDEA.
  User: lucaslima-ieg
  Date: 13/02/2026
  Time: 09:52
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Kori - Criar Conta</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/create-account.css">
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
    <img src="${pageContext.request.contextPath}/assets/logo.svg" width="100px" onclick="location.href='index.html'">
  </div>

  <h1>Criar conta</h1>
  <p class="subtitle">Já esteve aqui antes? <a href="enter">Entrar</a></p>

  <form method="post" action="createAccount">

    <label>Nome completo</label>
    <input type="text" name="name" placeholder="Digite seu nome completo" required>

    <label>CPF</label>
    <input type="text" name="cpf" placeholder="Digite seu CPF" pattern="^\d{11}$|^\d{3}\.\d{3}\.\d{3}-\d{2}$" title="Digite seu cpf no formato 123.456.789-10 ou apenas os numeros" required>

    <label>E-mail</label>
    <input type="email" name="email" id="email" placeholder="Digite seu e-mail" required>

    <label>Criar senha</label>
    <div class="password-field">
      <input type="password" name="password" id="password" placeholder="Crie sua senha" required>
      <img src="${pageContext.request.contextPath}/assets/eye-off.svg" class="eye" id="togglePassword">
    </div>

    <button type="submit">Entrar</button>

  </form>
</div>

<button class="back-button" onclick="location.href='enter'">
  ← Voltar
</button>

</body>
<script>
  const contextPath = '${pageContext.request.contextPath}';
</script>
<script src="${pageContext.request.contextPath}/js/create-account.js"></script>
</html>
