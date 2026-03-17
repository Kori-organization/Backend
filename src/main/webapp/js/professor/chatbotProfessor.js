/* ==============================
   CHAT CONFIG
============================== */

const CHAT_API_URL = window.APP_CONFIG?.chatApiUrl;
let professorId = window.APP_CONFIG?.professorId;

let isSendingMessage = false;
let chatExpanded = false;
let conversationStarted = false;

/* ==============================
   CHAT ELEMENTS
============================== */

const chatToggle = document.getElementById("chatToggle");
const chatBox = document.getElementById("chatBox");
const chatConversation = document.getElementById("chatConversation");

const closeChat = document.getElementById("closeChat");
const closeConversation = document.getElementById("closeConversation");

const expandChat = document.getElementById("expandChat");
const expandConversation = document.getElementById("expandConversation");

const chatOverlay = document.getElementById("chatOverlay");

const conversationMessages = document.getElementById("conversationMessages");

const chatInput = document.querySelector(".chat-input input");
const chatSendBtn = document.querySelector(".chat-input button");

const conversationInput = document.querySelector("#chatConversation .chat-input input");
const conversationSendBtn = document.querySelector("#chatConversation .chat-input button");

const suggestionButtons = document.querySelectorAll(".chat-suggestions button");

/* ==============================
   OPEN / CLOSE CHAT
============================== */

function openChat() {
    if (conversationStarted) {
        chatConversation.style.display = "flex";
        chatBox.style.display = "none";
        chatConversation.classList.toggle("expanded", chatExpanded);
    } else {
        chatBox.style.display = "flex";
        chatConversation.style.display = "none";
        chatBox.classList.toggle("expanded", chatExpanded);
    }

    chatOverlay.classList.add("show");
}

function closeChatBox() {
    chatBox.style.display = "none";
    chatConversation.style.display = "none";
    chatOverlay.classList.remove("show");
}

chatToggle.addEventListener("click", () => {
    if (chatBox.style.display === "flex" || chatConversation.style.display === "flex") {
        closeChatBox();
    } else {
        openChat();
    }
});

closeChat.addEventListener("click", closeChatBox);
closeConversation.addEventListener("click", closeChatBox);
chatOverlay.addEventListener("click", closeChatBox);

/* ==============================
   EXPAND CHAT
============================== */

expandChat.addEventListener("click", () => {
    chatExpanded = !chatExpanded;
    chatBox.classList.toggle("expanded", chatExpanded);
});

expandConversation.addEventListener("click", () => {
    chatExpanded = !chatExpanded;
    chatConversation.classList.toggle("expanded", chatExpanded);
});

/* ==============================
   MESSAGE HELPERS
============================== */

function scrollConversationToBottom() {
    conversationMessages.scrollTop = conversationMessages.scrollHeight;
}

function formatBotMessage(text) {
    if (!text) return "";

    text = text.replace(/\*\*(.*?)\*\*/g, '<span class="chat-bold">$1</span>');

    const lines = text.split("\n");

    let html = "";
    let insideList = false;

    lines.forEach(line => {
        const trimmed = line.trim();

        if (trimmed.startsWith("* ")) {
            if (!insideList) {
                html += "<ul class='chat-list'>";
                insideList = true;
            }

            html += `<li>${trimmed.substring(2)}</li>`;
        } else {
            if (insideList) {
                html += "</ul>";
                insideList = false;
            }

            if (trimmed !== "") {
                html += `<div>${trimmed}</div>`;
            }
        }
    });

    if (insideList) {
        html += "</ul>";
    }

    return html;
}

function addUserMessage(text) {
    const msg = document.createElement("div");
    msg.className = "message user";
    msg.innerHTML = `<div class="bubble"></div>`;
    msg.querySelector(".bubble").textContent = text;

    conversationMessages.appendChild(msg);
    scrollConversationToBottom();
}

function addBotMessage(text) {
    const msg = document.createElement("div");
    msg.className = "message bot";

    msg.innerHTML = `
        <img src="${contextPath}/assets/blue-monster2.svg" alt="Bot">
        <div class="bubble"></div>
    `;

    msg.querySelector(".bubble").innerHTML = formatBotMessage(text);

    conversationMessages.appendChild(msg);
    scrollConversationToBottom();
}

