<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!-- Floating Chat Button -->
<div class="chat-toggle" id="chatToggle">
  <img src="${pageContext.request.contextPath}/assets/stars.svg" alt="" width="22px" style="margin-left: -2px">
</div>

<div class="chat-overlay" id="chatOverlay"></div>

<!-- Chat Box -->
<div class="chat-box" id="chatBox">

  <div class="chat-header">
    <div class="chat-controls">
      <button id="expandChat">⤢</button>
      <button id="closeChat">✕</button>
    </div>
  </div>

  <div class="chat-body">
    <div class="chat-avatar">
      <img src="${pageContext.request.contextPath}/assets/blue-moster1.svg" alt="" width="340px">
    </div>

    <p class="chat-text">
      Olá! Estou aqui para te auxiliar na plataforma Kori
      — <br>como funciona e o que você pode fazer com ela.
    </p>

    <div class="chat-suggestions">
      <button>Quais são os eventos próximos?</button>
      <button>Eu vou passar de ano?</button>
    </div>
  </div>

  <div class="chat-input">
    <input type="text" placeholder="Digite aqui sua mensagem">
    <button><img src="${pageContext.request.contextPath}/assets/enter.svg" alt="" width="32px" style="margin-top: 5px"></button>
  </div>

</div>

<div class="chat-conversation" id="chatConversation">

  <div class="chat-header">
    <div class="chat-controls">
      <button id="expandConversation">⤢</button>
      <button id="closeConversation">✕</button>
    </div>
  </div>

  <div class="conversation-body" id="conversationMessages">

    <div class="message bot">
      <img src="${pageContext.request.contextPath}/assets/blue-monster2.svg">
      <div class="bubble">
        Olá! Estou aqui para te auxiliar na plataforma Kori — como funciona e o que você pode fazer com ela.
      </div>
    </div>

  </div>

  <div class="chat-input conversation-input">
    <input type="text" placeholder="Digite aqui sua mensagem">
    <button>
      <img src="${pageContext.request.contextPath}/assets/enter.svg" width="32px" style="margin-top: 5px">
    </button>
  </div>

</div>