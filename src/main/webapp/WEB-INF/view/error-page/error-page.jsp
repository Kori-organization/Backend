<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <meta charset="utf-8" />
  <meta name="viewport" content="width=device-width,initial-scale=1" />
  <title>Kori - Erro</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/error-page/error-page.css">
  <link rel="icon" href="${pageContext.request.contextPath}/assets/logo-top.svg" type="image/png">
</head>
<body>
<!-- Background colored circles -->
<div class="bg-shape bg-blue" aria-hidden="true"></div>
<div class="bg-shape bg-pink" aria-hidden="true"></div>
<div class="bg-shape bg-yellow" aria-hidden="true"></div>
<div class="bg-shape bg-red" aria-hidden="true"></div>
<img src="${pageContext.request.contextPath}/assets/x-big.svg" alt="X grande" class="big-x">

<main class="container" role="main" aria-labelledby="title404">
  <section class="right" aria-labelledby="title404">
    <h1 id="title404">Erro</h1>
    <h2>Algo não saiu como esperado.</h2>
    <p>
      Ocorreu um problema ao carregar esta página.
      Tente novamente mais tarde e tente novamente.
    </p>

    <a href="enter" class="btn" id="backHome">Voltar para login</a>

    <div class="small">Se o problema persistir, entre em contato com o suporte.</div>
  </section>
</main>
</body>
<script>
  window.addEventListener("pageshow", function (event) {
    const nav = performance.getEntriesByType("navigation")[0];

    if (event.persisted || (nav && nav.type === "back_forward")) {
      window.location.replace("enter");
    }
  });
</script>
</html>
