<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Criar nova senha – Kori</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/create-password.css">
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
    <img src="${pageContext.request.contextPath}/assets/logo.svg" alt="Logo" width="100px" onclick="location.href='../index.html'">
  </div>

  <h1>Criar nova senha</h1>
  <p class="subtitle">
    Sua senha precisa ser diferente da sua senha anterior.
  </p>

  <form method="post" action="updatePassword">
    <label>Nova senha</label>
    <div class="password-field">
      <input type="password" name="newPassword" id="newPassword" placeholder="Digite a nova senha">
      <img
              src="${pageContext.request.contextPath}/assets/eye-off.svg"
              class="eye toggle-password"
              data-target="newPassword"
              alt="Mostrar senha"
      >
    </div>

    <label>Confirmar nova senha</label>
    <div class="password-field">
      <input type="password" name="confirmNewPassword" id="confirmNewPassword" placeholder="Confirme a nova senha">
      <img
              src="${pageContext.request.contextPath}/assets/eye-off.svg"
              class="eye toggle-password"
              data-target="confirmNewPassword"
              alt="Mostrar senha"
      >
    </div>

    <button type="submit">Criar</button>
  </form>
</div>

<button class="back-button" onclick="location.href='forgot-password.html'">
  ← Voltar
</button>
</body>
<script>
  const contextPath = '${pageContext.request.contextPath}';
</script>
<script src="${pageContext.request.contextPath}/js/create-password.js"></script>
</html>
