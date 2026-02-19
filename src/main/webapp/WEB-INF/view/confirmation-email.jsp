<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Kori - Email enviado</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/confirmation-email.css">
  <link rel="icon" href="${pageContext.request.contextPath}/assets/logo-top.svg" type="image/png">
  <%
    boolean result = (boolean) request.getAttribute("result");
  %>
</head>
<body>
<!-- Background shapes -->
<div class="shape blue"></div>
<div class="shape pink"></div>
<div class="shape green"></div>
<div class="shape yellow"></div>

<!-- Card -->
<div class="login-card success-card">
  <div class="logo">
    <img src="${pageContext.request.contextPath}/assets/logo.svg" alt="Logo" width="100px" onclick="location.href='enter'">
  </div>

  <!-- Header -->
  <div class="success-header">
    <img src="${pageContext.request.contextPath}/assets/<%= result ? "check-icon.svg" : "error-icon.svg" %>" alt="<%= result ? "Email enviado" : "Erro" %>" class="success-icon">
    <h1><%= result ? "Email enviado!" : "Não foi possível<br>enviar o email." %></h1>
  </div>

  <% if (result) { %>
    <p class="subtitle">
      Vá até sua caixa de entrada e clique em <br>
      Alterar Senha no email que mandamos para <br>
      <span>${requestScope.email}</span>.
    </p>
  <% } else { %>
    <p class="subtitle">
      Por favor, verifique se o endereço<br>
      <span>${requestScope.email}</span>
      está apto a receber mensagens e tente novamente.
    </p>
  <% } %>

  <% if (result) { %>
    <p class="resend-text">
      Não recebeu um email?
    </p>
  <% } %>

  <button type="button" onclick="location.href='forgotPassword'">
    Enviar email novamente
  </button>
</div>

<!-- Back button -->
<button class="back-button" onclick="location.href='forgotPassword'">
  ← Voltar
</button>
</body>
</html>