function addTypingMessage() {
    const msg = document.createElement("div");
    msg.className = "message bot typing-message";
    msg.id = "typingMessage";

    msg.innerHTML = `
        <img src="${contextPath}/assets/blue-monster2.svg" alt="Bot">
        <div class="bubble">Pensando...</div>
    `;

    conversationMessages.appendChild(msg);
    scrollConversationToBottom();
}

function removeTypingMessage() {
    const typingMessage = document.getElementById("typingMessage");
    if (typingMessage) typingMessage.remove();
}

function setConversationInputState(disabled) {
    conversationInput.disabled = disabled;
    conversationSendBtn.disabled = disabled;
}

/* ==============================
   CONVERSATION STATE
============================== */

function initializeConversation() {
    if (conversationStarted) return;

    conversationMessages.innerHTML = `
        <div class="message bot">
            <img src="${contextPath}/assets/blue-monster2.svg" alt="Bot">
            <div class="bubble">
                Olá! Estou aqui para te auxiliar na plataforma Kori. Você pode me fazer perguntas sobre turmas, desempenho, eventos e informações da plataforma.
            </div>
        </div>
    `;

    conversationStarted = true;
}

function showConversationScreen() {
    chatBox.style.display = "none";
    chatConversation.style.display = "flex";
    chatConversation.classList.toggle("expanded", chatExpanded);
    chatOverlay.classList.add("show");
}

/* ==============================
   API
============================== */

async function sendQuestionToAI(question) {
    const payload = {
        professor_id: Number(professorId),
        question: question
    };
    const response = await fetch(CHAT_API_URL, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(payload)
    });
    const data = await response.json();
    if (!response.ok) {
        throw new Error(data.detail || `Erro HTTP: ${response.status}`);
    }

    if (data.status && data.status !== "ok") {
        throw new Error(data.answer || "Erro da API.");
    }

    return data.answer || data.response || data.message || "Sem resposta da IA.";
}

/* ==============================
   CONVERSATION SYSTEM
============================== */

function startConversation(message) {
    initializeConversation();
    showConversationScreen();

    addUserMessage(message);
    handleAIResponse(message);
}

async function handleAIResponse(question) {
    if (isSendingMessage) return;

    isSendingMessage = true;
    setConversationInputState(true);
    addTypingMessage();

    try {
        const answer = await sendQuestionToAI(question);
        removeTypingMessage();
        addBotMessage(answer);
    } catch (error) {
        console.error("Erro ao enviar pergunta para a IA:", error);
        removeTypingMessage();
        addBotMessage("Desculpe, não foi possível gerar a resposta agora.");
    } finally {
        isSendingMessage = false;
        setConversationInputState(false);
        conversationInput.focus();
    }
}

function sendConversationMessage() {
    const text = conversationInput.value.trim();

    if (text === "" || isSendingMessage) return;

    addUserMessage(text);
    conversationInput.value = "";
    handleAIResponse(text);
}

/* ==============================
   FIRST CHAT INPUT
============================== */

chatInput.addEventListener("keypress", (e) => {
    if (e.key === "Enter") {
        const text = chatInput.value.trim();

        if (text !== "") {
            startConversation(text);
            chatInput.value = "";
        }
    }
});

chatSendBtn.addEventListener("click", () => {
    const text = chatInput.value.trim();

    if (text !== "") {
        startConversation(text);
        chatInput.value = "";
    }
});

/* ==============================
   SUGGESTION BUTTONS
============================== */

suggestionButtons.forEach(btn => {
    btn.addEventListener("click", () => {
        startConversation(btn.innerText.trim());
    });
});

/* ==============================
   CONVERSATION INPUT
============================== */

conversationInput.addEventListener("keypress", (e) => {
    if (e.key === "Enter") {
        sendConversationMessage();
    }
});

conversationSendBtn.addEventListener("click", sendConversationMessage);