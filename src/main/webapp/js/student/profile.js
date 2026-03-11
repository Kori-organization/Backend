/* ==============================
   LOGOUT MODAL
============================== */

const openBtn = document.getElementById("openLogout");
const overlay = document.getElementById("logoutOverlay");
const cancelBtn = document.getElementById("cancelLogout");

openBtn.addEventListener("click", () => {
    overlay.classList.add("show");
});

cancelBtn.addEventListener("click", () => {
    overlay.classList.remove("show");
});

overlay.addEventListener("click", (e) => {
    if (e.target === overlay) {
        overlay.classList.remove("show");
    }
});


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

/* GLOBAL EXPAND STATE */
let chatExpanded = false;


/* ==============================
   OPEN / CLOSE CHAT
============================== */

function openChat() {

    chatBox.style.display = "flex";
    chatOverlay.classList.add("show");

    chatBox.classList.toggle("expanded", chatExpanded);
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
   CONVERSATION SYSTEM
============================== */

function startConversation(message) {

    chatBox.style.display = "none";
    chatConversation.style.display = "flex";

    chatConversation.classList.toggle("expanded", chatExpanded);

    resetConversation();

    addUserMessage(message);
}

function addUserMessage(text) {

    const msg = document.createElement("div");
    msg.className = "message user";

    msg.innerHTML = `<div class="bubble">${text}</div>`;

    conversationMessages.appendChild(msg);

    conversationMessages.scrollTop = conversationMessages.scrollHeight;
}

function sendConversationMessage() {

    const text = conversationInput.value.trim();

    if (text === "") return;

    addUserMessage(text);

    conversationInput.value = "";
}


/* ==============================
   FIRST CHAT INPUT
============================== */

chatInput.addEventListener("keypress", (e) => {

    if (e.key === "Enter") {

        if (chatInput.value.trim() !== "") {
            startConversation(chatInput.value);
            chatInput.value = "";
        }

    }

});

chatSendBtn.addEventListener("click", () => {

    if (chatInput.value.trim() !== "") {
        startConversation(chatInput.value);
        chatInput.value = "";
    }

});


/* ==============================
   SUGGESTION BUTTONS
============================== */

suggestionButtons.forEach(btn => {

    btn.addEventListener("click", () => {
        startConversation(btn.innerText);
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

function resetConversation() {

    conversationMessages.innerHTML = `
        <div class="message bot">
            <img src="${contextPath}/assets/blue-monster2.svg">
            <div class="bubble">
                Olá! Estou aqui para te auxiliar na plataforma Kori — como funciona e o que você pode fazer com ela.
            </div>
        </div>
    `;

}